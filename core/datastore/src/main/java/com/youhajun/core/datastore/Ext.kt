package com.youhajun.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

suspend fun <T> DataStore<Preferences>.getValue(key: Preferences.Key<T>): T? {
    return data.catch { emit(emptyPreferences()) }.map { it[key] }.firstOrNull()
}