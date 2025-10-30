package com.youhajun.webrtc.model.stream

object CallMediaKey {
    private const val KEY_SEPARATOR = "_"

    fun createKey(userId: String, mediaContentType: String): String {
        return userId + KEY_SEPARATOR + mediaContentType
    }
}

sealed interface CallMediaUser {
    val key get() = CallMediaKey.createKey(userId, mediaContentType)
    val userId: String
    val mediaContentType: String
    val videoStream: CallVideoStream
    val audioStream: CallAudioStream
}

data class LocalMediaUser(
    override val userId: String,
    override val videoStream: LocalVideoStream,
    override val audioStream: LocalAudioStream,
    override val mediaContentType: String,
) : CallMediaUser

data class RemoteMediaUser(
    override val userId: String,
    override val videoStream: RemoteVideoStream,
    override val audioStream: RemoteAudioStream,
    override val mediaContentType: String
) : CallMediaUser