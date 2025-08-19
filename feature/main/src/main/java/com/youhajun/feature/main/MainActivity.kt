package com.youhajun.feature.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.rememberNavController
import com.youhajun.core.design.TransCallTheme
import com.youhajun.core.route.rememberNavigationEventHandler
import com.youhajun.feature.auth.api.GoogleAuthManager
import com.youhajun.feature.auth.impl.util.LocalGoogleAuthManager
import com.youhajun.feature.call.api.CallIntentFactory
import com.youhajun.feature.call.api.LocalCallIntentFactory
import com.youhajun.feature.main.navigation.MainTab
import com.youhajun.feature.main.navigation.rememberMainNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.collections.immutable.toImmutableList
import org.orbitmvi.orbit.compose.collectSideEffect
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var googleAuthManager: GoogleAuthManager

    @Inject
    lateinit var callIntentFactory: CallIntentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handleIntent(intent)

        setContent {
            TransCallTheme {
                val navigator = rememberNavController()
                val mainNavigator = rememberMainNavigator(navigator)
                val navigationEventHandler = rememberNavigationEventHandler(navigator)

                viewModel.collectSideEffect {
                    when (it) {
                        is MainSideEffect.Navigation -> {
                            navigationEventHandler.handleNavigationEvent(it.event)
                        }
                    }
                }

                CompositionLocalProvider(
                    LocalGoogleAuthManager provides googleAuthManager,
                    LocalCallIntentFactory provides callIntentFactory
                ) {
                    MainScreen(
                        navController = navigator,
                        bottomBarVisibility = mainNavigator.shouldShowBottomBar(),
                        currentTab = mainNavigator.currentTab ?: MainTab.Home,
                        mainTabs = MainTab.tabList().toImmutableList(),
                        onNavigationEvent = viewModel::onNavigationEvent,
                        onClickMainTab = viewModel::onClickMainTab
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        viewModel.onHandleIntent(intent)
    }
}