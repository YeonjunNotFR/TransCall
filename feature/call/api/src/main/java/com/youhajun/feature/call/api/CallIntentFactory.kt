package com.youhajun.feature.call.api

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.staticCompositionLocalOf

interface CallIntentFactory {
    fun getCallActivityIntent(context: Context, roomId: String): Intent
    fun getCallServiceIntent(context: Context, roomId: String): Intent
    fun getCallServiceIntent(context: Context): Intent
}

val LocalCallIntentFactory = staticCompositionLocalOf<CallIntentFactory> {
    error("No GoogleAuthManager provided")
}