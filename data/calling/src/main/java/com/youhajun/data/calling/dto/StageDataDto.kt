package com.youhajun.data.calling.dto

import com.youhajun.core.model.calling.StageMessageType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal sealed interface StageDataDto : CallingDataDto {
    @Serializable
    @SerialName("error")
    data class ErrorDto(
        @SerialName("message")
        val message: String
    ) : StageDataDto

    @Serializable
    @SerialName("waiting")
    data object WaitingDto : StageDataDto

    @Serializable
    @SerialName("signaling")
    data class SignalingDto(
        @SerialName("isCaller")
        val isCaller: Boolean
    ) : StageDataDto

    @Serializable
    @SerialName("calling")
    data object CallingDto : StageDataDto

    @Serializable
    @SerialName("ended")
    data object EndedDto : StageDataDto

    override fun toModel(): StageMessageType {
        return when (this) {
            is ErrorDto -> StageMessageType.Error(message)
            is WaitingDto -> StageMessageType.Waiting
            is SignalingDto -> StageMessageType.Signaling(isCaller)
            is CallingDto -> StageMessageType.Calling
            EndedDto -> StageMessageType.Ended
        }
    }
}