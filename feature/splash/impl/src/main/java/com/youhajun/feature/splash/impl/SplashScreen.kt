package com.youhajun.feature.splash.impl

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.youhajun.core.design.Colors
import com.youhajun.core.design.R
import com.youhajun.core.design.Typography
import com.youhajun.transcall.core.ui.components.VerticalSpacer

@Composable
internal fun SplashRoute() {
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1000)
            )
            alpha.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 1000)
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.PrimaryLight),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Call,
                contentDescription = "App Logo",
                tint = Colors.White,
                modifier = Modifier
                    .size(100.dp)
                    .alpha(alpha.value)
            )

            VerticalSpacer(16.dp)

            Text(
                text = stringResource(R.string.app_name),
                color = Colors.White,
                style = Typography.displayLarge,
                modifier = Modifier.alpha(alpha.value)
            )
        }
    }
}