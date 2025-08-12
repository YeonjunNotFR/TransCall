package com.youhajun.webrtc.model

internal data class SubscriberMidMapper(
    val userId: String,
    val mediaContentType: MediaContentType,
    val trackType: TrackType
)