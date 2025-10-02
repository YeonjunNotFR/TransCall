package com.youhajun.core.model.room

import com.youhajun.core.model.SortDirection
import com.youhajun.core.model.pagination.CursorPageRequest

data class CurrentRoomListRequest(
    val participantSort: SortDirection,
    val createdAtSort: SortDirection,
    val cursorRequest: CursorPageRequest
)