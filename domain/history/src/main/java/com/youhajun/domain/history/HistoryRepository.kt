package com.youhajun.domain.history

import com.youhajun.core.model.calling.CallHistory
import com.youhajun.core.model.filter.DateRangeFilter
import com.youhajun.core.model.pagination.OffsetPage
import com.youhajun.core.model.pagination.OffsetPageRequest

interface HistoryRepository {
    suspend fun getHistoryList(request: OffsetPageRequest, range: DateRangeFilter?): Result<OffsetPage<CallHistory>>
    suspend fun getHistoryDetail(callId: String): Result<CallHistory>
    suspend fun deleteHistory(callId: String): Result<Unit>
}