package com.youhajun.core.notification.call

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.youhajun.core.notification.NotificationConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import com.youhajun.core.design.R
import javax.inject.Singleton

@Singleton
internal class CallNotificationBuilder @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun build(
        endCallIntent: Intent,
        openCallIntent: Intent
    ): Notification {

        val openCallPendingIntent = createOpenCallScreenPendingIntent(openCallIntent)
        val endCallPendingIntent = createEndCallPendingIntent(endCallIntent)

        return NotificationCompat.Builder(context, NotificationConfig.CHANNEL_ID_CALL)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setSmallIcon(R.drawable.ic_home)
            .setContentTitle(context.getString(R.string.notification_call_title))
            .setContentText(context.getString(R.string.notification_call_content))
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOnlyAlertOnce(true)
            .setContentIntent(openCallPendingIntent)
            .addAction(
                NotificationCompat.Action.Builder(
                    R.drawable.ic_home,
                    context.getString(R.string.notification_call_end),
                    endCallPendingIntent
                ).build()
            )
            .build()
    }

    private fun createOpenCallScreenPendingIntent(openCallIntent: Intent): PendingIntent {
        return PendingIntent.getActivity(
            context,
            0,
            openCallIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createEndCallPendingIntent(endCallIntent: Intent): PendingIntent {
        return PendingIntent.getService(
            context,
            1,
            endCallIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
