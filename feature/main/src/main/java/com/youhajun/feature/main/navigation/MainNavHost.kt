package com.youhajun.feature.main.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import com.youhajun.core.route.NavigationEvent
import com.youhajun.feature.auth.api.AuthNavRoute
import com.youhajun.feature.auth.impl.navigation.loginNavGraph
import com.youhajun.feature.history.impl.navigation.historyNavGraph
import com.youhajun.feature.home.api.HomeNavRoute
import com.youhajun.feature.home.impl.navigation.homeNavGraph
import com.youhajun.feature.impl.navigation.roomNavGraph
import com.youhajun.feature.splash.api.SplashNavRoute
import com.youhajun.feature.splash.impl.navigation.splashNavGraph

@Composable
internal fun MainNavHost(
    padding: PaddingValues,
    navController: NavHostController,
    onNavigationEvent: (NavigationEvent) -> Unit,
) {
    NavHost(
        modifier = Modifier.padding(padding),
        navController = navController,
        startDestination = SplashNavRoute.Splash
    ) {
        splashNavGraph()
        authNestedGraph(onNavigationEvent)
        mainNestedGraph(onNavigationEvent)
    }
}

private fun NavGraphBuilder.authNestedGraph(onNavigationEvent: (NavigationEvent) -> Unit) {
    navigation<MainNavRoute.AuthNestedGraph>(startDestination = AuthNavRoute.Login) {
        loginNavGraph(onNavigationEvent)
    }
}

private fun NavGraphBuilder.mainNestedGraph(onNavigationEvent: (NavigationEvent) -> Unit) {
    navigation<MainNavRoute.MainNestedGraph>(startDestination = HomeNavRoute.Home) {
        homeNavGraph(onNavigationEvent)
        historyNavGraph(onNavigationEvent)
        roomNavGraph(onNavigationEvent)
    }
}