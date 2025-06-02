package com.youhajun.core.route

sealed interface NavigationEvent {

    data class Navigate(
        val route: TransCallRoute,
        val saveState: Boolean,
        val launchSingleTop: Boolean,
    ) : NavigationEvent

    data class NavigateWeb(val url: String) : NavigationEvent

    data object NavigateBack : NavigationEvent

    data class NavigateAndClear(
        val route: TransCallRoute,
        val launchSingleTop: Boolean,
    ) : NavigationEvent
}