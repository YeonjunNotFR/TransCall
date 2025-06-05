package com.youhajun.core.model.conversation

import com.youhajun.core.model.LanguageType

data class SenderInfo(
    val senderId: String,
    val displayName: String,
    val language: LanguageType,
    val profileUrl: String? = null,
)