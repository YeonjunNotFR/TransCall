package com.youhajun.webrtc.audio

import com.youhajun.webrtc.model.CallAudioStream
import com.youhajun.webrtc.model.CallMediaKey
import com.youhajun.webrtc.model.MediaContentType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

internal class AudioStreamStoreImpl @Inject constructor() : AudioStreamStore {

    private val _audioStreamsFlow = MutableStateFlow<List<CallAudioStream>>(emptyList())
    override val audioStreamsFlow: StateFlow<List<CallAudioStream>> = _audioStreamsFlow.asStateFlow()

    override fun upsert(stream: CallAudioStream) {
        _audioStreamsFlow.update { list ->
            list.toMutableList().apply {
                val idx = indexOfFirst { it.key == stream.key }
                if (idx >= 0) set(idx, stream) else add(stream)
            }
        }
    }

    override fun updateAll(transform: (CallAudioStream) -> CallAudioStream) {
        _audioStreamsFlow.update { list ->
            list.map { transform(it) }
        }
    }

    override fun update(
        userId: String,
        mediaContentType: MediaContentType,
        transform: (CallAudioStream) -> CallAudioStream
    ) {
        _audioStreamsFlow.update { list ->
            list.map { if (it.key == CallMediaKey.createKey(userId, mediaContentType.type)) transform(it) else it }
        }
    }
}