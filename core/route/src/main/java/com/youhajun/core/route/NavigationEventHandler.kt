package com.youhajun.core.route

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController

class NavigationEventHandler(
    private val navController: NavHostController
) {
    fun handleNavigationEvent(event: NavigationEvent) {
        when (event) {

            is NavigationEvent.Navigate -> {
                navController.go(event.route, singleTop = event.launchSingleTop)
            }

            is NavigationEvent.NavigateAndSave -> {
                navController.goAndSave(
                    route = event.route,
                    popUpToRoute = event.popUpRoute,
                    singleTop = event.launchSingleTop
                )
            }

            is NavigationEvent.NavigateAndClear -> {
                navController.goAndClear(
                    route = event.route,
                    popUpToRoute = event.popUpRoute,
                    singleTop = event.launchSingleTop
                )
            }

            is NavigationEvent.NavigateBottomBar -> {
                navController.goAndSave(
                    route = event.route,
                    popUpToId = navController.currentNestedGraphStartId(),
                    singleTop = event.launchSingleTop
                )
            }

            is NavigationEvent.NavigateDeepLink -> {
                navController.goDeepLink(
                    uri = event.uri,
                    defaultRoute = event.defaultRoute
                )
            }

            NavigationEvent.NavigateBack -> navController.popBackStack()

            is NavigationEvent.NavigateWeb -> {
                // TODO: CustomTabs or in-app webview 로 분기
            }
        }
    }

    private fun NavController.go(route: TransCallRoute, singleTop: Boolean = false) {
        navigate(route) { launchSingleTop = singleTop }
    }

    private fun NavController.goAndSave(
        route: TransCallRoute,
        popUpToRoute: TransCallRoute? = null,
        popUpToId: Int? = null,
        singleTop: Boolean = false
    ) {
        navigate(route) {
            when {
                popUpToId != null -> popUpTo(popUpToId) { saveState = true }
                popUpToRoute != null -> popUpTo(popUpToRoute) { saveState = true }
                else -> popUpTo(graph.findStartDestination().id) { saveState = true }
            }
            restoreState = true
            launchSingleTop = singleTop
        }
    }

    private fun NavController.goAndClear(
        route: TransCallRoute,
        popUpToRoute: TransCallRoute? = null,
        singleTop: Boolean = false
    ) {
        navigate(route) {
            if (popUpToRoute != null) popUpTo(popUpToRoute)
            else popUpTo(0)
            launchSingleTop = singleTop
        }
    }

    private fun NavController.goDeepLink(
        uri: Uri,
        defaultRoute: TransCallRoute,
    ) {

        if (graph.hasDeepLink(uri)) {
            val navOption = NavOptions.Builder()
                .setPopUpTo(defaultRoute, inclusive = false, saveState = false)
                .setLaunchSingleTop(true)
                .build()
            navigate(deepLink = uri, navOptions = navOption)
        } else {
            goAndClear(defaultRoute, popUpToRoute = null, singleTop = true)
        }
    }

    private fun NavController.currentNestedGraphStartId(): Int {
        val parent = currentDestination?.parent ?: graph
        return parent.findStartDestination().id
    }
}

@Composable
fun rememberNavigationEventHandler(
    navController: NavHostController = rememberNavController(),
): NavigationEventHandler = remember(navController) {
    NavigationEventHandler(navController)
}