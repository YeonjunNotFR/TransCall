package com.youhajun.data.auth

import com.youhajun.core.network.di.RestHttpClient
import com.youhajun.data.auth.dto.JwtTokenDto
import com.youhajun.data.auth.dto.LoginRequestDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import javax.inject.Inject

internal interface AuthRemoteDataSource {
    suspend fun login(request: LoginRequestDto): JwtTokenDto
    suspend fun getLoginNonce(): String
}

internal class AuthRemoteDataSourceImpl @Inject constructor(
    @RestHttpClient private val client: HttpClient
) : AuthRemoteDataSource {

    override suspend fun login(request: LoginRequestDto): JwtTokenDto {
        return client.post(AuthEndpoint.Login.path) {
            setBody(request)
        }.body()
    }

    override suspend fun getLoginNonce(): String {
        return client.get(AuthEndpoint.Nonce.path).body()
    }
}