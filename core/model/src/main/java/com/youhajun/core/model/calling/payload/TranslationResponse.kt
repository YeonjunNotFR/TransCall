package com.youhajun.core.model.calling.payload

import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.conversation.ConversationState

sealed interface TranslationResponse : ResponsePayload

data object SttStart : TranslationResponse

data class TranslationMessage(
    val conversationId: String,
    val roomId: String,
    val senderId: String,
    val state: ConversationState,
    val originText: String,
    val originLanguage: LanguageType,
    val transText: String?,
    val transLanguage: LanguageType?,
    val updatedAtToEpochTime: Long,
    val createdAtToEpochTime: Long
) : TranslationResponse