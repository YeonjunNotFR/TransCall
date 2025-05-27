package com.youhajun.core.model.user

import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.MembershipPlan
import com.youhajun.core.model.RemainTime

data class MyInfo(
    val userId: String,
    val displayName: String = "",
    val language: LanguageType = LanguageType.ENGLISH,
    val membershipPlan: MembershipPlan = MembershipPlan.Free,
    val remainTime: RemainTime = RemainTime(),
    val imageUrl: String? = null
)