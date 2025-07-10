package com.youhajun.data.auth

sealed class AuthEndpoint(val path: String) {
    data object SocialLogin : AuthEndpoint("api/auth/social-login")
    data object TokenRefresh : AuthEndpoint("api/auth/refresh")
    data object Nonce : AuthEndpoint("api/auth/nonce")
}