package com.youhajun.data.auth

sealed class AuthEndpoint(val path: String) {
    data object SocialLogin : AuthEndpoint("/auth/social-login")
    data object TokenRefresh : AuthEndpoint("/auth/refresh")
    data object Nonce : AuthEndpoint("/auth/nonce")
}