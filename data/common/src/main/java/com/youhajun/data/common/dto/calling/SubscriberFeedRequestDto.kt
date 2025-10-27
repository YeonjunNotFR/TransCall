package com.youhajun.data.common.dto.calling

import com.youhajun.core.model.calling.SubscriberFeedRequest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubscriberFeedRequestDto(
    @SerialName("feedId")
    val feedId: Long,
    @SerialName("mid")
    val mid: String?,
    @SerialName("crossrefid")
    val crossrefid: String?
)

fun SubscriberFeedRequest.toDto() = SubscriberFeedRequestDto(
    feedId = feedId,
    mid = mid,
    crossrefid = crossrefid
)