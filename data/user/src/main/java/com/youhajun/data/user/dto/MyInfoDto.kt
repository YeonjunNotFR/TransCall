package com.youhajun.data.user.dto

import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.MembershipPlan
import com.youhajun.core.model.user.MyInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MyInfoDto(
    @SerialName("userId")
    val userId: String = "",
    @SerialName("displayName")
    val displayName: String = "",
    @SerialName("language")
    val language: String = "",
    @SerialName("membershipPlan")
    val membershipPlan: String = "",
    @SerialName("remainTime")
    val remainTime: RemainTimeDto = RemainTimeDto(),
    @SerialName("imageUrl")
    val imageUrl: String? = null,
) {
    fun toModel(): MyInfo = MyInfo(
        userId = userId,
        displayName = displayName,
        imageUrl = imageUrl ?: "",
        language = LanguageType.fromCode(language),
        membershipPlan = MembershipPlan.fromId(membershipPlan),
        remainTime = remainTime.toModel(),
    )
}