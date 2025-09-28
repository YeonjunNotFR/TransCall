package com.youhajun.data.conversation

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.youhajun.core.database.TransactionProvider
import com.youhajun.core.model.TimeRange
import com.youhajun.core.model.calling.payload.TranslationMessage
import com.youhajun.core.model.pagination.CursorPage
import com.youhajun.core.model.pagination.CursorPageRequest
import com.youhajun.data.conversation.dto.toCursorEntities
import com.youhajun.data.conversation.dto.toEntities
import com.youhajun.data.conversation.dto.toEntity
import com.youhajun.data.conversation.dto.toModel
import com.youhajun.data.conversation.paging.ConversationRemoteMediator
import com.youhajun.domain.conversation.ConversationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

internal class ConversationRepositoryImpl @Inject constructor(
    private val local: ConversationLocalDataSource,
    private val remote: ConversationRemoteDataSource,
    private val transactionProvider: TransactionProvider,
    private val remoteMediatorFactory: ConversationRemoteMediator.Factory
) : ConversationRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getConversationPagingDataFlow(
        roomId: String,
        timeRange: TimeRange,
        pageSize: Int,
        prefetchDistance: Int,
        initialLoadSize: Int
    ): Flow<PagingData<TranslationMessage>> = Pager(
        config = PagingConfig(
            pageSize = pageSize,
            enablePlaceholders = true,
            prefetchDistance = prefetchDistance,
            initialLoadSize = initialLoadSize,
        ),
        remoteMediator = remoteMediatorFactory.create(roomId, timeRange),
        pagingSourceFactory = { local.pagingSourceInTimeRange(roomId, timeRange) }
    ).flow.map { it.map { it.toModel() } }

    override fun getRecentConversationFlow(roomId: String): Flow<TranslationMessage> =
        local.getRecentConversationFlow(roomId).mapNotNull { it?.toModel() }

    override suspend fun upsertConversation(message: TranslationMessage) =
        local.upsertConversation(message.toEntity())

    override suspend fun getConversationLastSyncAt(roomId: String, joinedAt: Long): Long? =
        local.getConversationMeta(roomId, joinedAt)?.lastSyncedAt

    override suspend fun getConversationLastCursorInTimeRange(roomId: String, timeRange: TimeRange): String? =
        local.getConversationLastCursorInTimeRange(roomId, timeRange)?.historyCursor

    override suspend fun updateConversationLastSyncAt(roomId: String, joinedAt: Long, updatedAt: Long) =
        local.updateConversationLastSyncAt(roomId, joinedAt, updatedAt)

    override suspend fun getSyncConversationList(
        roomId: String,
        timeRange: TimeRange,
        request: CursorPageRequest,
        updatedAfter: Long?
    ): Result<CursorPage<TranslationMessage>> = runCatching {
        remote.getConversationsSyncTimeRange(roomId, timeRange, request, updatedAfter).toModel { it.toModel() }
    }.onSuccess {
        transactionProvider.runAsTransaction {
            local.upsertAllConversations(it.toEntities())
            local.upsertAllConversationCursor(it.toCursorEntities())
        }
    }
}