package com.youhajun.feature.history.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.youhajun.core.route.NavigationEvent
import com.youhajun.feature.history.api.HistoryNavRoute
import com.youhajun.feature.history.impl.HistoryRoute

fun NavGraphBuilder.historyNavGraph(
    onNavigate: (NavigationEvent) -> Unit,
) {
    composable<HistoryNavRoute.HistoryList> {
        HistoryRoute(onNavigate = onNavigate)
    }
}