package com.youhajun.feature.main.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.youhajun.feature.call.api.CallNavGraphRegistrar
import com.youhajun.feature.call.api.CallNavRoute
import com.youhajun.feature.home.api.HomeNavGraphRegistrar

@Composable
internal fun MainNavHost(
    padding: PaddingValues,
    navigator: MainNavigator,
    homeNavGraphRegistrar: HomeNavGraphRegistrar,
    callNavGraphRegistrar: CallNavGraphRegistrar
) {
    NavHost(
        modifier = Modifier.padding(padding),
        navController = navigator.navController,
        startDestination = navigator.startDestination
    ) {
        homeNavGraphRegistrar.register(
            this,
            onNavigateToCall = { roomCode ->
                navigator.navigate(CallNavRoute.Calling(roomCode))
            }
        )

        callNavGraphRegistrar.register(this)
    }
}