package com.youhajun.feature.call.api

import android.content.Context
import android.content.Intent

interface CallIntentFactory {
    fun goToCallActivity(context: Context, roomCode: String): Intent
    fun startCallService(context: Context, roomCode: String): Intent
    fun callService(context: Context): Intent
}