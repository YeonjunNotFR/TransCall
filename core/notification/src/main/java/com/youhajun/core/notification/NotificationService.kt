package com.youhajun.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.youhajun.core.notification.channel.AppChannels
import com.youhajun.core.notification.channel.ChannelSpec
import com.youhajun.core.notification.notification.AppNotification
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun channelInit() {
        AppChannels.INIT_CHANNELS.forEach {
            ensureChannel(it)
        }
    }

    fun publish(notification: AppNotification) {
        ensureChannel(notification.channel)
        manager.notify(notification.notificationId, notification.build(context))
    }

    fun cancel(notificationId: Int) {
        manager.cancel(notificationId)
    }

    private fun ensureChannel(spec: ChannelSpec) {
        if (manager.getNotificationChannel(spec.id) != null) return
        val channel = NotificationChannel(
            spec.id,
            context.getString(spec.nameRes),
            spec.importance
        ).apply {
            description = context.getString(spec.descRes)
            setShowBadge(spec.showBadge)
            enableVibration(spec.enableVibration)
            setSound(spec.soundUri, null)
        }

        manager.createNotificationChannel(channel)
    }
}