package com.youhajun.core.notification.notification

import android.app.Notification
import android.content.Context
import com.youhajun.core.notification.channel.ChannelSpec

sealed interface AppNotification {
    val channel: ChannelSpec
    val notificationId: Int
    fun build(context: Context): Notification
}