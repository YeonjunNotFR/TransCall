package com.youhajun.core.notification.call

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.youhajun.core.design.R
import com.youhajun.core.notification.NotificationController
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CallNotificationController @Inject constructor(
    @ApplicationContext private val context: Context,
) : NotificationController {

    private val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override fun createChannel(channelId: String) {
        val channel = NotificationChannel(
            channelId,
            context.getString(R.string.notification_channel_name_call),
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = context.getString(R.string.notification_channel_desc_call)
            setSound(null, null)
            enableVibration(false)
            setShowBadge(false)
        }

        manager.createNotificationChannel(channel)
    }

    override fun deleteChannel(channelId: String) {
        manager.deleteNotificationChannel(channelId)
    }

    override fun notify(id: Int, notification: Notification) {
        manager.notify(id, notification)
    }

    override fun cancel(id: Int) {
        manager.cancel(id)
    }
}
