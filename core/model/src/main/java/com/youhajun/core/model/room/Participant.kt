package com.youhajun.core.model.room

import com.youhajun.core.model.LanguageType

data class Participant(
    val userId: String,
    val displayName: String,
    val imageUrl: String,
    val language: LanguageType,
)