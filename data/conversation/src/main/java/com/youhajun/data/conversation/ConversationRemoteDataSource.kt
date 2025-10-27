package com.youhajun.data.conversation

import com.youhajun.core.model.TimeRange
import com.youhajun.core.model.pagination.CursorPageRequest
import com.youhajun.core.network.di.RestHttpClient
import com.youhajun.data.common.dto.pagination.CursorPageDto
import com.youhajun.data.common.parametersFrom
import com.youhajun.data.common.dto.conversation.ConversationResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

internal interface ConversationRemoteDataSource {
    suspend fun getConversationsInTimeRange(
        roomId: String,
        timeRange: TimeRange,
        request: CursorPageRequest,
    ): CursorPageDto<ConversationResponseDto>

    suspend fun getConversationsSyncTimeRange(
        roomId: String,
        timeRange: TimeRange,
        request: CursorPageRequest,
        updatedAfter: Long?
    ): CursorPageDto<ConversationResponseDto>
}

internal class ConversationRemoteDataSourceImpl @Inject constructor(
    @RestHttpClient private val client: HttpClient
): ConversationRemoteDataSource {

    override suspend fun getConversationsInTimeRange(
        roomId: String,
        timeRange: TimeRange,
        request: CursorPageRequest,
    ): CursorPageDto<ConversationResponseDto> {
        return client.get(ConversationEndpoint.List(roomId).path) {
            parametersFrom(request)
            parametersFrom(timeRange)
        }.body()
    }

    override suspend fun getConversationsSyncTimeRange(
        roomId: String,
        timeRange: TimeRange,
        request: CursorPageRequest,
        updatedAfter: Long?,
    ): CursorPageDto<ConversationResponseDto> {
        return client.get(ConversationEndpoint.Sync(roomId).path) {
            parameter("updatedAfter", updatedAfter)
            parametersFrom(request)
            parametersFrom(timeRange)
        }.body()
    }
}