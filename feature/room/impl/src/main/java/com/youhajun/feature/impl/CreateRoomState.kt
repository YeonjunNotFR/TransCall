package com.youhajun.feature.impl

import com.youhajun.core.model.room.RoomVisibility
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class CreateRoomState(
    val maxParticipantCount: Int,
    val selectedMaxParticipantCount: Int = 2,
    val selectedRoomVisibility: RoomVisibility = RoomVisibility.PUBLIC,
    val tags: ImmutableList<String> = persistentListOf()
)