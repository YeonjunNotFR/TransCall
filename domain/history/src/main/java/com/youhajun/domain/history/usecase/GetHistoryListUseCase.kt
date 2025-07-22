package com.youhajun.domain.history.usecase

import com.youhajun.core.model.calling.CallHistory
import com.youhajun.core.model.filter.DateRangeFilter
import com.youhajun.core.model.pagination.CursorPage
import com.youhajun.core.model.pagination.CursorPageRequest
import com.youhajun.domain.history.HistoryRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetHistoryListUseCase @Inject constructor(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke(
        request: CursorPageRequest,
        range: DateRangeFilter? = null
    ): Result<CursorPage<CallHistory>> {
        return repository.getHistoryList(request, range)
    }
}
