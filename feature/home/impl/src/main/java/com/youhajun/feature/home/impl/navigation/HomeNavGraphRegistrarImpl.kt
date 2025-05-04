package com.youhajun.feature.home.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.youhajun.feature.home.api.HomeNavGraphRegistrar
import com.youhajun.feature.home.api.HomeNavRoute
import com.youhajun.feature.home.impl.HomeScreen
import javax.inject.Inject

internal class HomeNavGraphRegistrarImpl @Inject constructor() : HomeNavGraphRegistrar {

    override fun register(builder: NavGraphBuilder, onNavigateToCall: (roomCode: String) -> Unit) {
        builder.composable<HomeNavRoute.Home> {
            HomeScreen()
        }
    }
}