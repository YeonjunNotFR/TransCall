package com.youhajun.data.user

import com.youhajun.core.network.di.RestHttpClient
import com.youhajun.data.common.dto.user.MyInfoDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

internal interface UserRemoteDataSource {
    suspend fun getMyInfo(): MyInfoDto
}

internal class UserRemoteDataSourceImpl @Inject constructor(
    @RestHttpClient private val client: HttpClient
): UserRemoteDataSource {

    override suspend fun getMyInfo(): MyInfoDto {
        return client.get(UserEndpoint.MyInfo.path).body()
    }
}