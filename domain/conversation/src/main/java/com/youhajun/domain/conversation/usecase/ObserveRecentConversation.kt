package com.youhajun.domain.conversation.usecase

import com.youhajun.core.model.conversation.Conversation
import com.youhajun.domain.conversation.ConversationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObserveRecentConversation @Inject constructor(
    private val repository: ConversationRepository
) {
    operator fun invoke(roomId: String): Flow<Conversation> {
        return repository.observeRecentConversation(roomId)
    }
}