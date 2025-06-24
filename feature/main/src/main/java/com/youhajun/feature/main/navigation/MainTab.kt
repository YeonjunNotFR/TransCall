package com.youhajun.feature.main.navigation

import androidx.annotation.DrawableRes
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import com.youhajun.core.route.TransCallRoute
import com.youhajun.core.design.R
import com.youhajun.feature.history.api.HistoryNavRoute
import com.youhajun.feature.home.api.HomeNavRoute

enum class MainTab(
    val route: TransCallRoute,
    val order: Int,
    @DrawableRes val iconResId: Int
) {
    Home(HomeNavRoute.Home, 0, R.drawable.ic_home),
    History(HistoryNavRoute.HistoryList, 1, R.drawable.ic_home),
    MyPage(HomeNavRoute.Home, 2, R.drawable.ic_home);

    companion object {
        fun tabList(): List<MainTab> = entries.sortedBy { it.order }

        fun findByHierarchy(hierarchy: Sequence<NavDestination>): MainTab? =
            entries.firstOrNull { entry ->
                hierarchy.any { it.hasRoute(entry.route::class) }
            }

        fun find(route: TransCallRoute): MainTab? =
            entries.firstOrNull { it.route == route }

        operator fun contains(route: TransCallRoute): Boolean =
            entries.any { it.route == route }
    }
}