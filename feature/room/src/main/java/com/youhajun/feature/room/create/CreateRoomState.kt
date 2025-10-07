package com.youhajun.feature.room.create

import com.youhajun.core.model.room.RoomVisibility
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf

data class CreateRoomState(
    val maxParticipantCount: Int,
    val selectedMaxParticipantCount: Int = 2,
    val selectedRoomVisibility: RoomVisibility = RoomVisibility.PUBLIC,
    val titleHint: String = "",
    val tags: ImmutableSet<String> = persistentSetOf(),
    val maxTagCount: Int,
)