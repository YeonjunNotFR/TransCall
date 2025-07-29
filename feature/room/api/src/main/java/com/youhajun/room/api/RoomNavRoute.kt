package com.youhajun.room.api

import com.youhajun.core.route.TransCallRoute
import kotlinx.serialization.Serializable

sealed interface RoomNavRoute : TransCallRoute {

    @Serializable
    data object CreateRoom : RoomNavRoute
}