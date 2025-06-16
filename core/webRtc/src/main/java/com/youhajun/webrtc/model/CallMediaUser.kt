package com.youhajun.webrtc.model

sealed interface CallMediaUser {
    val key get() = userId + mediaContentType
    val userId: String
    val mediaContentType: MediaContentType
    val videoStream: CallVideoStream
    val audioStream: CallAudioStream
}

data class LocalMediaUser(
    override val userId: String,
    override val videoStream: LocalVideoStream,
    override val audioStream: LocalAudioStream,
    override val mediaContentType: MediaContentType,
) : CallMediaUser

data class RemoteMediaUser(
    override val userId: String,
    override val videoStream: RemoteVideoStream,
    override val audioStream: RemoteAudioStream,
    override val mediaContentType: MediaContentType
) : CallMediaUser