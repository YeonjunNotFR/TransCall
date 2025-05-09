package com.youhajun.feature.call.api

import com.youhajun.core.model.navigation.TransCallRoute
import kotlinx.serialization.Serializable

sealed interface CallNavRoute: TransCallRoute {

    @Serializable
    data class Calling(val roomCode: String) : CallNavRoute

    @Serializable
    data class Waiting(val roomCode: String) : CallNavRoute
}