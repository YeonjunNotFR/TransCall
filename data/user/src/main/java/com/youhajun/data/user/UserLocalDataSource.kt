package com.youhajun.data.user

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import javax.inject.Inject

internal interface UserLocalDataSource {

}

internal class UserLocalDataSourceImpl @Inject constructor(
    private val data: DataStore<Preferences>
): UserLocalDataSource {

}