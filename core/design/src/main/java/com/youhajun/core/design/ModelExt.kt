package com.youhajun.core.design

import com.youhajun.core.model.LanguageType

fun LanguageType.toDisplayName(): Int {
    return when (this) {
        LanguageType.ENGLISH -> R.string.language_english
        LanguageType.KOREAN -> R.string.language_korean
        LanguageType.JAPANESE -> R.string.language_japanese
        LanguageType.CHINESE -> R.string.language_chinese
        LanguageType.SPANISH -> R.string.language_spanish
    }
}

fun LanguageType.toDisplayIcon(): Int {
    return when (this) {
        LanguageType.ENGLISH -> R.drawable.ic_call_left
        LanguageType.KOREAN -> R.drawable.ic_call_left
        LanguageType.JAPANESE -> R.drawable.ic_call_left
        LanguageType.CHINESE -> R.drawable.ic_call_left
        LanguageType.SPANISH -> R.drawable.ic_call_left
    }
}