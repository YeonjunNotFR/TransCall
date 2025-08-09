package com.youhajun.core.model.calling.payload

import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.conversation.SenderInfo

sealed interface TranslationResponse : MessagePayload, ResponsePayload

data object SttStart : TranslationResponse

data class TranslationMessage(
    val conversationId: String,
    val senderInfo: SenderInfo,
    val originText: String,
    val originLanguage: LanguageType,
    val transText: String?,
    val transLanguage: LanguageType,
) : TranslationResponse