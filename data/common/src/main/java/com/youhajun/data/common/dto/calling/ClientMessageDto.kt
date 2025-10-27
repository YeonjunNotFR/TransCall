package com.youhajun.data.common.dto.calling

import com.youhajun.core.model.calling.ClientMessage
import com.youhajun.data.common.dto.calling.payload.RequestPayloadDto
import com.youhajun.data.common.dto.calling.payload.toDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClientMessageDto(
    @SerialName("type")
    val type: String,
    @SerialName("payload")
    val payload: RequestPayloadDto,
)

fun ClientMessage.toDto(): ClientMessageDto = ClientMessageDto(
    type = type.type,
    payload = payload.toDto()
)