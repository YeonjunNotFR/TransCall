package com.youhajun.core.model;

enum class LanguageType(
    val code: String,
) {
    ENGLISH("en"),
    KOREAN("ko"),
    JAPANESE("ja"),
    CHINESE("zh"),
    SPANISH("es");

    companion object {
        fun fromCode(code: String): LanguageType =
            entries.firstOrNull { it.code == code } ?: ENGLISH
    }
}