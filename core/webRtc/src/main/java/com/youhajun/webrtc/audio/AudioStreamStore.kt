package com.youhajun.webrtc.audio

import com.youhajun.webrtc.model.stream.CallAudioStream
import com.youhajun.webrtc.model.stream.LocalAudioStream
import com.youhajun.webrtc.model.local.MediaContentType
import com.youhajun.webrtc.model.stream.RemoteAudioStream
import kotlinx.coroutines.flow.StateFlow

internal interface AudioStreamStore {
    val audioStreamsFlow: StateFlow<List<CallAudioStream>>

    fun upsert(stream: CallAudioStream)
    fun remove(userId: String, mediaContentType: MediaContentType)
    fun update(userId: String, mediaContentType: MediaContentType, transform: (CallAudioStream) -> CallAudioStream)
    fun updateAll(transform: (CallAudioStream) -> CallAudioStream)

    fun updateDefaultLocal(userId: String, transform: (LocalAudioStream) -> LocalAudioStream)
    fun updateDefaultRemote(userId: String, transform: (RemoteAudioStream) -> RemoteAudioStream)
}