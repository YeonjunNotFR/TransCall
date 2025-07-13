package com.youhajun.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.youhajun.core.datastore.getValue
import javax.inject.Inject
import javax.inject.Singleton

internal interface AuthLocalDataSource {
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun deleteTokens()
}

@Singleton
internal class AuthLocalDataSourceImpl @Inject constructor(
    private val data: DataStore<Preferences>,
): AuthLocalDataSource {

    override suspend fun getAccessToken(): String? {
        return data.getValue(KEY_ACCESS_TOKEN)
    }

    override suspend fun getRefreshToken(): String? {
        return data.getValue(KEY_REFRESH_TOKEN)
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        data.edit {
            it[KEY_ACCESS_TOKEN] = accessToken
            it[KEY_REFRESH_TOKEN] = refreshToken
        }
    }

    override suspend fun deleteTokens() {
        data.edit {
            it.remove(KEY_ACCESS_TOKEN)
            it.remove(KEY_REFRESH_TOKEN)
        }
    }

    companion object {
        private val KEY_ACCESS_TOKEN =  stringPreferencesKey("AccessToken")
        private val KEY_REFRESH_TOKEN = stringPreferencesKey("RefreshToken")
    }
}