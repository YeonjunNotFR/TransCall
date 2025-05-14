package com.youhajun.feature.history.api

import com.youhajun.core.model.navigation.TransCallRoute
import kotlinx.serialization.Serializable

sealed interface HistoryNavRoute : TransCallRoute {
    @Serializable
    data object HistoryList : HistoryNavRoute
}