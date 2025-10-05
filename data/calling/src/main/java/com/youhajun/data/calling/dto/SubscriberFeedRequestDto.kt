package com.youhajun.data.calling.dto

import com.youhajun.core.model.calling.SubscriberFeedRequest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SubscriberFeedRequestDto(
    @SerialName("feedId")
    val feedId: Long,
    @SerialName("mid")
    val mid: String?,
    @SerialName("crossrefid")
    val crossrefid: String?
)

internal fun SubscriberFeedRequest.toDto() = SubscriberFeedRequestDto(
    feedId = feedId,
    mid = mid,
    crossrefid = crossrefid
)