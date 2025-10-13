package com.youhajun.feature.history.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.youhajun.core.route.DeepLinkEntry
import com.youhajun.core.route.NavigationEvent
import com.youhajun.feature.history.HistoryRoute
import com.youhajun.feature.history.api.HistoryNavRoute
import com.youhajun.feature.history.detail.HistoryDetailRoute

fun NavGraphBuilder.historyNavGraph(
    onNavigate: (NavigationEvent) -> Unit,
) {
    composable<HistoryNavRoute.HistoryList>(
        deepLinks = listOf(navDeepLink { uriPattern = DeepLinkEntry.HistoryList.pattern })
    ) {
        HistoryRoute(onNavigate = onNavigate)
    }

    composable<HistoryNavRoute.HistoryDetail>(
        deepLinks = listOf(navDeepLink { uriPattern = DeepLinkEntry.HistoryDetail.PATTERN })
    ) {
        HistoryDetailRoute(onNavigate = onNavigate)
    }
}