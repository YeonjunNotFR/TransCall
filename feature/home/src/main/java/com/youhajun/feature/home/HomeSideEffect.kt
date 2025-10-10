package com.youhajun.feature.home

import com.youhajun.core.route.NavigationEvent

sealed class HomeSideEffect {

    data class GoToCall(val roomId: String) : HomeSideEffect()
    data class Navigation(val navigationEvent: NavigationEvent) : HomeSideEffect()
    data object CallPermissionCheck : HomeSideEffect()
}