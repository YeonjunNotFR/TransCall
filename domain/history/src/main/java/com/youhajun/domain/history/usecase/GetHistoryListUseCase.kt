package com.youhajun.domain.history.usecase

import com.youhajun.core.model.CallHistory
import com.youhajun.domain.history.HistoryRepository
import javax.inject.Inject

class GetHistoryListUseCase @Inject constructor(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke(): Result<List<CallHistory>> {
        return repository.getHistoryList()
    }
}
