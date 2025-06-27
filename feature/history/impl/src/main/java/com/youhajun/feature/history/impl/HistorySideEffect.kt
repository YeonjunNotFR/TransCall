package com.youhajun.feature.history.impl

import com.youhajun.core.route.NavigationEvent

sealed class HistorySideEffect {

    data class Navigation(val navigationEvent: NavigationEvent) : HistorySideEffect()
}