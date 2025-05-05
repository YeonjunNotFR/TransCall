package com.youhajun.feature.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.navigation.toRoute
import com.youhajun.core.model.navigation.TransCallRoute
import com.youhajun.feature.home.api.HomeNavRoute

internal class MainNavigator(
    val navController: NavHostController,
) {

    private val currentBackStackEntry: NavBackStackEntry?
        @Composable get() = navController.currentBackStackEntryAsState().value

    val currentTab: MainTab?
        @Composable get() = currentBackStackEntry
            ?.toRoute<TransCallRoute>()
            ?.let(MainTab::find)

    val startDestination = MainTab.startDestination()

    fun navigate(
        route: TransCallRoute,
        navOptions: NavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    ) {
        navController.navigate(route, navOptions)
    }

    fun popBackStack() {
        navController.popBackStack()
    }

    fun popBackStackIfNotStart() {
        navController.currentBackStackEntry?.toRoute<HomeNavRoute.Home>()?.let {
            popBackStack()
        }
    }

    @Composable
    fun shouldShowBottomBar(): Boolean {
        val currentRoute = currentBackStackEntry?.toRoute<TransCallRoute>() ?: return false
        return currentRoute in MainTab
    }
}

@Composable
internal fun rememberMainNavigator(
    navController: NavHostController = rememberNavController(),
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}
