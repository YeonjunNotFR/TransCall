package com.youhajun.core.model.room

data class CreateRoomRequest(
    val title: String,
    val maxParticipantCount: Int,
    val visibility: RoomVisibility,
    val tags: Set<String>,
)