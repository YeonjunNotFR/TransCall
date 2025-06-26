package com.youhajun.feature.home.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.youhajun.core.route.NavigationEvent
import com.youhajun.feature.home.api.HomeNavRoute
import com.youhajun.feature.home.impl.HomeRoute

fun NavGraphBuilder.homeNavGraph(
    onNavigate: (NavigationEvent) -> Unit,
) {
    composable<HomeNavRoute.Home> {
        HomeRoute(onNavigate = onNavigate)
    }
}