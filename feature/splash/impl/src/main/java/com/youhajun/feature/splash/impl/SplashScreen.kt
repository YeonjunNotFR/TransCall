package com.youhajun.feature.splash.impl

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youhajun.core.design.Colors
import com.youhajun.core.design.R
import com.youhajun.core.design.Typography
import com.youhajun.transcall.core.ui.components.VerticalSpacer

@Composable
internal fun SplashRoute() {
    val infiniteTransition = rememberInfiniteTransition()

    // 동일한 애니메이션 리듬 기반
    val sharedAnimProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        )
    )

    val logoScale = 0.9f + (sharedAnimProgress * 0.2f)
    val logoRotation = -10f + (sharedAnimProgress * 20f)

    val textScale = 1f + (sharedAnimProgress * 0.05f)
    val textOffsetY = -4f + (sharedAnimProgress * 8f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.PrimaryLight),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Call,
            contentDescription = "App Logo",
            tint = Colors.White,
            modifier = Modifier
                .size(100.dp)
                .graphicsLayer {
                    scaleX = logoScale
                    scaleY = logoScale
                    rotationZ = logoRotation
                }
        )

        VerticalSpacer(16.dp)

        Text(
            text = stringResource(R.string.app_name),
            color = Colors.White,
            style = Typography.displayLarge,
            modifier = Modifier
                .graphicsLayer {
                    scaleX = textScale
                    scaleY = textScale
                    translationY = textOffsetY
                }
        )
    }
}

@Composable
@Preview
private fun SplashPreviewMirror() {
    SplashPreview()
}