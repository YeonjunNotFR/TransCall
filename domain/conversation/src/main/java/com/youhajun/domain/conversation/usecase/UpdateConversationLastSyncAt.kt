package com.youhajun.domain.conversation.usecase

import com.youhajun.domain.conversation.ConversationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateConversationLastSyncAt @Inject constructor(
    private val repository: ConversationRepository
) {
    suspend operator fun invoke(roomId: String, joinedAt: Long, updatedAt: Long) {
        repository.updateConversationLastSyncAt(roomId, joinedAt, updatedAt)
    }
}