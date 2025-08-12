package com.youhajun.webrtc.model

import org.webrtc.VideoTrack

sealed interface CallVideoStream {
    val key get() = CallMediaKey.createKey(userId, mediaContentType)
    val userId: String
    val mediaContentType: String
    val videoTrack: VideoTrack?
    val isVideoEnable: Boolean
}

data class LocalVideoStream(
    override val userId: String,
    override val mediaContentType: String,
    override val videoTrack: VideoTrack?,
    override val isVideoEnable: Boolean,
    val isFrontCamera: Boolean,
) : CallVideoStream

data class RemoteVideoStream(
    override val userId: String,
    override val mediaContentType: String,
    override val videoTrack: VideoTrack?,
    override val isVideoEnable: Boolean,
) : CallVideoStream