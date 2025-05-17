package com.youhajun.feature.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.youhajun.feature.call.api.CallNavGraphRegistrar
import com.youhajun.feature.history.api.HistoryNavGraphRegistrar
import com.youhajun.feature.home.api.HomeNavGraphRegistrar
import com.youhajun.feature.main.navigation.MainTab
import com.youhajun.feature.main.navigation.rememberMainNavigator
import com.youhajun.transcall.core.ui.theme.TransCallTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var homeNavGraphRegistrar: HomeNavGraphRegistrar

    @Inject
    lateinit var callNavGraphRegistrar: CallNavGraphRegistrar

    @Inject
    lateinit var historyNavGraphRegistrar: HistoryNavGraphRegistrar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            TransCallTheme {
                val navigator = rememberMainNavigator()
                MainScreen(
                    mainNavigator = navigator,
                    homeNavGraphRegistrar = homeNavGraphRegistrar,
                    callNavGraphRegistrar = callNavGraphRegistrar,
                    historyNavGraphRegistrar = historyNavGraphRegistrar,
                    mainTabs = MainTab.tabList().toImmutableList(),
                )
            }
        }
    }
}