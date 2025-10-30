package com.youhajun.webrtc.model.signaling

data class VideoRoomHandleInfo(
    val defaultPublisherHandleId: Long,
    val screenSharePublisherHandleId: Long,
    val subscriberHandleId: Long
)