package com.youhajun.feature.call.impl

import android.content.Context
import android.content.Intent
import com.youhajun.feature.call.api.CallIntentFactory
import com.youhajun.feature.call.impl.service.CallForegroundService

class CallIntentFactoryImpl : CallIntentFactory {

    override fun goToCallActivity(context: Context, roomCode: String): Intent {
        return Intent(context, CallActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(CallActivity.INTENT_KEY_ROOM_CODE, roomCode)
        }
    }

    override fun callService(context: Context): Intent {
        return Intent(context, CallForegroundService::class.java)
    }

    override fun startCallService(context: Context, roomCode: String): Intent {
        return Intent(context, CallForegroundService::class.java).apply {
            putExtra(CallForegroundService.INTENT_KEY_ROOM_CODE, roomCode)
        }
    }
}