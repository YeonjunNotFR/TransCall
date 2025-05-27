package com.youhajun.data.auth

sealed class AuthEndpoint(val path: String) {
    data object TokenRefresh : AuthEndpoint("/auth/refresh")
}