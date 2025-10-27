package com.youhajun.data.common.dto.calling.payload

import com.youhajun.core.model.calling.payload.MediaStateRequest
import com.youhajun.core.model.calling.payload.RequestPayload
import com.youhajun.core.model.calling.payload.ResponsePayload
import com.youhajun.core.model.calling.payload.SignalingRequest
import kotlinx.serialization.Serializable

@Serializable
sealed interface MessagePayloadDto

sealed interface RequestPayloadDto : MessagePayloadDto

fun RequestPayload.toDto(): RequestPayloadDto = when (this) {
    is SignalingRequest -> this.toDto()
    is MediaStateRequest -> this.toDto()
}

@Serializable
sealed interface ResponsePayloadDto : MessagePayloadDto {
    fun toModel(): ResponsePayload
}
