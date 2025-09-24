package com.youhajun.domain.conversation.usecase

import com.youhajun.core.model.TimeRange
import com.youhajun.core.model.calling.payload.TranslationMessage
import com.youhajun.core.model.pagination.CursorPage
import com.youhajun.core.model.pagination.CursorPageRequest
import com.youhajun.domain.conversation.ConversationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSyncConversationList @Inject constructor(
    private val repository: ConversationRepository
) {
    suspend operator fun invoke(
        roomId: String,
        timeRange: TimeRange,
        request: CursorPageRequest,
        updatedAfter: Long?
    ): Result<CursorPage<TranslationMessage>> {
        return repository.getSyncConversationList(roomId, timeRange, request, updatedAfter)
    }
}