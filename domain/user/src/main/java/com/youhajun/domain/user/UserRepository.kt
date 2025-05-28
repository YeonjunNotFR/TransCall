package com.youhajun.domain.user

import com.youhajun.core.model.user.MyInfo

interface UserRepository {
    suspend fun getMyInfo(): Result<MyInfo>
}