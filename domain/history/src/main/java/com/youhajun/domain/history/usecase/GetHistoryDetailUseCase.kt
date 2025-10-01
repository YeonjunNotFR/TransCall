package com.youhajun.domain.history.usecase

import com.youhajun.core.model.history.CallHistory
import com.youhajun.domain.history.HistoryRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetHistoryDetailUseCase @Inject constructor(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke(historyId: String): Result<CallHistory> {
        return repository.getHistoryDetail(historyId)
    }
}
