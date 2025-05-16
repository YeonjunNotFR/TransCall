package com.youhajun.feature.main.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.youhajun.feature.call.api.CallNavGraphRegistrar
import com.youhajun.feature.call.api.CallNavRoute
import com.youhajun.feature.history.api.HistoryNavGraphRegistrar
import com.youhajun.feature.history.api.HistoryNavRoute
import com.youhajun.feature.home.api.HomeNavGraphRegistrar

@Composable
internal fun MainNavHost(
    padding: PaddingValues,
    navigator: MainNavigator,
    homeNavGraphRegistrar: HomeNavGraphRegistrar,
    callNavGraphRegistrar: CallNavGraphRegistrar,
    historyNavGraphRegistrar: HistoryNavGraphRegistrar,
) {
    NavHost(
        modifier = Modifier.padding(padding),
        navController = navigator.navController,
        startDestination = navigator.startDestination
    ) {
        homeNavGraphRegistrar.register(
            this,
            onNavigateToCallWaiting = { roomCode ->
                navigator.navigate(CallNavRoute.Waiting(roomCode))
            },
            onNavigateToHistory = {
                navigator.navigate(HistoryNavRoute.HistoryList)
            }
        )

        callNavGraphRegistrar.register(this)

        historyNavGraphRegistrar.register(this)
    }
}