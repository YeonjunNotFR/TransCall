package com.youhajun.core.model

data class RoomInfo(
    val code: String,
    val name: String,
    val hostId: String,
    val participants: List<Participant>,
    val createdAt: Long
)