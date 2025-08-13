package com.youhajun.webrtc.video

import com.youhajun.webrtc.model.CallVideoStream
import com.youhajun.webrtc.model.MediaContentType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

internal class VideoStreamStoreImpl @Inject constructor() : VideoStreamStore {

    private val _videoStreamsFlow = MutableStateFlow<List<CallVideoStream>>(emptyList())
    override val videoStreamsFlow: StateFlow<List<CallVideoStream>> = _videoStreamsFlow.asStateFlow()

    override fun upsert(stream: CallVideoStream) {
        _videoStreamsFlow.update { list ->
            list.toMutableList().apply {
                val idx = indexOfFirst { it.key == stream.key }
                if (idx >= 0) set(idx, stream) else add(stream)
            }
        }
    }

    override fun update(
        userId: String,
        mediaContentType: MediaContentType,
        transform: (CallVideoStream) -> CallVideoStream
    ) {
        _videoStreamsFlow.update { list ->
            list.map { if (it.key == userId + mediaContentType.type) transform(it) else it }
        }
    }
}