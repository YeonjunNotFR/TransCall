package com.youhajun.data.history

import com.youhajun.core.model.filter.DateRangeFilter
import com.youhajun.core.model.pagination.CursorPageRequest
import com.youhajun.core.network.di.RestHttpClient
import com.youhajun.data.common.dto.pagination.CursorPageDto
import com.youhajun.data.common.parametersFrom
import com.youhajun.data.common.dto.history.CallHistoryDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import javax.inject.Inject

internal interface HistoryRemoteDataSource {
    suspend fun getHistoryList(
        request: CursorPageRequest,
        range: DateRangeFilter?
    ): CursorPageDto<CallHistoryDto>

    suspend fun getHistoryDetail(historyId: String): CallHistoryDto
    suspend fun deleteHistory(historyId: String)
}

internal class HistoryRemoteDataSourceImpl @Inject constructor(
    @RestHttpClient private val client: HttpClient
) : HistoryRemoteDataSource {

    override suspend fun getHistoryList(
        request: CursorPageRequest,
        range: DateRangeFilter?
    ): CursorPageDto<CallHistoryDto> {
        return client.get(HistoryEndpoint.List.path) {
            parametersFrom(request)
            range?.let { parametersFrom(it) }
        }.body()
    }

    override suspend fun getHistoryDetail(historyId: String): CallHistoryDto {
        return client.get(HistoryEndpoint.Detail(historyId).path).body()
    }

    override suspend fun deleteHistory(historyId: String) {
        return client.delete(HistoryEndpoint.Delete(historyId).path).body()
    }
}