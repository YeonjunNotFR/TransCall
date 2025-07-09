package com.youhajun.core.model.auth

data class SocialLoginRequest(
    val loginRequestId: String,
    val socialType: SocialType,
    val token: String
)