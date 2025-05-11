package com.youhajun.domain.history.usecase

import com.youhajun.core.model.CallHistory
import com.youhajun.core.model.pagination.OffsetPage
import com.youhajun.core.model.pagination.OffsetPageRequest
import com.youhajun.domain.history.HistoryRepository
import javax.inject.Inject

class GetHistoryListUseCase @Inject constructor(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke(request: OffsetPageRequest): Result<OffsetPage<CallHistory>> {
        return repository.getHistoryList(request)
    }
}
