package com.youhajun.data.auth

sealed class AuthEndpoint(val path: String) {
    data object Login : AuthEndpoint("/auth/login")
    data object TokenRefresh : AuthEndpoint("/auth/refresh")
    data object Nonce : AuthEndpoint("/auth/nonce")
}