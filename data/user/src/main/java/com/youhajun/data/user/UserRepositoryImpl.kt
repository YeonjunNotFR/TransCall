package com.youhajun.data.user

import com.youhajun.core.model.user.MyInfo
import com.youhajun.domain.user.UserRepository
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val remote: UserRemoteDataSource,
    private val local: UserLocalDataSource
): UserRepository {
    override suspend fun getMyInfo(): Result<MyInfo> = runCatching {
        remote.getMyInfo().toModel()
    }
}