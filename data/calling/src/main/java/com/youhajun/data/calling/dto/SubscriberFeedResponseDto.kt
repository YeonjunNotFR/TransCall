package com.youhajun.data.calling.dto

import com.youhajun.core.model.calling.SubscriberFeedResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SubscriberFeedResponseDto(
    @SerialName("type")
    val type: String,
    @SerialName("mid")
    val mid: String,
    @SerialName("feedId")
    val feedId: Long,
    @SerialName("feedMid")
    val feedMid: String,
    @SerialName("feedDisplay")
    val feedDisplay: String?,
    @SerialName("feedDescription")
    val feedDescription: String?,
) {
    fun toModel() = SubscriberFeedResponse(
        type = type,
        mid = mid,
        feedId = feedId,
        feedMid = feedMid,
        feedDisplay = feedDisplay ?: "",
        feedDescription = feedDescription ?: ""
    )
}
