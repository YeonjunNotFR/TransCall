package com.youhajun.data.conversation.dto

import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.conversation.Conversation
import com.youhajun.core.model.conversation.ConversationMessageType
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Polymorphic
@Serializable
internal sealed interface ConversationDataDto {

    @Serializable
    @SerialName("sttMessage")
    data class SttMessageDto(
        @SerialName("roomId")
        val roomId: String,
        @SerialName("text")
        val text: String,
        @SerialName("languageType")
        val languageType: LanguageType,
    ) : ConversationDataDto

    fun toModel(): ConversationMessageType = when (this) {
        is SttMessageDto -> ConversationMessageType.SttMessage(
            roomId = roomId,
            text = text,
            languageType = languageType
        )
        is ConversationDto -> toModel()
    }
}

internal fun ConversationMessageType.toDto(): ConversationDataDto = when (this) {
    is ConversationMessageType.SttMessage -> ConversationDataDto.SttMessageDto(
        roomId = roomId,
        text = text,
        languageType = languageType
    )
    is Conversation -> ConversationDto(
        id = id,
        roomId = roomId,
        senderInfo = senderInfo.toDto(),
        originText = originText,
        transText = transText,
        transLanguageCode = transLanguage.code,
        timestamp = timestamp
    )
}