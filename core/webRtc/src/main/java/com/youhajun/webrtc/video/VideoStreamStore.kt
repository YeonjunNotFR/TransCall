package com.youhajun.webrtc.video

import com.youhajun.webrtc.model.local.MediaContentType
import com.youhajun.webrtc.model.stream.CallVideoStream
import com.youhajun.webrtc.model.stream.LocalVideoStream
import com.youhajun.webrtc.model.stream.RemoteVideoStream
import kotlinx.coroutines.flow.StateFlow

internal interface VideoStreamStore {
    val videoStreamsFlow: StateFlow<List<CallVideoStream>>

    fun upsert(stream: CallVideoStream)
    fun remove(userId: String, mediaContentType: MediaContentType)
    fun updateDefaultLocal(userId: String, transform: (LocalVideoStream) -> LocalVideoStream)
    fun updateDefaultRemote(userId: String, transform: (RemoteVideoStream) -> RemoteVideoStream)
}