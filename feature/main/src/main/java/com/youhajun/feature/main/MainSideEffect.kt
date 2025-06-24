package com.youhajun.feature.main

import com.youhajun.core.route.NavigationEvent

sealed class MainSideEffect {

    data class Navigation(val event: NavigationEvent) : MainSideEffect()
}