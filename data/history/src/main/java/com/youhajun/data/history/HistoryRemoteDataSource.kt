package com.youhajun.data.history

import com.youhajun.core.model.filter.DateRangeFilter
import com.youhajun.core.model.pagination.OffsetPageRequest
import com.youhajun.data.common.pagination.OffsetPageDto
import com.youhajun.data.common.parametersFrom
import com.youhajun.data.history.dto.CallHistoryDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import javax.inject.Inject

internal interface HistoryRemoteDataSource {
    suspend fun getHistoryList(request: OffsetPageRequest, range: DateRangeFilter?): OffsetPageDto<CallHistoryDto>
    suspend fun getHistoryDetail(callId: String): CallHistoryDto
    suspend fun deleteHistory(callId: String)
}

internal class HistoryRemoteDataSourceImpl @Inject constructor(
    private val client: HttpClient
): HistoryRemoteDataSource {

    override suspend fun getHistoryList(request: OffsetPageRequest, range: DateRangeFilter?): OffsetPageDto<CallHistoryDto> {
        return client.get(HistoryEndpoint.List.path) {
            parametersFrom(request)
            range?.let { parametersFrom(it) }
        }.body()
    }

    override suspend fun getHistoryDetail(callId: String): CallHistoryDto {
        return client.get(HistoryEndpoint.Detail(callId).path).body()
    }

    override suspend fun deleteHistory(callId: String) {
        return client.delete(HistoryEndpoint.Delete(callId).path).body()
    }
}