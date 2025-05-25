package com.youhajun.core.design

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val TransCallLightColorScheme: ColorScheme = lightColorScheme(
    primary = Colors.Primary,
    onPrimary = Colors.White,
    primaryContainer = Colors.PrimaryLight,
    onPrimaryContainer = Colors.Black,
    secondary = Colors.Secondary,
    onSecondary = Colors.Black,
    background = Colors.BackgroundLight,
    onBackground = Colors.Gray900,
    surface = Colors.SurfaceLight,
    onSurface = Colors.Gray900,
    error = Colors.Error,
    onError = Colors.White,
    outline = Colors.Outline
)

private val TransCallDarkColorScheme: ColorScheme = darkColorScheme(
    primary = Colors.PrimaryLight,
    onPrimary = Colors.Black,
    primaryContainer = Colors.PrimaryDark,
    onPrimaryContainer = Colors.White,
    secondary = Colors.SecondaryLight,
    onSecondary = Colors.Black,
    background = Colors.BackgroundDark,
    onBackground = Colors.White,
    surface = Colors.SurfaceDark,
    onSurface = Colors.White,
    error = Colors.Error,
    onError = Colors.Black,
    outline = Colors.Gray500
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
        typography = Typography,
        content = content
    )
}