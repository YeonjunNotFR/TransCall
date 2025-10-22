package com.youhajun.transcall.core.ui.components.paging

import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState

@Composable
fun PagingComp(
    state: ScrollableState,
    buffer: Int = 2,
    reverse: Boolean = false,
    onLoadMore: () -> Unit
) {
    val updatedState by rememberUpdatedState(state)

    val loadMore by remember(updatedState) {
        derivedStateOf {
            when (val scrollState = updatedState) {
                is LazyListState -> if (reverse) scrollState.reachedTop(buffer) else scrollState.reachedBottom(buffer)
                is LazyGridState -> if (reverse) scrollState.reachedTop(buffer) else scrollState.reachedBottom(buffer)
                is LazyStaggeredGridState -> if (reverse) scrollState.reachedTop(buffer) else scrollState.reachedBottom(buffer)
                else -> false
            }
        }
    }

    LaunchedEffect(loadMore) {
        if (loadMore) onLoadMore()
    }
}

private const val INDEX = 1

private fun isReachedTop(totalCount: Int, firstVisibleIndex: Int?, buffer: Int): Boolean {
    if (totalCount == 0) return false
    val firstVisible = firstVisibleIndex ?: return false
    return firstVisible <= buffer
}

private fun isReachedBottom(totalCount: Int, lastVisibleIndex: Int?, buffer: Int): Boolean {
    if (totalCount == 0) return false
    val lastVisible = lastVisibleIndex ?: return false
    val lastCriteria = totalCount - INDEX - buffer
    return lastVisible >= lastCriteria
}

private fun LazyListState.reachedBottom(buffer: Int): Boolean = isReachedBottom(
    totalCount = layoutInfo.totalItemsCount,
    lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index,
    buffer = buffer
)

private fun LazyListState.reachedTop(buffer: Int): Boolean = isReachedTop(
    totalCount = layoutInfo.totalItemsCount,
    firstVisibleIndex = layoutInfo.visibleItemsInfo.firstOrNull()?.index,
    buffer = buffer
)

private fun LazyGridState.reachedBottom(buffer: Int): Boolean = isReachedBottom(
    totalCount = layoutInfo.totalItemsCount,
    lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index,
    buffer = buffer
)

private fun LazyGridState.reachedTop(buffer: Int): Boolean = isReachedTop(
    totalCount = layoutInfo.totalItemsCount,
    firstVisibleIndex = layoutInfo.visibleItemsInfo.firstOrNull()?.index,
    buffer = buffer
)

private fun LazyStaggeredGridState.reachedBottom(buffer: Int): Boolean = isReachedBottom(
    totalCount = layoutInfo.totalItemsCount,
    lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index,
    buffer = buffer
)

private fun LazyStaggeredGridState.reachedTop(buffer: Int): Boolean = isReachedTop(
    totalCount = layoutInfo.totalItemsCount,
    firstVisibleIndex = layoutInfo.visibleItemsInfo.firstOrNull()?.index,
    buffer = buffer
)