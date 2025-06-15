package com.youhajun.data.calling.dto

import com.youhajun.core.model.calling.CallingMessage
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CallingMessageDto(
    @Polymorphic
    @SerialName("data")
    val data: CallingDataDto,
    @SerialName("from")
    val from: String,
    @SerialName("timestamp")
    val timestamp: Long,
) {
    fun toModel(): CallingMessage = CallingMessage(
        type = data.toModel(),
        from = from,
        timestamp = timestamp
    )
}

internal fun CallingMessage.toDto(): CallingMessageDto = CallingMessageDto(
    data = type.toDto(),
    from = from,
    timestamp = timestamp
)