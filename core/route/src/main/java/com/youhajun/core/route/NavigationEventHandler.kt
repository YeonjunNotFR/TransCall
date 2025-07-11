package com.youhajun.core.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

class NavigationEventHandler(
    private val navController: NavHostController
) {
    fun handleNavigationEvent(event: NavigationEvent) {
        when (event) {

            is NavigationEvent.Navigate -> {
                navController.navigate(event.route) {
                    launchSingleTop = event.launchSingleTop
                }
            }

            is NavigationEvent.NavigateAndSave -> {
                navController.navigate(event.route) {
                    popUpTo(event.popUpRoute) {
                        saveState = true
                    }
                    restoreState = true
                    launchSingleTop = event.launchSingleTop
                }
            }

            is NavigationEvent.NavigateAndClear -> {
                navController.navigate(event.route) {
                    val popUpRoute = event.popUpRoute ?: navController.currentNestedGraphStartId()
                    popUpTo(0) {
                        inclusive = true
                    }
                    launchSingleTop = event.launchSingleTop
                }
            }

            is NavigationEvent.NavigateBottomBar -> {
                navController.navigate(event.route) {
                    popUpTo(navController.currentNestedGraphStartId()) {
                        saveState = true
                    }
                    restoreState = true
                    launchSingleTop = event.launchSingleTop
                }
            }

            NavigationEvent.NavigateBack -> {
                navController.popBackStack()
            }

            is NavigationEvent.NavigateWeb -> {

            }
        }
    }

    private fun NavController.currentNestedGraphStartId(): Int {
        val currentDestination = currentBackStackEntry?.destination
        val parentGraph = currentDestination?.parent ?: graph
        return parentGraph.findStartDestination().id
    }
}

@Composable
fun rememberNavigationEventHandler(
    navController: NavHostController = rememberNavController(),
): NavigationEventHandler = remember(navController) {
    NavigationEventHandler(navController)
}