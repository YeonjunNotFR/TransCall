package com.youhajun.feature.impl

import com.youhajun.core.route.NavigationEvent

sealed class CreateRoomSideEffect {
    data object PermissionCheck : CreateRoomSideEffect()
    data class Navigation(val navigationEvent: NavigationEvent) : CreateRoomSideEffect()
    data class GoToCall(val roomId: String) : CreateRoomSideEffect()
}