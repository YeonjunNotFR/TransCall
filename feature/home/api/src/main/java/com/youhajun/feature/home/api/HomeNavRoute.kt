package com.youhajun.feature.home.api

import com.youhajun.core.model.navigation.TransCallRoute
import kotlinx.serialization.Serializable

sealed interface HomeNavRoute : TransCallRoute {
    @Serializable
    data object Home : HomeNavRoute
}