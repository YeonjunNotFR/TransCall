package com.youhajun.domain.user.usecase

import com.youhajun.core.model.user.MyInfo
import com.youhajun.domain.user.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMyInfoUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): Result<MyInfo> {
        return repository.getMyInfo()
    }
}
