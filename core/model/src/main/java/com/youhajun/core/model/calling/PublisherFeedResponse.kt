package com.youhajun.core.model.calling

data class PublisherFeedResponse(
    val feedId: Long,
    val display: String,
    val streams: List<PublisherFeedResponseStream>
)

data class PublisherFeedResponseStream(
    val type: String,
    val mid: String,
)