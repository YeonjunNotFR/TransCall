package com.youhajun.feature.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.youhajun.core.route.NavigationEvent
import com.youhajun.feature.impl.CreateRoomRoute
import com.youhajun.room.api.RoomNavRoute

fun NavGraphBuilder.roomNavGraph(
    onNavigate: (NavigationEvent) -> Unit,
) {
    composable<RoomNavRoute.CreateRoom> {
        CreateRoomRoute(onNavigate = onNavigate)
    }
}