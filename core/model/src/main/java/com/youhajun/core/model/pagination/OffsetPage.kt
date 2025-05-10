package com.youhajun.core.model.pagination

data class OffsetPage<T>(
    val data: List<T>,
    val offset: Int,
    val limit: Int,
    val total: Int?,
    val hasMore: Boolean?
) {
    fun isLastPage(): Boolean = hasMore == false || data.isEmpty()

    fun nextRequest(): OffsetPageRequest = OffsetPageRequest(offset = offset + data.size, limit = limit)
}