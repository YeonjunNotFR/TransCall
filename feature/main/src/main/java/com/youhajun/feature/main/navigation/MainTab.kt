package com.youhajun.feature.main.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import com.youhajun.core.route.TransCallRoute
import com.youhajun.core.design.R
import com.youhajun.feature.history.api.HistoryNavRoute
import com.youhajun.feature.home.api.HomeNavRoute
import com.youhajun.room.api.RoomNavRoute

enum class MainTab(
    val route: TransCallRoute,
    val order: Int,
    val isCenter: Boolean,
    @param:DrawableRes val iconResId: Int,
    @param:StringRes val stringResId: Int,
) {
    History(HistoryNavRoute.HistoryList, 0, false, R.drawable.ic_history, R.string.bottom_bar_history),
    Home(HomeNavRoute.Home, 1, true, R.drawable.ic_home, R.string.bottom_bar_home),
    Room(RoomNavRoute.RoomList, 2, false, R.drawable.ic_find_room, R.string.bottom_bar_room);

    companion object {
        fun tabList(): List<MainTab> = entries.sortedBy { it.order }

        fun findByHierarchy(hierarchy: Sequence<NavDestination>): MainTab? =
            entries.firstOrNull { entry ->
                hierarchy.any { it.hasRoute(entry.route::class) }
            }
    }
}