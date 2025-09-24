package com.youhajun.domain.conversation.usecase

import com.youhajun.core.model.TimeRange
import com.youhajun.domain.conversation.ConversationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetConversationLastCursorInTimeRange @Inject constructor(
    private val repository: ConversationRepository
) {
    suspend operator fun invoke(roomId: String, timeRange: TimeRange): String? {
        return repository.getConversationLastCursorInTimeRange(roomId, timeRange)
    }
}