package com.youhajun.webrtc.video

import com.youhajun.webrtc.model.stream.CallVideoStream
import com.youhajun.webrtc.model.local.LocalVideoEvent
import com.youhajun.webrtc.model.media.MediaState
import com.youhajun.webrtc.model.stream.RemoteVideoStream
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import org.webrtc.VideoTrack

internal interface VideoSessionManager {
    val videoStreamsFlow: StateFlow<List<CallVideoStream>>
    val localVideoEvent: SharedFlow<LocalVideoEvent>

    fun startCamera(localUserId: String): VideoTrack
    fun dispose()
    fun flipCamera(localUserId: String)
    fun setCameraEnabled(localUserId: String, enabled: Boolean)
    fun addRemoteVideoTrack(remoteVideo: RemoteVideoStream)
    fun onMediaStateChanged(state: MediaState)
    fun onMediaStateInit(list: List<MediaState>)
}