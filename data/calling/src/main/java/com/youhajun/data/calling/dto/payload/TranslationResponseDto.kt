package com.youhajun.data.calling.dto.payload

import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.calling.payload.ResponsePayload
import com.youhajun.core.model.calling.payload.SttStart
import com.youhajun.core.model.calling.payload.TranslationMessage
import com.youhajun.core.model.conversation.SenderInfo
import com.youhajun.data.calling.dto.payload.SttStartDto.STT_START_ACTION_TYPE
import com.youhajun.data.calling.dto.payload.TranslationMessageDto.Companion.TRANS_MESSAGE_ACTION_TYPE
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal sealed interface TranslationResponseDto : MessagePayloadDto, ResponsePayloadDto

@Serializable
@SerialName(TRANS_MESSAGE_ACTION_TYPE)
internal data class TranslationMessageDto(
    @SerialName("conversationId")
    val conversationId: String,
    @SerialName("senderInfo")
    val senderInfo: SenderInfoDto,
    @SerialName("originText")
    val originText: String,
    @SerialName("originLanguage")
    val originLanguage: String,
    @SerialName("transText")
    val transText: String?,
    @SerialName("transLanguage")
    val transLanguage: String,
) : TranslationResponseDto {
    companion object {
        internal const val TRANS_MESSAGE_ACTION_TYPE: String = "transMessage"
    }

    override fun toModel(): ResponsePayload = TranslationMessage(
        conversationId = conversationId,
        senderInfo = senderInfo.toModel(),
        originText = originText,
        originLanguage = LanguageType.fromCode(originLanguage),
        transText = transText,
        transLanguage = LanguageType.fromCode(transLanguage),
    )
}

@Serializable
internal data class SenderInfoDto(
    @SerialName("senderId")
    val senderId: String,
    @SerialName("displayName")
    val displayName: String,
    @SerialName("language")
    val language: String,
    @SerialName("profileUrl")
    val profileUrl: String? = null,
) {
    fun toModel() = SenderInfo(
        senderId = senderId,
        displayName = displayName,
        language = LanguageType.fromCode(language),
        profileUrl = profileUrl
    )
}

@Serializable
@SerialName(STT_START_ACTION_TYPE)
internal data object SttStartDto : TranslationResponseDto {

    internal const val STT_START_ACTION_TYPE: String = "sttStart"

    override fun toModel(): ResponsePayload = SttStart
}