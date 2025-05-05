package com.youhajun.feature.main.navigation

import androidx.annotation.DrawableRes
import com.youhajun.core.model.navigation.TransCallRoute
import com.youhajun.core.ui.R
import com.youhajun.feature.home.api.HomeNavRoute

enum class MainTab(
    val route: TransCallRoute,
    val order: Int,
    val isStartDestination: Boolean,
    @DrawableRes val iconResId: Int
) {
    HOME(HomeNavRoute.Home, 0, true, R.drawable.ic_home),
    CALL(HomeNavRoute.Home, 1, false, R.drawable.ic_home),
    Wait(HomeNavRoute.Home, 2, false, R.drawable.ic_home);

    companion object {
        fun tabList(): List<MainTab> = entries.sortedBy { it.order }

        fun startDestination(): Any =
            entries.first { it.isStartDestination }.route

        fun find(route: TransCallRoute): MainTab? =
            entries.firstOrNull { it.route == route }

        operator fun contains(route: TransCallRoute): Boolean =
            entries.any { it.route == route }
    }
}