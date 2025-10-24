package com.youhajun.feature.main

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.youhajun.core.design.TransCallTheme
import com.youhajun.core.route.rememberNavigationEventHandler
import com.youhajun.feature.auth.api.GoogleAuthManager
import com.youhajun.feature.auth.util.LocalGoogleAuthManager
import com.youhajun.feature.call.api.CallIntentFactory
import com.youhajun.feature.call.api.CallServiceMainContract
import com.youhajun.feature.call.api.LocalCallIntentFactory
import com.youhajun.feature.call.api.LocalCallServiceMainContract
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

    private var callServiceMainContract: CallServiceMainContract? by mutableStateOf(null)
    private var isBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            callServiceMainContract = binder as? CallServiceMainContract
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            callServiceMainContract = null
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

                LaunchedEffect(Unit) {
                    viewModel.onHandleIntent(intent)
                }

                CompositionLocalProvider(
                    LocalGoogleAuthManager provides googleAuthManager,
                    LocalCallIntentFactory provides callIntentFactory,
                    LocalCallServiceMainContract provides callServiceMainContract
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

    override fun onStart() {
        super.onStart()
        val intent = callIntentFactory.getCallServiceIntent(this)
        bindService(intent, serviceConnection, 0)
    }

    override fun onStop() {
        super.onStop()
        if (isBound) {
            unbindService(serviceConnection)
            isBound = false
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        viewModel.onHandleIntent(intent)
    }
}