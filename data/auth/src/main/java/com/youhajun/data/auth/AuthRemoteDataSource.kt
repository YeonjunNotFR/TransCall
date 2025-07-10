package com.youhajun.data.auth

import com.youhajun.core.network.di.RestHttpClient
import com.youhajun.data.auth.dto.JwtTokenDto
import com.youhajun.data.auth.dto.SocialLoginRequestDto
import com.youhajun.data.auth.dto.NonceDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

internal interface AuthRemoteDataSource {
    suspend fun socialLogin(request: SocialLoginRequestDto): JwtTokenDto
    suspend fun getLoginNonce(): NonceDto
}

internal class AuthRemoteDataSourceImpl @Inject constructor(
    @RestHttpClient private val client: HttpClient
) : AuthRemoteDataSource {

    override suspend fun socialLogin(request: SocialLoginRequestDto): JwtTokenDto {
        return client.post(AuthEndpoint.SocialLogin.path) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun getLoginNonce(): NonceDto {
        return client.get(AuthEndpoint.Nonce.path).body()
    }
}