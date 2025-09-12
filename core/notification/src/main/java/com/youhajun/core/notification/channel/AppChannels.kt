package com.youhajun.core.notification.channel

import android.app.NotificationManager
import com.youhajun.core.design.R

internal object AppChannels {
    private const val CHANNEL_ID_DEFAULT  = "channel_default"
    private const val CHANNEL_ID_CALL = "channel_call"

    val DEFAULT = ChannelSpec(
        id = CHANNEL_ID_DEFAULT,
        nameRes = R.string.notification_channel_name_default,
        descRes = R.string.notification_channel_desc_default,
        importance = NotificationManager.IMPORTANCE_DEFAULT,
        showBadge = true,
        enableVibration = true,
        soundUri = null
    )

    val CALL = ChannelSpec(
        id = CHANNEL_ID_CALL,
        nameRes = R.string.notification_channel_name_call,
        descRes = R.string.notification_channel_desc_call,
        importance = NotificationManager.IMPORTANCE_DEFAULT,
        showBadge = false,
        enableVibration = false,
        soundUri = null
    )

    val INIT_CHANNELS = listOf(DEFAULT, CALL)
}