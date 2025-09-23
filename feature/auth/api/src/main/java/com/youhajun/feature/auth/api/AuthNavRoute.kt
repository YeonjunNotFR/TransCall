package com.youhajun.feature.auth.api

import com.youhajun.core.route.TransCallRoute
import kotlinx.serialization.Serializable

sealed interface AuthNavRoute : TransCallRoute {
    @Serializable
    data object Login : AuthNavRoute
}