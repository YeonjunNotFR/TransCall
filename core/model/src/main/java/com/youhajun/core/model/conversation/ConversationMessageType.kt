package com.youhajun.core.model.conversation

import com.youhajun.core.model.LanguageType

sealed interface ConversationMessageType {
    data class SttMessage(
        val roomId: String,
        val text: String,
        val languageType: LanguageType,
    ): ConversationMessageType
}