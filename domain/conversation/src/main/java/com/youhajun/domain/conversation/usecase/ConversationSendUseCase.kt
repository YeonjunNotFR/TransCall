package com.youhajun.domain.conversation.usecase

import com.youhajun.core.model.conversation.ConversationMessage
import com.youhajun.core.model.conversation.ConversationMessageType
import com.youhajun.domain.conversation.ConversationRepository
import com.youhajun.domain.user.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConversationSendUseCase @Inject constructor(
    private val repository: ConversationRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(msg: ConversationMessageType) {
        userRepository.getMyInfo().onSuccess {
            ConversationMessage(
                type = msg,
                from = it.userId,
                timestamp = System.currentTimeMillis()
            ).let { msg ->
                repository.send(msg)
            }
        }
    }
}