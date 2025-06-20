package com.youhajun.webrtc.video

import com.youhajun.webrtc.model.CallVideoStream
import com.youhajun.webrtc.model.LocalVideoEvent
import com.youhajun.webrtc.model.MediaMessage
import com.youhajun.webrtc.model.RemoteVideoStream
import dagger.assisted.AssistedFactory
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import org.webrtc.VideoTrack

interface VideoSessionManager {
    val videoStreamsFlow: StateFlow<List<CallVideoStream>>
    val localVideoEvent: SharedFlow<LocalVideoEvent>

    fun startCamera(): VideoTrack
    fun dispose()
    fun flipCamera()
    fun setCameraEnabled(enabled: Boolean)
    fun addRemoteVideoTrack(remoteVideo: RemoteVideoStream)
    fun setVideoStateChange(state: MediaMessage.VideoStateChange)

    @AssistedFactory
    interface Factory {
        fun create(localUserId: String): VideoSessionManagerImpl
    }
}