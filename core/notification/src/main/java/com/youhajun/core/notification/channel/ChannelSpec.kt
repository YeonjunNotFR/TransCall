package com.youhajun.core.notification.channel

import android.net.Uri

data class ChannelSpec(
    val id: String,
    val nameRes: Int,
    val descRes: Int,
    val importance: Int,
    val showBadge: Boolean = false,
    val enableVibration: Boolean = false,
    val soundUri: Uri? = null
)