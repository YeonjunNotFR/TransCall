package com.youhajun.feature.auth.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.youhajun.core.route.NavigationEvent
import com.youhajun.feature.auth.api.AuthNavRoute
import com.youhajun.feature.auth.impl.login.LoginRoute

fun NavGraphBuilder.loginNavGraph(
    onNavigate: (NavigationEvent) -> Unit,
) {
    composable<AuthNavRoute.Login> {
        LoginRoute(onNavigate = onNavigate)
    }
}