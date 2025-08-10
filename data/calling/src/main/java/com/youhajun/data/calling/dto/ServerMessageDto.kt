package com.youhajun.data.calling.dto

import com.youhajun.core.model.calling.type.MessageType
import com.youhajun.core.model.calling.ServerMessage
import com.youhajun.data.calling.dto.payload.ResponsePayloadDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ServerMessageDto(
    @SerialName("type")
    val type: String,
    @SerialName("payload")
    val payload: ResponsePayloadDto,
    @SerialName("timestamp")
    val timestamp: Long,
    @SerialName("senderId")
    val senderId: String?,
) {
    fun toModel(): ServerMessage = ServerMessage(
        type = MessageType.fromType(type),
        payload = payload.toModel(),
        timestamp = timestamp,
        senderId = senderId
    )
}