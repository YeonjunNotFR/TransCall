package com.youhajun.core.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

class NavigationEventHandler(
    private val navController: NavHostController
) {
    fun handleNavigationEvent(event: NavigationEvent) {
        when (event) {
            NavigationEvent.NavigateBack -> {
                navController.popBackStack()
            }

            is NavigationEvent.Navigate -> {
                navController.navigate(event.route) {
                    if (event.saveState) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                    }
                    launchSingleTop = event.launchSingleTop
                }
            }

            is NavigationEvent.NavigateAndClear -> {
                navController.navigate(event.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                    launchSingleTop = event.launchSingleTop
                }
            }

            is NavigationEvent.NavigateWeb -> {

            }
        }
    }
}

@Composable
fun rememberNavigationEventHandler(
    navController: NavHostController = rememberNavController(),
): NavigationEventHandler = remember(navController) {
    NavigationEventHandler(navController)
}