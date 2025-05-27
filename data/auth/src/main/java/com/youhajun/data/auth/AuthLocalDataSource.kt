package com.youhajun.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.youhajun.core.network.TokenProvider
import javax.inject.Inject

internal interface AuthLocalDataSource {
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun saveTokens(accessToken: String, refreshToken: String)
}

internal class AuthLocalDataSourceImpl @Inject constructor(
    private val data: DataStore<Preferences>
): AuthLocalDataSource, TokenProvider {

    override suspend fun getAccessToken(): String? {
        TODO("Not yet implemented")
    }

    override suspend fun getRefreshToken(): String? {
        TODO("Not yet implemented")
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {

    }
}