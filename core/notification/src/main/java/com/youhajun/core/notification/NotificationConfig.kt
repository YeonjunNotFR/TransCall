package com.youhajun.core.notification

internal object NotificationConfig {
    const val CHANNEL_ID_FCM  = "channel_fcm"
    const val CHANNEL_ID_CALL = "channel_call"

    object CALL {
        const val NOTIFICATION_ID_CALLING = 1000
        const val NOTIFICATION_ID_CALL_RECEIVE = 1001
    }
}