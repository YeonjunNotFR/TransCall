package com.youhajun.data.calling.dto.payload

import com.youhajun.core.model.calling.payload.SttMessage
import com.youhajun.core.model.calling.payload.TranslationRequest
import com.youhajun.data.calling.dto.payload.SttMessageDto.Companion.STT_MESSAGE_ACTION_TYPE
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal sealed interface TranslationRequestDto : MessagePayloadDto, RequestPayloadDto

@Serializable
@SerialName(STT_MESSAGE_ACTION_TYPE)
internal data class SttMessageDto(
    @SerialName("text")
    val text: String,
    @SerialName("language")
    val language: String,
) : TranslationRequestDto {
    companion object {
        internal const val STT_MESSAGE_ACTION_TYPE: String = "sttMessage"
    }
}

internal fun TranslationRequest.toDto(): TranslationRequestDto = when (this) {
    is SttMessage -> SttMessageDto(
        text = text,
        language = language.code,
    )
}