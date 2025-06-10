package com.youhajun.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.youhajun.core.datastore.getValue
import com.youhajun.core.network.TokenProvider
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal interface AuthLocalDataSource {
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun deleteTokens()
}

internal class AuthLocalDataSourceImpl @Inject constructor(
    private val data: DataStore<Preferences>
): AuthLocalDataSource, TokenProvider {

    override suspend fun getAccessToken(): String? {
        return data.getValue(stringPreferencesKey(KEY_ACCESS_TOKEN))
    }

    override suspend fun getRefreshToken(): String? {
        return data.getValue(stringPreferencesKey(KEY_REFRESH_TOKEN))
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        data.edit {
            it[stringPreferencesKey(KEY_ACCESS_TOKEN)] = accessToken
            it[stringPreferencesKey(KEY_REFRESH_TOKEN)] = refreshToken
        }
    }

    override suspend fun deleteTokens() {
        data.edit {
            it.remove(stringPreferencesKey(KEY_ACCESS_TOKEN))
            it.remove(stringPreferencesKey(KEY_REFRESH_TOKEN))
        }
    }

    companion object {
        private const val KEY_ACCESS_TOKEN = "AccessToken"
        private const val KEY_REFRESH_TOKEN = "RefreshToken"
    }
}