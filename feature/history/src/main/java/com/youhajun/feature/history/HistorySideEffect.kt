package com.youhajun.feature.history

import com.youhajun.core.route.NavigationEvent

sealed class HistorySideEffect {

    data class Navigation(val navigationEvent: NavigationEvent) : HistorySideEffect()
}