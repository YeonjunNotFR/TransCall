package com.youhajun.feature.main.navigation

import androidx.annotation.DrawableRes
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import com.youhajun.core.model.navigation.TransCallRoute
import com.youhajun.core.ui.R
import com.youhajun.feature.history.api.HistoryNavRoute
import com.youhajun.feature.home.api.HomeNavRoute

enum class MainTab(
    val route: TransCallRoute,
    val order: Int,
    val isStartDestination: Boolean,
    @DrawableRes val iconResId: Int
) {
    Home(HomeNavRoute.Home, 0, true, R.drawable.ic_home),
    History(HistoryNavRoute.HistoryList, 1, false, R.drawable.ic_home),
    MyPage(HomeNavRoute.Home, 2, false, R.drawable.ic_home);

    companion object {
        fun tabList(): List<MainTab> = entries.sortedBy { it.order }

        fun startDestination(): Any =
            entries.first { it.isStartDestination }.route

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