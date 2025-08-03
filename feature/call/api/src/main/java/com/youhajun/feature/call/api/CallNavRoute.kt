package com.youhajun.feature.call.api

import com.youhajun.core.route.TransCallRoute
import kotlinx.serialization.Serializable

sealed interface CallNavRoute: TransCallRoute {

    @Serializable
    data class Calling(val roomId: String) : CallNavRoute

    @Serializable
    data class Summary(val roomId: String) : CallNavRoute
}