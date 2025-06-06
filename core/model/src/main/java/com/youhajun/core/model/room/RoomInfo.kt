package com.youhajun.core.model.room

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class RoomInfo(
    val code: String,
    val name: String = "",
    val hostId: String = "",
    val participants: ImmutableList<Participant> = persistentListOf(),
    val createdAt: Long = 0,
    val roomType: RoomType = RoomType.CODE_JOIN
)