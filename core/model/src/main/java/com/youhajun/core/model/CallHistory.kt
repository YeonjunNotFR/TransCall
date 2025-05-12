package com.youhajun.core.model

data class CallHistory(
    val callId: String,
    val partner: Participant? = null,
    val startedAtEpochSeconds: Long = 0,
    val durationSeconds: Int = 0,
    val isFavorite: Boolean = false,
)