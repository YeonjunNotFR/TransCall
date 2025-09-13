package com.youhajun.core.notification.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import com.youhajun.core.design.R
import com.youhajun.core.notification.channel.AppChannels
import com.youhajun.core.notification.channel.ChannelSpec

data class DefaultNotification(
    private val title: String,
    private val content: String,
    private val open: PendingIntent? = null,
    private val notificationIdOverride: Int? = null,
    private val smallIconRes: Int = R.drawable.ic_launcher_round,
    private val useBigTextStyle: Boolean = true,
    private val autoCancel: Boolean = true,
) : AppNotification {

    override val channel: ChannelSpec = AppChannels.DEFAULT

    override val notificationId: Int = notificationIdOverride ?: 1000

    override fun build(context: Context): Notification {
        val builder = NotificationCompat.Builder(context, channel.id)
            .setSmallIcon(smallIconRes)
            .setContentTitle(title)
            .setContentText(content)
            .setAutoCancel(autoCancel)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(open)

        if (useBigTextStyle) {
            builder.setStyle(NotificationCompat.BigTextStyle().bigText(content))
        }

        return builder.build()
    }
}
