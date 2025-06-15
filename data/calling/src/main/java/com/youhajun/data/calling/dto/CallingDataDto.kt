package com.youhajun.data.calling.dto

import com.youhajun.core.model.calling.*
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Polymorphic
@Serializable
internal sealed interface CallingDataDto {
    fun toModel(): CallingMessageType = when (this) {
        is StageDataDto -> toModel()
        is SignalingDataDto -> toModel()
        is MediaDataDto -> toModel()
        is JoinedDataDto -> CallingMessageType.Joined(participants)
        is LeftResponseDto -> CallingMessageType.LeftResponse(userId)
        ConnectDataDto -> CallingMessageType.Connect
        else -> throw IllegalArgumentException("Unsupported Response CallingMessageType: $this")
    }
}

internal fun CallingMessageType.toDto(): CallingDataDto = when (this) {
    is SignalingMessageType -> toDto()
    is MediaMessageType -> toDto()
    is CallingMessageType.LeftRequest -> LeftRequestDto
    else -> throw IllegalArgumentException("Unsupported Request CallingMessageType: $this")
}

@Serializable
@SerialName("connect")
internal data object ConnectDataDto : CallingDataDto

@Serializable
@SerialName("leftRequest")
internal data object LeftRequestDto : CallingDataDto

@Serializable
@SerialName("joined")
internal data class JoinedDataDto(
    @SerialName("participants")
    val participants: List<String>
) : CallingDataDto

@Serializable
@SerialName("left")
internal data class LeftResponseDto(
    @SerialName("userId")
    val userId: String
) : CallingDataDto