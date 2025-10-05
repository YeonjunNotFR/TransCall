package com.youhajun.data.calling.dto

import com.youhajun.core.model.calling.PublisherFeedResponse
import com.youhajun.core.model.calling.PublisherFeedResponseStream
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PublisherFeedResponseDto(
    @SerialName("feedId")
    val feedId: Long,
    @SerialName("display")
    val display: String,
    @SerialName("streams")
    val streams: List<PublisherFeedResponseStreamDto>
) {
    fun toModel() = PublisherFeedResponse(
        feedId = feedId,
        display = display,
        streams = streams.map { it.toModel() }
    )
}

@Serializable
internal data class PublisherFeedResponseStreamDto(
    @SerialName("type")
    val type: String,
    @SerialName("mid")
    val mid: String,
) {
    fun toModel() = PublisherFeedResponseStream(
        type = type,
        mid = mid
    )
}