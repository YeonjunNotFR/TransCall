package com.youhajun.core.model.auth

data class LoginRequest(
    val socialType: SocialType,
    val authCode: String
)