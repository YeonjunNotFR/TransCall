package com.youhajun.feature.splash.api

import com.youhajun.core.route.TransCallRoute
import kotlinx.serialization.Serializable

sealed interface SplashNavRoute : TransCallRoute {
    @Serializable
    data object Splash : SplashNavRoute
}