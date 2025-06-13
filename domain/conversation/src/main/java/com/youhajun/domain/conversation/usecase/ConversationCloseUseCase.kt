package com.youhajun.domain.conversation.usecase

import com.youhajun.domain.conversation.ConversationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConversationCloseUseCase @Inject constructor(
    private val repository: ConversationRepository
) {
    suspend operator fun invoke() {
        repository.close()
    }
}