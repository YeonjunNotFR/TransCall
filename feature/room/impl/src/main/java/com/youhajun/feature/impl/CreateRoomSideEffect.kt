package com.youhajun.feature.impl

import com.youhajun.core.route.NavigationEvent

sealed class CreateRoomSideEffect {

    data class Navigation(val navigationEvent: NavigationEvent) : CreateRoomSideEffect()
}