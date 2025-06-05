package com.youhajun.core.model.conversation

import com.youhajun.core.model.LanguageType

sealed interface ConversationMessageType {
    data class SttMessage(
        val roomCode: String,
        val text: String,
        val languageType: LanguageType,
    ): ConversationMessageType
}