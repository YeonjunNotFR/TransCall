package com.youhajun.core.model.pagination

data class CursorPagingState(
    val request: CursorPageRequest,
    private val hasNextPage: Boolean = true
) {
    fun isFirstCall(): Boolean = request.after == null

    fun canLoadMore(): Boolean = hasNextPage

    fun next(receivedPage: CursorPage<*>): CursorPagingState =
        CursorPagingState(
            request = CursorPageRequest(after = receivedPage.pageInfo.nextCursor, first = request.first),
            hasNextPage = receivedPage.pageInfo.hasNextPage
        )
}