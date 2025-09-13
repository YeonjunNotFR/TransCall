package com.youhajun.core.notification.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import com.youhajun.core.design.R
import com.youhajun.core.notification.channel.AppChannels
import com.youhajun.core.notification.channel.ChannelSpec

data class CallActions(
    val open: PendingIntent,
    val end: PendingIntent,
    val micToggle: PendingIntent,
    val cameraToggle: PendingIntent
)

data class CallNotification(
    private val title: String,
    private val content: String,
    private val actions: CallActions,
    private val isMicEnabled: Boolean,
    private val isCameraEnabled: Boolean
) : AppNotification {

    override val channel: ChannelSpec = AppChannels.CALL

    override val notificationId: Int = 2000

    override fun build(context: Context): Notification {
        val micIcon = if (isMicEnabled) R.drawable.ic_call_mic_on else R.drawable.ic_call_mic_off
        val micText = if (isMicEnabled) context.getString(R.string.notification_mic_on) else context.getString(R.string.notification_mic_off)
        val camIcon = if (isCameraEnabled) R.drawable.ic_call_camera_on else R.drawable.ic_call_camera_off
        val camText = if (isCameraEnabled) context.getString(R.string.notification_camera_on) else context.getString(R.string.notification_camera_off)

        return NotificationCompat.Builder(context, channel.id)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setSmallIcon(R.drawable.ic_home)
            .setContentTitle(title)
            .setContentText(content)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(actions.open)
            .addAction(R.drawable.ic_leave_call, context.getString(R.string.notification_call_end), actions.end)
            .addAction(micIcon, micText, actions.micToggle)
            .addAction(camIcon, camText, actions.cameraToggle)
            .build()
    }
}
