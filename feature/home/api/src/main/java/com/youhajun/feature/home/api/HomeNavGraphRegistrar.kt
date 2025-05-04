package com.youhajun.feature.home.api

import androidx.navigation.NavGraphBuilder

interface HomeNavGraphRegistrar {
    fun register(
        builder: NavGraphBuilder,
        onNavigateToCall: (roomCode: String) -> Unit
    )
}