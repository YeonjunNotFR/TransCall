package com.youhajun.webrtc.video

import com.youhajun.webrtc.Config
import com.youhajun.webrtc.model.stream.CallVideoStream
import com.youhajun.webrtc.model.local.LocalVideoEvent
import com.youhajun.webrtc.model.stream.LocalVideoStream
import com.youhajun.webrtc.model.local.MediaContentType
import com.youhajun.webrtc.model.media.MediaState
import com.youhajun.webrtc.model.stream.RemoteVideoStream
import com.youhajun.webrtc.peer.StreamPeerConnectionFactory
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.webrtc.VideoTrack
import javax.inject.Inject

internal class VideoSessionManagerImpl @Inject constructor(
    private val peerConnectionFactory: StreamPeerConnectionFactory,
    private val cameraController: CameraController,
    private val videoStreamStore: VideoStreamStore
) : VideoSessionManager {

    override val videoStreamsFlow: StateFlow<List<CallVideoStream>> = videoStreamStore.videoStreamsFlow

    private val _localVideoEvent = MutableSharedFlow<LocalVideoEvent>(extraBufferCapacity = 1)
    override val localVideoEvent: SharedFlow<LocalVideoEvent> = _localVideoEvent.asSharedFlow()

    private val cameraSource by lazy {
        peerConnectionFactory.makeVideoSource(false)
    }

    private val cameraTrack: VideoTrack by lazy {
        val trackId = Config.getVideoTrackId(MediaContentType.DEFAULT)
        peerConnectionFactory.makeVideoTrack(cameraSource, trackId)
    }

    override fun startCamera(localUserId: String): VideoTrack {
        cameraController.initCapture(cameraSource.capturerObserver)
        cameraController.startCapture()
        return cameraTrack.also {
            val localStream = LocalVideoStream(localUserId, MediaContentType.DEFAULT.type, it, true, cameraController.isFrontCamera)
            videoStreamStore.upsert(localStream)
        }
    }

    override fun setCameraEnabled(localUserId: String, enabled: Boolean) {
        videoStreamStore.updateDefaultLocal(localUserId) { it.copy(isVideoEnable = enabled) }
        val result = if(enabled) cameraController.startCapture() else cameraController.stopCapture()
        result.onSuccess {
            _localVideoEvent.tryEmit(LocalVideoEvent.CameraEnabledChanged(enabled))
        }.onFailure {
            videoStreamStore.updateDefaultLocal(localUserId) { it.copy(isVideoEnable = !enabled) }
        }
    }

    override fun flipCamera(localUserId: String) {
        cameraController.switchCamera { isFrontCamera ->
            videoStreamStore.updateDefaultLocal(localUserId) {
                it.copy(isFrontCamera = isFrontCamera)
            }
        }
    }

    override fun addRemoteVideoTrack(remoteVideo: RemoteVideoStream) {
        videoStreamStore.upsert(remoteVideo)
    }

    override fun removeRemoteVideoTrack(userId: String, mediaContentType: MediaContentType) {
        videoStreamStore.remove(userId, mediaContentType)
    }

    override fun dispose() {
        cameraController.stopCapture()
        runCatching { cameraSource.dispose() }
        runCatching { cameraTrack.dispose() }
        cameraController.dispose()
    }

    override fun onMediaStateChanged(state: MediaState) {
        videoStreamStore.updateDefaultRemote(state.userId) {
            it.copy(isVideoEnable = state.cameraEnabled)
        }
    }

    override fun onMediaStateInit(list: List<MediaState>) {
        list.forEach { onMediaStateChanged(it) }
    }
}
