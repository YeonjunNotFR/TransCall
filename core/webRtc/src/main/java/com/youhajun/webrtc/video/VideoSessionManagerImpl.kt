package com.youhajun.webrtc.video

import com.youhajun.webrtc.Config
import com.youhajun.webrtc.model.CallVideoStream
import com.youhajun.webrtc.model.LocalVideoEvent
import com.youhajun.webrtc.model.LocalVideoStream
import com.youhajun.webrtc.model.MediaContentType
import com.youhajun.webrtc.model.MediaMessage
import com.youhajun.webrtc.model.RemoteVideoStream
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
        cameraTrack.setEnabled(enabled)
        val result = if(enabled) cameraController.startCapture() else cameraController.stopCapture()
        result.onSuccess {
            videoStreamStore.update(localUserId, MediaContentType.DEFAULT) {
                (it as LocalVideoStream).copy(
                    isVideoEnable = enabled
                )
            }
            _localVideoEvent.tryEmit(LocalVideoEvent.EnabledChanged(enabled))
        }.onFailure {
            cameraTrack.setEnabled(!enabled)
        }
    }

    override fun flipCamera(localUserId: String) {
        cameraController.switchCamera { isFrontCamera ->
            videoStreamStore.update(localUserId, MediaContentType.DEFAULT) {
                (it as LocalVideoStream).copy(
                    isFrontCamera = isFrontCamera
                )
            }
        }
    }

    override fun addRemoteVideoTrack(remoteVideo: RemoteVideoStream) {
        videoStreamStore.upsert(remoteVideo)
    }

    override fun setVideoStateChange(state: MediaMessage.VideoStateChange) {
        val mediaType = MediaContentType.fromType(state.mediaContentType)
        videoStreamStore.update(state.userId, mediaType) {
            (it as RemoteVideoStream).copy(
                isVideoEnable = state.isVideoEnabled
            )
        }
    }

    override fun dispose() {
        cameraController.dispose()
        cameraSource.dispose()
        cameraTrack.dispose()
    }
}
