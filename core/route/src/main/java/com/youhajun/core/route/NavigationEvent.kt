package com.youhajun.core.route

import android.net.Uri

sealed interface NavigationEvent {

    data class Navigate(
        val route: TransCallRoute,
        val launchSingleTop: Boolean,
    ) : NavigationEvent

    data class NavigateAndSave(
        val route: TransCallRoute,
        val launchSingleTop: Boolean,
        val popUpRoute: TransCallRoute
    ) : NavigationEvent

    data class NavigateBottomBar(
        val route: TransCallRoute,
        val launchSingleTop: Boolean,
    ) : NavigationEvent

    data class NavigateAndClear(
        val route: TransCallRoute,
        val launchSingleTop: Boolean,
        val popUpRoute: TransCallRoute? = null
    ) : NavigationEvent

    data class NavigateDeepLink(
        val uri: Uri,
        val defaultRoute: TransCallRoute
    ) : NavigationEvent

    data class NavigateWeb(val url: String) : NavigationEvent

    data object NavigateBack : NavigationEvent
}