package com.youhajun.core.model.room

import com.youhajun.core.model.CountryType
import com.youhajun.core.model.LanguageType

data class Participant(
    val participantId: String,
    val userId: String = "",
    val displayName: String = "",
    val imageUrl: String = "",
    val language: LanguageType = LanguageType.ENGLISH,
    val country: CountryType = CountryType.UNITED_STATES,
    val leftAtToEpochTime: Long? = null
)