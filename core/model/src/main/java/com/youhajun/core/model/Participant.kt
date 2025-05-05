package com.youhajun.core.model

data class Participant(
    val userId: String,
    val displayName: String,
    val language: LanguageType,
    val isHost: Boolean,
)