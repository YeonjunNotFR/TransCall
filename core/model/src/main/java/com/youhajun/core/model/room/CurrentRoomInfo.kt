package com.youhajun.core.model.room

import kotlinx.collections.immutable.ImmutableList

data class CurrentRoomInfo(
    val roomInfo: RoomInfo,
    val currentParticipants: ImmutableList<Participant>,
)