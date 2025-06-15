package com.youhajun.data.conversation.dto

import com.youhajun.core.model.conversation.ConversationMessage
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ConversationMessageDto(
    @Polymorphic
    @SerialName("data")
    val data: ConversationDataDto,
    @SerialName("from")
    val from: String,
    @SerialName("timestamp")
    val timestamp: Long,
) {
    fun toModel(): ConversationMessage = ConversationMessage(
        type = data.toModel(),
        from = from,
        timestamp = timestamp
    )
}

internal fun ConversationMessage.toDto(): ConversationMessageDto = ConversationMessageDto(
    data = type.toDto(),
    from = from,
    timestamp = timestamp
)