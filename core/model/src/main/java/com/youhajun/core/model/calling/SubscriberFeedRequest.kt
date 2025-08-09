package com.youhajun.core.model.calling

data class SubscriberFeedRequest(
    val feedId: Long,
    val mid: String?,
    val crossrefid: String?
)