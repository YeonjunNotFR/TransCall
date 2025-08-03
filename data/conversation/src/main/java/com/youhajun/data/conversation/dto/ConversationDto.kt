package com.youhajun.data.conversation.dto

import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.conversation.Conversation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("conversation")
internal data class ConversationDto(
    @SerialName("id")
    val id: String,
    @SerialName("roomId")
    val roomId: String,
    @SerialName("senderInfo")
    val senderInfo: SenderInfoDto,
    @SerialName("originText")
    val originText: String,
    @SerialName("transText")
    val transText: String?,
    @SerialName("transLanguageCode")
    val transLanguageCode: String,
    @SerialName("timestamp")
    val timestamp: Long,
): ConversationDataDto {
    override fun toModel(): Conversation = Conversation(
        id = id,
        roomId = roomId,
        senderInfo = senderInfo.toModel(),
        originText = originText,
        transText = transText,
        transLanguage = LanguageType.fromCode(transLanguageCode),
        timestamp = timestamp
    )
}