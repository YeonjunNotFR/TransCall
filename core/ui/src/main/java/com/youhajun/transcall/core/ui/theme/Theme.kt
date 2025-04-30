package com.youhajun.transcall.core.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val TransCallLightColorScheme: ColorScheme = lightColorScheme(
    primary = TransCallColors.Primary,
    onPrimary = TransCallColors.White,
    primaryContainer = TransCallColors.PrimaryLight,
    onPrimaryContainer = TransCallColors.Black,
    secondary = TransCallColors.Secondary,
    onSecondary = TransCallColors.Black,
    background = TransCallColors.BackgroundLight,
    onBackground = TransCallColors.Gray900,
    surface = TransCallColors.SurfaceLight,
    onSurface = TransCallColors.Gray900,
    error = TransCallColors.Error,
    onError = TransCallColors.White,
    outline = TransCallColors.Outline
)

private val TransCallDarkColorScheme: ColorScheme = darkColorScheme(
    primary = TransCallColors.PrimaryLight,
    onPrimary = TransCallColors.Black,
    primaryContainer = TransCallColors.PrimaryDark,
    onPrimaryContainer = TransCallColors.White,
    secondary = TransCallColors.SecondaryLight,
    onSecondary = TransCallColors.Black,
    background = TransCallColors.BackgroundDark,
    onBackground = TransCallColors.White,
    surface = TransCallColors.SurfaceDark,
    onSurface = TransCallColors.White,
    error = TransCallColors.Error,
    onError = TransCallColors.Black,
    outline = TransCallColors.Gray500
)

@Composable
fun TransCallTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> TransCallDarkColorScheme
        else -> TransCallLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = TransCallTypography,
        content = content
    )
}