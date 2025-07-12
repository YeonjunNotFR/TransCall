package com.youhajun.feature.main

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youhajun.core.event.MainEvent
import com.youhajun.core.event.MainEventManager
import com.youhajun.core.route.DeepLinkRoute
import com.youhajun.core.route.NavigationEvent
import com.youhajun.core.route.TransCallRoute
import com.youhajun.domain.auth.usecase.HasAccessTokenUseCase
import com.youhajun.feature.auth.api.AuthNavRoute
import com.youhajun.feature.history.api.HistoryNavRoute
import com.youhajun.feature.home.api.HomeNavRoute
import com.youhajun.feature.main.navigation.MainTab
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainEventManager: MainEventManager,
    private val hasAccessTokenUseCase: HasAccessTokenUseCase,
) : ContainerHost<MainState, MainSideEffect>, ViewModel() {

    override val container: Container<MainState, MainSideEffect> = container(MainState()) {
        checkAppInfo()
        observeEvents()
    }

    fun onNavigationEvent(event: NavigationEvent) = intent {
        postSideEffect(MainSideEffect.Navigation(event))
    }

    fun onClickMainTab(tab: MainTab) = intent {
        val event = NavigationEvent.NavigateBottomBar(route = tab.route, launchSingleTop = true)
        postSideEffect(MainSideEffect.Navigation(event))
    }

    fun onHandleIntent(intent: Intent) {
        viewModelScope.launch {
            val hasToken = hasAccessTokenUseCase().getOrDefault(false)
            val route = intent.toNavRoute(hasToken)
            val event = NavigationEvent.NavigateAndClear(route = route, launchSingleTop = true)
            postNavigationEvent(event)
        }
    }

    private fun checkAppInfo() {
        viewModelScope.launch {

        }
    }

    private fun observeEvents() {
        viewModelScope.launch {
            mainEventManager.events.collect { event ->
                when (event) {
                    is MainEvent.RequireLogin -> {
                        val navigationEvent = NavigationEvent.NavigateAndClear(route = AuthNavRoute.Login, launchSingleTop = true)
                        postNavigationEvent(navigationEvent)
                    }

                    is MainEvent.ShowSnackBar -> TODO()
                }
            }
        }
    }

    private fun postNavigationEvent(event: NavigationEvent) = intent {
        postSideEffect(MainSideEffect.Navigation(event))
    }

    private fun Intent.toNavRoute(hasToken: Boolean): TransCallRoute {
        if (!hasToken) return AuthNavRoute.Login

        return data?.let { uri ->
            deepLinkRoutes.firstNotNullOfOrNull { route -> route.uriToRoute(uri) }
        } ?: HomeNavRoute.Home
    }

    companion object {
        private val deepLinkRoutes: List<DeepLinkRoute> = listOf(
            AuthNavRoute.Login,
            HomeNavRoute.Home,
            HistoryNavRoute.HistoryList,
        )
    }
}
