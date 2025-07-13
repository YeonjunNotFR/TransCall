package com.youhajun.data.auth

sealed class AuthEndpoint(val path: String) {
    data object SocialLogin : AuthEndpoint("api/auth/social-login")
    data object TokenRefresh : AuthEndpoint("api/auth/reissue")
    data object Nonce : AuthEndpoint("api/auth/nonce")
}