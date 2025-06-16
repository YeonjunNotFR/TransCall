package com.youhajun.webrtc.model

import org.webrtc.VideoTrack

sealed interface CallVideoStream {
    val key get() = userId + mediaContentType
    val userId: String
    val mediaContentType: MediaContentType
    val videoTrack: VideoTrack?
    val isVideoEnable: Boolean
}

data class LocalVideoStream(
    override val userId: String,
    override val mediaContentType: MediaContentType,
    override val videoTrack: VideoTrack?,
    override val isVideoEnable: Boolean,
    val isFrontCamera: Boolean,
) : CallVideoStream

data class RemoteVideoStream(
    override val userId: String,
    override val mediaContentType: MediaContentType,
    override val videoTrack: VideoTrack?,
    override val isVideoEnable: Boolean,
) : CallVideoStream