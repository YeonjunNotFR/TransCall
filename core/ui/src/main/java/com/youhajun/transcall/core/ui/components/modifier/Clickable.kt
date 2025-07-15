package com.youhajun.transcall.core.ui.components.modifier

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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