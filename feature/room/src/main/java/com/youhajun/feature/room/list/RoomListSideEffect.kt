package com.youhajun.feature.room.list

import com.youhajun.core.route.NavigationEvent

sealed class RoomListSideEffect {
    data class GoToCall(val roomId: String) : RoomListSideEffect()
    data class Navigation(val navigationEvent: NavigationEvent) : RoomListSideEffect()
    data object PermissionCheck : RoomListSideEffect()
}