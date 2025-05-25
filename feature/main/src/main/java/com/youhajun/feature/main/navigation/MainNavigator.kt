package com.youhajun.feature.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.youhajun.core.model.navigation.TransCallRoute

internal class MainNavigator(
    val navController: NavHostController,
) {

    private val currentBackStackEntry: NavBackStackEntry?
        @Composable get() = navController.currentBackStackEntryAsState().value

    val currentTab: MainTab?
        @Composable get() = currentBackStackEntry
            ?.destination
            ?.hierarchy
            ?.let(MainTab::findByHierarchy)

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

    @Composable
    fun shouldShowBottomBar(): Boolean {
        return currentTab != null
    }
}

@Composable
internal fun rememberMainNavigator(
    navController: NavHostController = rememberNavController(),
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}
