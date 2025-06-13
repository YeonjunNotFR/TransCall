package com.youhajun.domain.calling.usecase

import com.youhajun.core.model.calling.CallingMessage
import com.youhajun.core.model.calling.CallingMessageType
import com.youhajun.domain.calling.CallingRepository
import com.youhajun.domain.user.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CallingSendUseCase @Inject constructor(
    private val repository: CallingRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(msg: CallingMessageType) {
        userRepository.getMyInfo().onSuccess {
            CallingMessage(
                type = msg,
                from = it.userId,
                timestamp = System.currentTimeMillis()
            ).let { msg ->
                repository.send(msg)
            }
        }
    }
}