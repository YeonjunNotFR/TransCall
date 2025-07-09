package com.youhajun.data.auth.dto

import com.youhajun.core.model.auth.SocialLoginRequest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SocialLoginRequestDto(
    @SerialName("loginRequestId")
    val loginRequestId: String,
    @SerialName("socialType")
    val socialType: String,
    @SerialName("token")
    val token: String
)

internal fun SocialLoginRequest.toDto(): SocialLoginRequestDto {
    return SocialLoginRequestDto(
        loginRequestId = loginRequestId,
        socialType = socialType.type,
        token = token
    )
}