package com.youhajun.domain.history

import com.youhajun.core.model.history.CallHistory
import com.youhajun.core.model.calling.payload.TranslationMessage
import com.youhajun.core.model.filter.DateRangeFilter
import com.youhajun.core.model.pagination.CursorPage
import com.youhajun.core.model.pagination.CursorPageRequest

interface HistoryRepository {
    suspend fun getHistoryList(request: CursorPageRequest, range: DateRangeFilter?): Result<CursorPage<CallHistory>>
    suspend fun getHistoryDetail(historyId: String): Result<CallHistory>
    suspend fun deleteHistory(historyId: String): Result<Unit>
}