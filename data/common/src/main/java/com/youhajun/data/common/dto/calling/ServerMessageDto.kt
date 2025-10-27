package com.youhajun.data.common.dto.calling

import com.youhajun.core.model.calling.type.MessageType
import com.youhajun.core.model.calling.ServerMessage
import com.youhajun.data.common.dto.calling.payload.ResponsePayloadDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServerMessageDto(
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