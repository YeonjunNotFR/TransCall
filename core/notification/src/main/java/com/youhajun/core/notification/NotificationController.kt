package com.youhajun.core.notification

import android.app.Notification

interface NotificationController {
    fun createChannel(channelId: String)
    fun deleteChannel(channelId: String)
    fun notify(id: Int, notification: Notification)
    fun cancel(id: Int)
}