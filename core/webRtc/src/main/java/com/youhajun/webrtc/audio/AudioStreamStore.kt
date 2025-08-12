package com.youhajun.webrtc.audio

import com.youhajun.webrtc.model.CallAudioStream
import com.youhajun.webrtc.model.MediaContentType
import kotlinx.coroutines.flow.StateFlow

internal interface AudioStreamStore {
    val audioStreamsFlow: StateFlow<List<CallAudioStream>>

    fun upsert(stream: CallAudioStream)
    fun update(userId: String, mediaContentType: MediaContentType, transform: (CallAudioStream) -> CallAudioStream)
    fun updateAll(transform: (CallAudioStream) -> CallAudioStream)
}