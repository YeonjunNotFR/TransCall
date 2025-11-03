package com.youhajun.webrtc.model.signaling

data class PublisherFeedResponse(
    val feedId: Long,
    val display: String,
    val streams: List<PublisherFeedResponseStream>
) {
    fun toSubscriberFeedRequest(): List<SubscriberFeedRequest> = streams.map {
        SubscriberFeedRequest(
            feedId = feedId,
            mid = it.mid,
            crossrefid = null
        )
    }
}

data class PublisherFeedResponseStream(
    val type: String,
    val mid: String,
)