package com.youhajun.feature.home.impl

import com.youhajun.core.route.NavigationEvent

sealed class HomeSideEffect {

    data class GoToCall(val roomCode: String) : HomeSideEffect()
    data class Navigation(val navigationEvent: NavigationEvent) : HomeSideEffect()
}