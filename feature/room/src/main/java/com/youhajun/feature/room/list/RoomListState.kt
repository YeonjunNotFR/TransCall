package com.youhajun.feature.room.list

import com.youhajun.core.model.SortDirection
import com.youhajun.core.model.room.CurrentRoomInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class RoomListState(
    val currentRoomList: ImmutableList<CurrentRoomInfo> = persistentListOf(),
    val participantSort: SortDirection = SortDirection.DESC,
    val createdAtSort: SortDirection = SortDirection.DESC,
)