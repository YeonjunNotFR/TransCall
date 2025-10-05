package com.youhajun.data.calling.dto.payload

import com.youhajun.core.model.calling.payload.MediaStateRequest
import com.youhajun.core.model.calling.payload.RequestPayload
import com.youhajun.core.model.calling.payload.ResponsePayload
import com.youhajun.core.model.calling.payload.SignalingRequest
import kotlinx.serialization.Serializable

@Serializable
internal sealed interface MessagePayloadDto

internal sealed interface RequestPayloadDto : MessagePayloadDto

internal fun RequestPayload.toDto(): RequestPayloadDto = when (this) {
    is SignalingRequest -> this.toDto()
    is MediaStateRequest -> this.toDto()
}

@Serializable
internal sealed interface ResponsePayloadDto : MessagePayloadDto {
    fun toModel(): ResponsePayload
}
