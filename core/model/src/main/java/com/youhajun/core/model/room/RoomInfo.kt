package com.youhajun.core.model.room

import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf

data class RoomInfo(
    val roomId: String,
    val roomCode: String = "",
    val title: String = "",
    val maxParticipantCount: Int = 0,
    val currentParticipantCount: Int = 0,
    val visibility: RoomVisibility = RoomVisibility.PUBLIC,
    val joinType: RoomJoinType = RoomJoinType.CODE_JOIN,
    val tags: ImmutableSet<String> = persistentSetOf(),
    val status: RoomStatus = RoomStatus.WAITING,
    val hostId: String = "",
    val createdAtToEpochTime: Long = 0
)