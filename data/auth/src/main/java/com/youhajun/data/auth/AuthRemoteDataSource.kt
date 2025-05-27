package com.youhajun.data.auth

import com.youhajun.core.network.di.RestHttpClient
import io.ktor.client.HttpClient
import javax.inject.Inject

internal interface AuthRemoteDataSource {
    suspend fun login()
    suspend fun logout()
}

internal class AuthRemoteDataSourceImpl @Inject constructor(
    @RestHttpClient private val client: HttpClient
): AuthRemoteDataSource {

    override suspend fun login() {
        TODO("Not yet implemented")
    }

    override suspend fun logout() {
        TODO("Not yet implemented")
    }
}