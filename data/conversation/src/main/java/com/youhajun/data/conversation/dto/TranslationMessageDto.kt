package com.youhajun.data.conversation.dto

import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.calling.payload.TranslationMessage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
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
) {
    fun toModel(): TranslationMessage = TranslationMessage(
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