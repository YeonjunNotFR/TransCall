package com.youhajun.core.model.calling

data class CallingMessage(
    val type: CallingMessageType,
    val from: String,
    val timestamp: Long,
)