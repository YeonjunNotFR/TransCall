package com.youhajun.feature.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

internal class MainNavigator(
    private val navController: NavHostController,
) {

    private val currentBackStackEntry: NavBackStackEntry?
        @Composable get() = navController.currentBackStackEntryAsState().value

    val currentTab: MainTab?
        @Composable get() = currentBackStackEntry
            ?.destination
            ?.hierarchy
            ?.let(MainTab::findByHierarchy)

    @Composable
    fun shouldShowBottomBar(): Boolean {
        return currentTab != null
    }
}

@Composable
internal fun rememberMainNavigator(
    navController: NavHostController,
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}
