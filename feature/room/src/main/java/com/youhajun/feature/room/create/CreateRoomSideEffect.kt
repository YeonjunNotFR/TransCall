package com.youhajun.feature.room.create

import com.youhajun.core.route.NavigationEvent

sealed class CreateRoomSideEffect {
    data object PermissionCheck : CreateRoomSideEffect()
    data class Navigation(val navigationEvent: NavigationEvent) : CreateRoomSideEffect()
    data class GoToCall(val roomId: String) : CreateRoomSideEffect()
}