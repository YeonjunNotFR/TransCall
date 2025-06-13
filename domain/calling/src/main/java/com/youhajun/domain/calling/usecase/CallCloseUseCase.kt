package com.youhajun.domain.calling.usecase

import com.youhajun.domain.calling.CallingRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CallCloseUseCase @Inject constructor(
    private val repository: CallingRepository
) {
    suspend operator fun invoke() {
        repository.close()
    }
}