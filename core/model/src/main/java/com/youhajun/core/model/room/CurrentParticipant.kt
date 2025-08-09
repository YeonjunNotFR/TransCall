package com.youhajun.core.model.room

import com.youhajun.core.model.LanguageType

data class CurrentParticipant(
    val userId: String,
    val displayName: String,
    val imageUrl: String,
    val language: LanguageType,
)