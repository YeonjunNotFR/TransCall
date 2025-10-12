package com.youhajun.feature.history.detail

import com.youhajun.core.route.NavigationEvent

sealed class HistoryDetailSideEffect {

    data class Navigation(val navigationEvent: NavigationEvent) : HistoryDetailSideEffect()
}