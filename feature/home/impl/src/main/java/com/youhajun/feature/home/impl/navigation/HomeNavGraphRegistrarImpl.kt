package com.youhajun.feature.home.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.youhajun.feature.home.api.HomeNavGraphRegistrar
import com.youhajun.feature.home.api.HomeNavRoute
import com.youhajun.feature.home.impl.HomeRoute
import com.youhajun.feature.home.impl.HomeSideEffect
import javax.inject.Inject

internal class HomeNavGraphRegistrarImpl @Inject constructor() : HomeNavGraphRegistrar {

    override fun register(
        builder: NavGraphBuilder,
        onNavigateToCallWaiting: (roomCode: String) -> Unit,
        onNavigateToHistory: () -> Unit
    ) {
        builder.composable<HomeNavRoute.Home> {
            HomeRoute {
                when (it) {
                    is HomeSideEffect.Navigation.GoToCallHistory -> onNavigateToHistory()
                    is HomeSideEffect.Navigation.GoToCallWaiting -> onNavigateToCallWaiting(it.roomCode)
                }
            }
        }
    }
}