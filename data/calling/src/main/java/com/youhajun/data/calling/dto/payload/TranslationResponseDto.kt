package com.youhajun.data.calling.dto.payload

import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.calling.payload.ResponsePayload
import com.youhajun.core.model.calling.payload.SttStart
import com.youhajun.core.model.calling.payload.TranslationMessage
import com.youhajun.data.calling.dto.payload.SttStartDto.STT_START_ACTION_TYPE
import com.youhajun.data.calling.dto.payload.TranslationMessageDto.Companion.TRANS_MESSAGE_ACTION_TYPE
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal sealed interface TranslationResponseDto : ResponsePayloadDto

@Serializable
@SerialName(TRANS_MESSAGE_ACTION_TYPE)
internal data class TranslationMessageDto(
    @SerialName("conversationId")
    val conversationId: String,
    @SerialName("roomId")
    val roomId: String,
    @SerialName("senderId")
    val senderId: String,
    @SerialName("originText")
    val originText: String,
    @SerialName("originLanguage")
    val originLanguage: String,
    @SerialName("transText")
    val transText: String?,
    @SerialName("transLanguage")
    val transLanguage: String?,
    @SerialName("updatedAtToEpochTime")
    val updatedAtToEpochTime: Long,
    @SerialName("createdAtToEpochTime")
    val createdAtToEpochTime: Long,
) : TranslationResponseDto {
    companion object {
        internal const val TRANS_MESSAGE_ACTION_TYPE: String = "transMessage"
    }

    override fun toModel(): ResponsePayload = TranslationMessage(
        conversationId = conversationId,
        roomId = roomId,
        senderId = senderId,
        originText = originText,
        originLanguage = LanguageType.fromCode(originLanguage),
        transText = transText,
        transLanguage = transLanguage?.let { LanguageType.fromCode(it) },
        updatedAtToEpochTime = updatedAtToEpochTime,
        createdAtToEpochTime = createdAtToEpochTime
    )
}

@Serializable
@SerialName(STT_START_ACTION_TYPE)
internal data object SttStartDto : TranslationResponseDto {

    internal const val STT_START_ACTION_TYPE: String = "sttStart"

    override fun toModel(): ResponsePayload = SttStart
}