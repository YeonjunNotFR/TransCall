package com.youhajun.webrtc.model

data class SubscriberFeedRequest(
    val feedId: Long,
    val mid: String? = null,
    val crossrefid: String? = null
)