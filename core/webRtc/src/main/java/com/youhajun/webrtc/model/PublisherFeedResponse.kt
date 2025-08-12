package com.youhajun.webrtc.model

data class PublisherFeedResponse(
    val feedId: Long,
    val display: String,
    val streams: List<PublisherFeedResponseStream>
) {
    fun toSubscriberFeedRequest() = SubscriberFeedRequest(
        feedId = feedId,
    )
}

data class PublisherFeedResponseStream(
    val type: String,
    val mid: String,
)