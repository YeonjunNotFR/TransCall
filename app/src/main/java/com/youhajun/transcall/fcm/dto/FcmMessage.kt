package com.youhajun.transcall.fcm.dto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("type")
internal data class FcmMessage(
    val type: String,
    val payload: FcmPayload = NotificationPayload
)