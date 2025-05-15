package com.youhajun.feature.history.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.youhajun.feature.history.api.HistoryNavGraphRegistrar
import com.youhajun.feature.history.api.HistoryNavRoute
import com.youhajun.feature.history.impl.HistoryRoute
import com.youhajun.feature.history.impl.HistorySideEffect
import javax.inject.Inject

internal class HistoryNavGraphRegistrarImpl @Inject constructor() : HistoryNavGraphRegistrar {

    override fun register(builder: NavGraphBuilder) {
        builder.composable<HistoryNavRoute.HistoryList> {
            HistoryRoute {
                when (it) {
                    is HistorySideEffect.Navigation.GoToCallWaiting -> TODO()
                }
            }
        }
    }
}