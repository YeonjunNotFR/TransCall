package com.youhajun.core.model

data class CallHistory(
    val callId: String,
    val partnerName: String = "",
    val partnerImageUrl: String? = null,
    val startedAtEpochSeconds: Long = 0,
    val durationSeconds: Int = 0,
    val isFavorite: Boolean = false,
)