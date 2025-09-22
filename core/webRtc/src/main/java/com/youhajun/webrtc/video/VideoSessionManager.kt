package com.youhajun.webrtc.video

import com.youhajun.webrtc.model.CallVideoStream
import com.youhajun.webrtc.model.LocalVideoEvent
import com.youhajun.webrtc.model.MediaState
import com.youhajun.webrtc.model.RemoteVideoStream
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