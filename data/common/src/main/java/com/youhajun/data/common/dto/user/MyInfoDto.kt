package com.youhajun.data.common.dto.user

import com.youhajun.core.model.CountryType
import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.MembershipPlan
import com.youhajun.core.model.user.MyInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyInfoDto(
    @SerialName("userId")
    val userId: String = "",
    @SerialName("displayName")
    val displayName: String = "",
    @SerialName("languageCode")
    val languageCode: String = "",
    @SerialName("countryCode")
    val countryCode: String = "",
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
        language = LanguageType.fromCode(languageCode),
        country = CountryType.fromCode(countryCode),
        membershipPlan = MembershipPlan.fromId(membershipPlan),
        remainTime = remainTime.toModel(),
    )
}