package com.youhajun.feature.call.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.youhajun.core.route.NavigationEvent
import com.youhajun.feature.call.api.CallNavRoute
import com.youhajun.feature.call.calling.CallingRoute

fun NavGraphBuilder.callNavGraph(
    onNavigate: (NavigationEvent) -> Unit,
) {
    composable<CallNavRoute.Calling> {
        CallingRoute(onNavigate = onNavigate)
    }
}