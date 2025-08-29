package com.youhajun.feature.call.impl.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import com.youhajun.core.notification.call.CallNotificationHandler
import com.youhajun.feature.call.api.CallIntentFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CallForegroundService : Service(), CallServiceManager.ServiceCallback {

    @Inject
    lateinit var serviceManager: CallServiceManager

    @Inject
    lateinit var callNotificationHandler: CallNotificationHandler

    @Inject
    lateinit var callIntentFactory: CallIntentFactory

    private var currentRoomId: String? = null

    override fun onBind(intent: Intent?): IBinder = LocalBinder()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handleAction(intent)
        if (currentRoomId != null) return START_REDELIVER_INTENT
        val roomId = intent?.getStringExtra(INTENT_KEY_ROOM_ID) ?: return START_REDELIVER_INTENT
        currentRoomId = roomId
        val notification = createNotification(roomId)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val flag = ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE or ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA
            startForeground(DEFAULT_SERVICE_ID, notification, flag)
        } else {
            startForeground(DEFAULT_SERVICE_ID, notification)
        }
        serviceManager.initManager(this)
        serviceManager.startCall(roomId)
        return START_REDELIVER_INTENT
    }

    private fun handleAction(intent: Intent?) {
        when (intent?.action) {
            INTENT_ACTION_CALL_LEFT -> {
                serviceManager.leaveCall()
            }
            else -> Unit
        }
    }

    private fun createNotification(roomId: String): Notification {
        callNotificationHandler.registerChannel()
        return callNotificationHandler.getCallingNotification(
            openIntent = callIntentFactory.getCallActivityIntent(this, roomId).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
            endIntent = callIntentFactory.getCallServiceIntent(this, roomId)
        )
    }

    override fun onError(error: Throwable) {
        stopSelf()
    }

    override fun onLeave() {
        stopSelf()
    }

    inner class LocalBinder : Binder() {
        fun getContract(): CallServiceContract = serviceManager
    }

    companion object {
        const val INTENT_KEY_ROOM_ID = "ROOM_ID"
        const val INTENT_ACTION_CALL_LEFT = "ACTION_CALL_LEFT"
        private const val DEFAULT_SERVICE_ID = 1
    }
}