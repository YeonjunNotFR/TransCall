package com.youhajun.webrtc.model

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
        mediaContentType = MediaContentType.fromType(feedDescription),
        trackType = TrackType.fromString(type)
    )
}

internal fun List<SubscriberFeedResponse>.toMidMap(): Map<String, SubscriberMidMapper> =
    associateBy(SubscriberFeedResponse::mid) { it.toSubscriberMidMapper() }