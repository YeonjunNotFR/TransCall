package com.youhajun.webrtc.model.stream

import androidx.compose.runtime.Stable
import org.webrtc.VideoTrack

@Stable
sealed interface CallVideoStream {
    val key get() = CallMediaKey.createKey(userId, mediaContentType)
    val userId: String
    val mediaContentType: String
    val videoTrack: VideoTrack?
    val isVideoEnable: Boolean
}

@Stable
data class LocalVideoStream(
    override val userId: String,
    override val mediaContentType: String,
    override val videoTrack: VideoTrack?,
    override val isVideoEnable: Boolean,
    val isFrontCamera: Boolean,
) : CallVideoStream

@Stable
data class RemoteVideoStream(
    override val userId: String,
    override val mediaContentType: String,
    override val videoTrack: VideoTrack?,
    override val isVideoEnable: Boolean,
) : CallVideoStream