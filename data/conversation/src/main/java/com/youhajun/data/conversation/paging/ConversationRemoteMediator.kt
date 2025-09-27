package com.youhajun.data.conversation.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.youhajun.core.database.TransactionProvider
import com.youhajun.core.database.entity.ConversationEntity
import com.youhajun.core.model.TimeRange
import com.youhajun.core.model.calling.payload.TranslationMessage
import com.youhajun.core.model.pagination.CursorPage
import com.youhajun.core.model.pagination.CursorPageRequest
import com.youhajun.data.conversation.ConversationLocalDataSource
import com.youhajun.data.conversation.ConversationRemoteDataSource
import com.youhajun.data.conversation.dto.toCursorEntities
import com.youhajun.data.conversation.dto.toEntities
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

@OptIn(ExperimentalPagingApi::class)
internal class ConversationRemoteMediator @AssistedInject constructor(
    private val remote: ConversationRemoteDataSource,
    private val local: ConversationLocalDataSource,
    @Assisted private val roomId: String,
    @Assisted private val timeRange: TimeRange,
    private val transactionProvider: TransactionProvider
) : RemoteMediator<Int, ConversationEntity>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ConversationEntity>
    ): MediatorResult = try {
        val after: String? = when (loadType) {
            LoadType.REFRESH -> null
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> transactionProvider.runAsTransaction {
                state.lastItemOrNull()?.id?.let {
                    local.getConversationCursor(it)?.historyCursor
                }
            } ?: return MediatorResult.Success(endOfPaginationReached = false)
        }

        val first = state.config.pageSize
        val response = getConversationsInTimeRange(first, after)
        transactionProvider.runAsTransaction {
            local.upsertAllConversations(response.toEntities())
            local.upsertAllConversationCursor(response.toCursorEntities())
        }
        val endOfPagination = !response.pageInfo.hasNextPage || response.edges.isEmpty()
        MediatorResult.Success(endOfPaginationReached = endOfPagination)
    } catch (e: Throwable) {
        MediatorResult.Error(e)
    }

    private suspend fun getConversationsInTimeRange(
        first: Int,
        after: String?,
    ): CursorPage<TranslationMessage> {
        return remote.getConversationsInTimeRange(
            roomId = roomId,
            timeRange = timeRange,
            request = CursorPageRequest(first = first, after = after),
        ).toModel { it.toModel() }
    }

    @AssistedFactory
    interface Factory {
        fun create(roomId: String, timeRange: TimeRange): ConversationRemoteMediator
    }
}
