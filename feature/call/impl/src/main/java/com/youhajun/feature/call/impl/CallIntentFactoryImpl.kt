package com.youhajun.feature.call.impl

import android.content.Context
import android.content.Intent
import com.youhajun.feature.call.api.CallIntentFactory
import com.youhajun.feature.call.impl.service.CallForegroundService
import javax.inject.Inject

class CallIntentFactoryImpl @Inject constructor() : CallIntentFactory {

    override fun getCallActivityIntent(context: Context, roomId: String): Intent {
        return Intent(context, CallActivity::class.java).apply {
            putExtra(CallActivity.INTENT_KEY_ROOM_ID, roomId)
        }
    }

    override fun getCallServiceIntent(context: Context, roomId: String): Intent {
        return Intent(context, CallForegroundService::class.java).apply {
            putExtra(CallForegroundService.INTENT_KEY_ROOM_ID, roomId)
        }
    }
}