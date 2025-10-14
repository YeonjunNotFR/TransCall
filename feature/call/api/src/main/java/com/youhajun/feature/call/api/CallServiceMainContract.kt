package com.youhajun.feature.call.api

import androidx.compose.runtime.staticCompositionLocalOf

interface CallServiceMainContract {
    fun currentRoomId(): String?
}

val LocalCallServiceMainContract = staticCompositionLocalOf<CallServiceMainContract?> {
    error("No CallServiceContract provided")
}