package com.youhajun.feature.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.youhajun.core.route.NavigationEvent
import com.youhajun.feature.main.navigation.MainBottomBar
import com.youhajun.feature.main.navigation.MainNavHost
import com.youhajun.feature.main.navigation.MainTab
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun MainScreen(
    navController: NavHostController,
    bottomBarVisibility: Boolean,
    mainTabs: ImmutableList<MainTab>,
    currentTab: MainTab,
    onNavigationEvent: (NavigationEvent) -> Unit,
    onClickMainTab: (MainTab) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize().safeContentPadding(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            MainBottomBar(
                visibility = bottomBarVisibility,
                currentTab = currentTab,
                tabs = mainTabs,
                onClickMainTab = onClickMainTab
            )
        },
    ) { padding ->
        MainNavHost(
            padding = padding,
            navController = navController,
            onNavigationEvent = onNavigationEvent,
        )
    }
}
