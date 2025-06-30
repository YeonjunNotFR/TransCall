package com.youhajun.feature.call.impl.calling

import com.youhajun.core.route.NavigationEvent

sealed interface CallingSideEffect {

    data class Navigation(val navigationEvent: NavigationEvent) : CallingSideEffect
}