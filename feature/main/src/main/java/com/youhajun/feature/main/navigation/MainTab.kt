package com.youhajun.feature.main.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import com.youhajun.core.route.TransCallRoute
import com.youhajun.core.design.R
import com.youhajun.feature.auth.api.AuthNavRoute
import com.youhajun.feature.history.api.HistoryNavRoute
import com.youhajun.feature.home.api.HomeNavRoute

enum class MainTab(
    val route: TransCallRoute,
    val order: Int,
    val isCenter: Boolean,
    @DrawableRes val iconResId: Int,
    @StringRes val stringResId: Int,
) {
    History(HistoryNavRoute.HistoryList, 0, false, R.drawable.ic_home, R.string.bottom_bar_history),
    Home(HomeNavRoute.Home, 1, true, R.drawable.ic_home, R.string.bottom_bar_home),
    MyPage(AuthNavRoute.Login, 2, false, R.drawable.ic_home, R.string.bottom_bar_profile);

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