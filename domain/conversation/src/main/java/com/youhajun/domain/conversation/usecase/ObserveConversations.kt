package com.youhajun.domain.conversation.usecase

import com.youhajun.core.model.conversation.Conversation
import com.youhajun.domain.conversation.ConversationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObserveConversations @Inject constructor(
    private val repository: ConversationRepository
) {
    operator fun invoke(roomCode: String, afterTimestamp: Long): Flow<List<Conversation>> {
        return repository.observeConversations(roomCode, afterTimestamp)
    }
}