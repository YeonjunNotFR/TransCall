package com.youhajun.core.model.conversation

enum class ConversationState(val type: String) {
    PENDING("pending"), FINAL("final");

    companion object {
        fun fromType(type: String?): ConversationState {
            return entries.firstOrNull { it.type == type } ?: FINAL
        }
    }
}