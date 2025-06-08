package com.youhajun.transcall.core.ui.util

import android.graphics.BlurMaskFilter
import android.graphics.Paint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.youhajun.core.design.Colors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Modifier.noRippleClickable(
    enabled: Boolean = true,
    throttleDelay: Long = 1000L,
    onClick: () -> Unit
) = this then Modifier.throttleClick(
    delayMillis = throttleDelay,
    enabled = enabled,
    onClick = onClick
)

@Composable
fun Modifier.throttleClick(
    delayMillis: Long = 1000L,
    enabled: Boolean,
    onClick: () -> Unit
): Modifier {
    val scope = rememberCoroutineScope()
    val interaction = remember { MutableInteractionSource() }
    var isClickable by remember { mutableStateOf(true) }

    return this.then(
        Modifier.clickable(
            enabled = isClickable && enabled,
            interactionSource = interaction,
            indication = null,
            onClick = {
                isClickable = false
                scope.launch {
                    delay(delayMillis)
                    isClickable = true
                }
                onClick()
            }
        )
    )
}

@Composable
fun Modifier.speakingGlow(
    isSpeaking: Boolean,
    shape: Shape,
    glowColor: Color = Colors.FF00FFFF,
    minBlurDp: Dp = 1.dp,
    maxBlurDp: Dp = 50.dp,
    outlineWidth: Dp = 3.dp,
    blurAlpha: Float = 0.9f,
    outlineAlpha: Float = 1f,
): Modifier {

    val density = LocalDensity.current
    val minBlurPx = remember(minBlurDp, density) {
        with(density) { minBlurDp.toPx() }
    }
    val maxBlurPx = remember(maxBlurDp, density) {
        with(density) { maxBlurDp.toPx() }
    }
    val defaultGlowWidth = remember(outlineWidth, density) {
        with(density) { outlineWidth.toPx() }
    }

    val animatedBlur by rememberInfiniteTransition().animateFloat(
        initialValue = minBlurPx,
        targetValue = if (isSpeaking) maxBlurPx else minBlurPx,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
    )

    return this.then(
        Modifier.drawWithContent {
            drawContent()

            val outlineSize = size.copy(
                width = size.width + defaultGlowWidth,
                height = size.height + defaultGlowWidth
            )
            val outline = shape.createOutline(outlineSize, layoutDirection, this)
            val path = Path().apply {
                when (outline) {
                    is Outline.Generic -> addPath(outline.path)
                    is Outline.Rounded -> addRoundRect(outline.roundRect)
                    is Outline.Rectangle -> addRect(outline.rect)
                }
            }

            val halfGlow = defaultGlowWidth / 2f
            translate(left = -halfGlow, top = -halfGlow) {
                drawIntoCanvas { canvas ->
                    val paint = Paint().apply {
                        isAntiAlias = true
                        color = glowColor.copy(alpha = blurAlpha).toArgb()
                        maskFilter = BlurMaskFilter(
                            animatedBlur + 2f,
                            BlurMaskFilter.Blur.OUTER
                        )
                    }
                    canvas.nativeCanvas.drawPath(path.asAndroidPath(), paint)
                }

                drawPath(
                    path = path,
                    color = glowColor.copy(alpha = outlineAlpha),
                    style = Stroke(width = defaultGlowWidth)
                )
            }
        }
    )
}