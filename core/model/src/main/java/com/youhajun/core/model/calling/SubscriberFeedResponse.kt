package com.youhajun.core.model.calling

data class SubscriberFeedResponse(
    val type: String,
    val mid: String,
    val feedId: Long,
    val feedMid: String,
    val feedDisplay: String,
    val feedDescription: String,
)
