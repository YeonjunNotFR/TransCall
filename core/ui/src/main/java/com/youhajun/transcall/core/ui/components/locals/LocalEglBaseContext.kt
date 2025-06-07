package com.youhajun.transcall.core.ui.components.locals

import androidx.compose.runtime.staticCompositionLocalOf
import org.webrtc.EglBase

val LocalEglBaseContext = staticCompositionLocalOf<EglBase.Context> {
    error("No EglBase.Context provided")
}