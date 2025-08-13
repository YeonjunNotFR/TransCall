package com.youhajun.webrtc.video

import com.youhajun.webrtc.model.CallVideoStream
import com.youhajun.webrtc.model.MediaContentType
import kotlinx.coroutines.flow.StateFlow

internal interface VideoStreamStore {
    val videoStreamsFlow: StateFlow<List<CallVideoStream>>

    fun upsert(stream: CallVideoStream)
    fun update(userId: String, mediaContentType: MediaContentType, transform: (CallVideoStream) -> CallVideoStream)
}