package com.youhajun.domain.conversation.usecase

import com.youhajun.core.model.calling.payload.TranslationMessage
import com.youhajun.domain.conversation.ConversationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpsertConversationUseCase @Inject constructor(
    private val repository: ConversationRepository
) {
    suspend operator fun invoke(message: TranslationMessage) {
        return repository.upsertConversation(message)
    }
}