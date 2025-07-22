package com.youhajun.data.history

import com.youhajun.core.model.calling.CallHistory
import com.youhajun.core.model.filter.DateRangeFilter
import com.youhajun.core.model.pagination.CursorPage
import com.youhajun.core.model.pagination.CursorPageRequest
import com.youhajun.domain.history.HistoryRepository
import javax.inject.Inject

internal class HistoryRepositoryImpl @Inject constructor(
    private val remote: HistoryRemoteDataSource
) : HistoryRepository {

    override suspend fun getHistoryList(
        request: CursorPageRequest,
        range: DateRangeFilter?
    ): Result<CursorPage<CallHistory>> = runCatching {
        remote.getHistoryList(request, range).toModel { it.toModel() }
    }

    override suspend fun getHistoryDetail(callId: String) = runCatching {
        remote.getHistoryDetail(callId).toModel()
    }

    override suspend fun deleteHistory(callId: String) = runCatching {
        remote.deleteHistory(callId)
    }
}