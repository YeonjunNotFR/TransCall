package com.youhajun.feature.room.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.youhajun.core.route.DeepLinkEntry
import com.youhajun.core.route.NavigationEvent
import com.youhajun.feature.room.create.CreateRoomRoute
import com.youhajun.feature.room.list.RoomListRoute
import com.youhajun.room.api.RoomNavRoute

fun NavGraphBuilder.roomNavGraph(
    onNavigate: (NavigationEvent) -> Unit,
) {
    composable<RoomNavRoute.CreateRoom>(
        deepLinks = listOf(navDeepLink { uriPattern = DeepLinkEntry.CreateRoom.pattern })
    ) {
        CreateRoomRoute(onNavigate = onNavigate)
    }

    composable<RoomNavRoute.RoomList>(
        deepLinks = listOf(navDeepLink { uriPattern = DeepLinkEntry.RoomList.pattern })
    ) {
        RoomListRoute(onNavigate = onNavigate)
    }
}