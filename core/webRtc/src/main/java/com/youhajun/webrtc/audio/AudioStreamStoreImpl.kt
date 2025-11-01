package com.youhajun.webrtc.audio

import com.youhajun.webrtc.model.stream.CallAudioStream
import com.youhajun.webrtc.model.stream.CallMediaKey
import com.youhajun.webrtc.model.stream.LocalAudioStream
import com.youhajun.webrtc.model.local.MediaContentType
import com.youhajun.webrtc.model.stream.RemoteAudioStream
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

    override fun remove(userId: String, mediaContentType: MediaContentType) {
        val key = CallMediaKey.createKey(userId, mediaContentType.type)
        _audioStreamsFlow.update { list ->
            list.onEach { if (it.key == key) it.audioTrack?.dispose() }.filterNot { it.key == key }
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
        val key = CallMediaKey.createKey(userId, mediaContentType.type)
        _audioStreamsFlow.update { list ->
            list.map { if (it.key == key) transform(it) else it }
        }
    }

    override fun updateDefaultLocal(userId: String, transform: (LocalAudioStream) -> LocalAudioStream) {
        val defaultKey = CallMediaKey.createKey(userId, MediaContentType.DEFAULT.type)
        _audioStreamsFlow.update { list ->
            list.map { if (it.key == defaultKey) transform(it as LocalAudioStream) else it }
        }
    }

    override fun updateDefaultRemote(userId: String, transform: (RemoteAudioStream) -> RemoteAudioStream) {
        val defaultKey = CallMediaKey.createKey(userId, MediaContentType.DEFAULT.type)
        _audioStreamsFlow.update { list ->
            list.map { if (it.key == defaultKey) transform(it as RemoteAudioStream) else it }
        }
    }
}