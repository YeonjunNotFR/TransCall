package com.youhajun.feature.history.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.youhajun.core.model.TimeRange
import com.youhajun.core.model.pagination.CursorPageRequest
import com.youhajun.domain.conversation.usecase.GetConversationLastCursorInTimeRange
import com.youhajun.domain.conversation.usecase.GetConversationLastSyncAt
import com.youhajun.domain.conversation.usecase.GetSyncConversationList
import com.youhajun.domain.conversation.usecase.UpdateConversationLastSyncAt
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.Instant

@HiltWorker
class ConversationSyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val getConversationLastCursorInTimeRange: GetConversationLastCursorInTimeRange,
    private val getConversationLastSyncAt: GetConversationLastSyncAt,
    private val getSyncConversationList: GetSyncConversationList,
    private val updateConversationLastSyncAt: UpdateConversationLastSyncAt,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = runCatching {
        val roomId = params.inputData.getString(KEY_ROOM_ID) ?: return Result.failure()
        val joinedAt = params.inputData.getLong(KEY_JOINED_AT, -1L).takeIf { it > 0L } ?: return Result.failure()
        val leftAt = params.inputData.getLong(KEY_LEFT_AT, -1L).takeIf { it > 0L } ?: return Result.failure()
        val timeRange = TimeRange(joinedAtToEpochTime = joinedAt, leftAtToEpochTime = leftAt)

        val lastCursor = getConversationLastCursorInTimeRange(roomId, timeRange) ?: return Result.success()
        val lastSyncedAt = getConversationLastSyncAt(roomId, joinedAt)

        var cursor: String? = lastCursor
        var hasNextPage = true
        var newestUpdatedAt = lastSyncedAt ?: Instant.now().epochSecond

        while (hasNextPage) {
            val request = CursorPageRequest(first = SYNC_CONVERSATION_SIZE_PER_PAGE, after = cursor)

            getSyncConversationList(roomId, timeRange, request, lastSyncedAt).onSuccess {
                val updatedAtMax = it.edges.maxOfOrNull { it.node.updatedAtToEpochTime } ?: newestUpdatedAt
                newestUpdatedAt = maxOf(newestUpdatedAt, updatedAtMax)
                cursor = it.pageInfo.nextCursor
                hasNextPage = it.pageInfo.hasNextPage && it.edges.isNotEmpty()
            }.onFailure {
                return Result.retry()
            }
        }

        updateConversationLastSyncAt(roomId, joinedAt, newestUpdatedAt)

        Result.success()
    }.getOrElse {
        Result.retry()
    }

    companion object {
        const val KEY_ROOM_ID = "roomId"
        const val KEY_JOINED_AT = "joinedAt"
        const val KEY_LEFT_AT = "leftAt"
        private const val SYNC_CONVERSATION_SIZE_PER_PAGE = 50
    }
}
