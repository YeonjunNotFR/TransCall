package com.youhajun.feature.main

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.youhajun.core.event.MainEvent
import com.youhajun.core.event.MainEventManager
import com.youhajun.core.route.NavigationEvent
import com.youhajun.domain.auth.usecase.HasAccessTokenUseCase
import com.youhajun.feature.auth.api.AuthNavRoute
import com.youhajun.feature.home.api.HomeNavRoute
import com.youhajun.feature.main.navigation.MainTab
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainEventManager: MainEventManager,
    private val hasAccessTokenUseCase: HasAccessTokenUseCase,
) : ContainerHost<MainState, MainSideEffect>, ViewModel() {

    override val container: Container<MainState, MainSideEffect> = container(MainState()) {
        supervisorScope {
            launch { checkAppInfo() }
            launch { observeEvents() }
        }
    }

    fun onNavigationEvent(event: NavigationEvent) = intent {
        postSideEffect(MainSideEffect.Navigation(event))
    }

    fun onClickMainTab(tab: MainTab) = intent {
        val event = NavigationEvent.NavigateBottomBar(route = tab.route, launchSingleTop = true)
        postSideEffect(MainSideEffect.Navigation(event))
    }

    fun onHandleIntent(intent: Intent) = intent {
        val hasToken = hasAccessTokenUseCase().getOrDefault(false)
        val uri = intent.data
        val event = when {
            !hasToken -> NavigationEvent.NavigateAndClear(route = AuthNavRoute.Login, launchSingleTop = true)
            uri != null -> NavigationEvent.NavigateDeepLink(uri, defaultRoute = HomeNavRoute.Home)
            else -> NavigationEvent.NavigateAndClear(route = HomeNavRoute.Home, launchSingleTop = true)
        }

        postSideEffect(MainSideEffect.Navigation(event))
    }

    @OptIn(OrbitExperimental::class)
    private suspend fun checkAppInfo() = subIntent {

    }

    @OptIn(OrbitExperimental::class)
    private suspend fun observeEvents() = subIntent {
        repeatOnSubscription {
            mainEventManager.events.collect { event ->
                when (event) {
                    is MainEvent.RequireLogin -> navigateToLogin()
                    is MainEvent.ShowSnackBar -> {}
                }
            }
        }
    }
    @OptIn(OrbitExperimental::class)
    private suspend fun navigateToLogin() = subIntent {
        val navigationEvent = NavigationEvent.NavigateAndClear(route = AuthNavRoute.Login, launchSingleTop = true)
        postSideEffect(MainSideEffect.Navigation(navigationEvent))
    }
}
