package com.youhajun.data.common

import com.youhajun.core.model.pagination.OffsetPageRequest
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.parameter

fun HttpRequestBuilder.parametersFrom(request: OffsetPageRequest) {
    parameter("offset", request.offset)
    parameter("limit", request.limit)
}