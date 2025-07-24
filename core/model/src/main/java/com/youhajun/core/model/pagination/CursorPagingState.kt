package com.youhajun.core.model.pagination

data class CursorPagingState(
    val request: CursorPageRequest,
    private val isLastPage: Boolean = false
) {
    fun isFirstCall(): Boolean = request.after == null

    fun canLoadMore(): Boolean = !isLastPage

    fun next(receivedPage: CursorPage<*>): CursorPagingState =
        CursorPagingState(
            request = CursorPageRequest(after = receivedPage.pageInfo.nextCursor, first = request.first),
            isLastPage = receivedPage.pageInfo.hasNextPage
        )
}