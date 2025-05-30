package com.youhajun.core.notification.call

import android.app.Notification
import android.content.Intent
import com.youhajun.core.notification.NotificationConfig
import com.youhajun.core.notification.NotificationController
import javax.inject.Inject
import javax.inject.Singleton

interface CallNotificationHandler {
    fun getCallingNotification(openIntent: Intent, endIntent: Intent): Notification
    fun registerChannel()
    fun unregisterChannel()
}

@Singleton
internal class CallNotificationHandlerImpl @Inject constructor(
    private val builder: CallNotificationBuilder,
    private val controller: CallNotificationController
): CallNotificationHandler {

    override fun getCallingNotification(openIntent: Intent, endIntent: Intent): Notification {
        return builder.build(endIntent, openIntent)
    }

    override fun registerChannel() {
        controller.createChannel(NotificationConfig.CHANNEL_ID_CALL)
    }

    override fun unregisterChannel() {
        controller.deleteChannel(NotificationConfig.CHANNEL_ID_CALL)
    }
}