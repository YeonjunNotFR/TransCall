package com.youhajun.domain.conversation.usecase

import androidx.paging.PagingData
import com.youhajun.core.model.TimeRange
import com.youhajun.core.model.calling.payload.TranslationMessage
import com.youhajun.domain.conversation.ConversationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetConversationPagingDataFlowUseCase @Inject constructor(
    private val repository: ConversationRepository
) {
    operator fun invoke(
        roomId: String,
        timeRange: TimeRange,
        pageSize: Int,
        prefetchDistance: Int,
        initialLoadSize: Int
    ): Flow<PagingData<TranslationMessage>> {
        return repository.getConversationPagingDataFlow(roomId, timeRange, pageSize, prefetchDistance, initialLoadSize)
    }
}