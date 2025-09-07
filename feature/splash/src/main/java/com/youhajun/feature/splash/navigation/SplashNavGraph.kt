package com.youhajun.feature.splash.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.youhajun.feature.splash.SplashRoute
import com.youhajun.feature.splash.api.SplashNavRoute

fun NavGraphBuilder.splashNavGraph() {
    composable<SplashNavRoute.Splash> {
        SplashRoute()
    }
}