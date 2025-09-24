package com.youhajun.domain.conversation

import androidx.paging.PagingData
import com.youhajun.core.model.TimeRange
import com.youhajun.core.model.calling.payload.TranslationMessage
import com.youhajun.core.model.pagination.CursorPage
import com.youhajun.core.model.pagination.CursorPageRequest
import kotlinx.coroutines.flow.Flow

interface ConversationRepository {

    fun getConversationPagingDataFlow(
        roomId: String,
        timeRange: TimeRange,
        pageSize: Int,
        prefetchDistance: Int,
        initialLoadSize: Int
    ): Flow<PagingData<TranslationMessage>>

    fun getRecentConversationFlow(roomId: String): Flow<TranslationMessage>

    suspend fun getConversationLastSyncAt(roomId: String, joinedAt: Long): Long?

    suspend fun getConversationLastCursorInTimeRange(roomId: String, timeRange: TimeRange): String?

    suspend fun upsertConversation(message: TranslationMessage)

    suspend fun getSyncConversationList(
        roomId: String,
        timeRange: TimeRange,
        request: CursorPageRequest,
        updatedAfter: Long?
    ): Result<CursorPage<TranslationMessage>>

    suspend fun updateConversationLastSyncAt(roomId: String, joinedAt: Long, updatedAt: Long)
}