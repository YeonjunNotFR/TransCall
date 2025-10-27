package com.youhajun.data.common.dto.calling.payload

import com.youhajun.core.model.calling.payload.ResponsePayload
import com.youhajun.core.model.calling.payload.SttStart
import com.youhajun.data.common.dto.calling.payload.SttStartDto.STT_START_ACTION_TYPE
import com.youhajun.data.common.dto.calling.payload.TranslationMessageDto.Companion.TRANS_MESSAGE_ACTION_TYPE
import com.youhajun.data.common.dto.conversation.ConversationResponseDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface TranslationResponseDto : ResponsePayloadDto

@Serializable
@SerialName(TRANS_MESSAGE_ACTION_TYPE)
data class TranslationMessageDto(
    @SerialName("message")
    val message: ConversationResponseDto
) : TranslationResponseDto {
    companion object {
        const val TRANS_MESSAGE_ACTION_TYPE: String = "transMessage"
    }

    override fun toModel(): ResponsePayload = message.toModel()
}

@Serializable
@SerialName(STT_START_ACTION_TYPE)
data object SttStartDto : TranslationResponseDto {

    const val STT_START_ACTION_TYPE: String = "sttStart"

    override fun toModel(): ResponsePayload = SttStart
}