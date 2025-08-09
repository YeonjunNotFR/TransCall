package com.youhajun.core.model.calling.payload

import com.youhajun.core.model.LanguageType

sealed interface TranslationRequest : MessagePayload, RequestPayload

data class SttMessage(
    val text: String,
    val language: LanguageType
) : TranslationRequest