package com.youhajun.feature.main.navigation

import com.youhajun.core.route.TransCallRoute
import kotlinx.serialization.Serializable

sealed interface MainNavRoute : TransCallRoute {
    @Serializable
    data object AuthNestedGraph : MainNavRoute

    @Serializable
    data object MainNestedGraph : MainNavRoute
}