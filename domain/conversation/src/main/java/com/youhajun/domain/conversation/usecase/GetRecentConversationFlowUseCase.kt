package com.youhajun.domain.conversation.usecase

import com.youhajun.core.model.calling.payload.TranslationMessage
import com.youhajun.domain.conversation.ConversationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetRecentConversationFlowUseCase @Inject constructor(
    private val repository: ConversationRepository
) {
    operator fun invoke(roomId: String): Flow<TranslationMessage> {
        return repository.getRecentConversationFlow(roomId)
    }
}