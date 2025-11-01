package com.youhajun.webrtc.video

import com.youhajun.webrtc.model.stream.CallMediaKey
import com.youhajun.webrtc.model.stream.CallVideoStream
import com.youhajun.webrtc.model.stream.LocalVideoStream
import com.youhajun.webrtc.model.local.MediaContentType
import com.youhajun.webrtc.model.stream.RemoteVideoStream
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

    override fun remove(userId: String, mediaContentType: MediaContentType) {
        val key = CallMediaKey.createKey(userId, mediaContentType.type)
        _videoStreamsFlow.update { list ->
            list.onEach { if (it.key == key) it.videoTrack?.dispose() }.filterNot { it.key == key }
        }
    }

    override fun updateDefaultLocal(userId: String, transform: (LocalVideoStream) -> LocalVideoStream) {
        val defaultKey = CallMediaKey.createKey(userId, MediaContentType.DEFAULT.type)
        _videoStreamsFlow.update { list ->
            list.map { if (it.key == defaultKey) transform(it as LocalVideoStream) else it }
        }
    }

    override fun updateDefaultRemote(userId: String, transform: (RemoteVideoStream) -> RemoteVideoStream) {
        val defaultKey = CallMediaKey.createKey(userId, MediaContentType.DEFAULT.type)
        _videoStreamsFlow.update { list ->
            list.map { if (it.key == defaultKey) transform(it as RemoteVideoStream) else it }
        }
    }
}