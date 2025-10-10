package com.youhajun.feature.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.youhajun.core.route.DeepLinkEntry
import com.youhajun.core.route.NavigationEvent
import com.youhajun.feature.home.HomeRoute
import com.youhajun.feature.home.api.HomeNavRoute

fun NavGraphBuilder.homeNavGraph(
    onNavigate: (NavigationEvent) -> Unit,
) {
    composable<HomeNavRoute.Home>(
        deepLinks = listOf(navDeepLink { uriPattern = DeepLinkEntry.Home.pattern })
    ) {
        HomeRoute(onNavigate = onNavigate)
    }
}