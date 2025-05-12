package com.youhajun.core.model.pagination

data class OffsetPagingState(
    val request: OffsetPageRequest,
    private val isLastPage: Boolean
) {
    fun isFirstCall(): Boolean = request.offset == 0

    fun canLoadMore(): Boolean = !isLastPage

    fun next(receivedPage: OffsetPage<*>): OffsetPagingState =
        OffsetPagingState(
            request = receivedPage.nextRequest(),
            isLastPage = receivedPage.isLastPage()
        )
}