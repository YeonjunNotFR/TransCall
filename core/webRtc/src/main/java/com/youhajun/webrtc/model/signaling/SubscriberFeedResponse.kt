package com.youhajun.webrtc.model.signaling

import com.youhajun.webrtc.model.local.MediaContentType
import com.youhajun.webrtc.model.local.TrackType

data class SubscriberFeedResponse(
    val type: String,
    val mid: String,
    val feedId: Long,
    val feedMid: String,
    val feedDisplay: String,
    val feedDescription: String,
) {
    internal fun toSubscriberMidMapper() = SubscriberMidMapper(
        userId = feedDisplay,
        mediaContentType = MediaContentType.Companion.fromType(feedDescription),
        trackType = TrackType.Companion.fromString(type),
        feedId = feedId
    )
}

internal fun List<SubscriberFeedResponse>.toMidMap(): Map<String, SubscriberMidMapper> =
    associateBy(SubscriberFeedResponse::mid) { it.toSubscriberMidMapper() }