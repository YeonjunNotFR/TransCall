package com.youhajun.webrtc.model.signaling

import com.youhajun.webrtc.model.local.MediaContentType
import com.youhajun.webrtc.model.local.TrackType

internal data class SubscriberMidMapper(
    val userId: String,
    val mediaContentType: MediaContentType,
    val trackType: TrackType,
    val feedId: Long
)