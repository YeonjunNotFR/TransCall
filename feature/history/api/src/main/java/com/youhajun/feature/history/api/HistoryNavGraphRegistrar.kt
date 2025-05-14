package com.youhajun.feature.history.api

import androidx.navigation.NavGraphBuilder

interface HistoryNavGraphRegistrar {
    fun register(
        builder: NavGraphBuilder,
    )
}