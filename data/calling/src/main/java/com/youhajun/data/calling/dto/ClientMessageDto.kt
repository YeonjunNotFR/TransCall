package com.youhajun.data.calling.dto

import com.youhajun.core.model.calling.ClientMessage
import com.youhajun.data.calling.dto.payload.RequestPayloadDto
import com.youhajun.data.calling.dto.payload.toDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ClientMessageDto(
    @SerialName("type")
    val type: String,
    @SerialName("payload")
    val payload: RequestPayloadDto,
)

internal fun ClientMessage.toDto(): ClientMessageDto = ClientMessageDto(
    type = type.type,
    payload = payload.toDto()
)