package com.youhajun.feature.history.api

import com.youhajun.core.route.TransCallRoute
import kotlinx.serialization.Serializable

sealed interface HistoryNavRoute : TransCallRoute {

    @Serializable
    data class HistoryDetail(val historyId: String) : HistoryNavRoute

    @Serializable
    data object HistoryList : HistoryNavRoute
}