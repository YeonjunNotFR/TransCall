package com.youhajun.transcall.fcm.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal sealed interface FcmPayload

@Serializable
@SerialName("notification")
internal object NotificationPayload : FcmPayload

@Serializable
@SerialName("conversation_sync")
internal data class ConversationSyncPayload(
    val roomId: String,
    val joinedAt: Long,
    val leftAt: Long
) : FcmPayload

@Serializable
@SerialName("history")
internal object HistoryPayload : FcmPayload

@Serializable
@SerialName("history_detail")
internal data class HistoryDetailPayload(
    val historyId: String,
) : FcmPayload

@Serializable
@SerialName("profile")
internal object ProfilePayload : FcmPayload

@Serializable
@SerialName("create_room")
internal object CreateRoomPayload : FcmPayload

@Serializable
@SerialName("room_list")
internal object RoomListPayload : FcmPayload