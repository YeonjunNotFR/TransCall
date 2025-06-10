package com.youhajun.data.auth.dto

import com.youhajun.core.model.auth.LoginRequest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class LoginRequestDto(
    @SerialName("socialType")
    val socialType: String,
    @SerialName("authCode")
    val authCode: String
)

internal fun LoginRequest.toDto(): LoginRequestDto {
    return LoginRequestDto(
        socialType = socialType.type,
        authCode = authCode
    )
}