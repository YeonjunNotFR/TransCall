package com.youhajun.core.model

data class MyInfo(
    val userId: String,
    val displayName: String = "",
    val language: LanguageType = LanguageType.ENGLISH,
    val membershipPlan: MembershipPlan = MembershipPlan.Free,
    val remainTime: RemainTime = RemainTime(),
    val imageUrl: String? = null
)