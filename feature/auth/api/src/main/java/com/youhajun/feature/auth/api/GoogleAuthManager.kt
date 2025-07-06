package com.youhajun.feature.auth.api

interface GoogleAuthManager {
    suspend fun signIn(nonce: String): Result<String>
    suspend fun signOut(): Result<Unit>
}