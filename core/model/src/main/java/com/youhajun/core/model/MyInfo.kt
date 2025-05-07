package com.youhajun.core.model

data class MyInfo(
    val userId: String,
    val displayName: String,
    val language: LanguageType,
    val membershipPlan: MembershipPlan,
    val remainTime: RemainTime,
    val imageUrl: String? = null
)