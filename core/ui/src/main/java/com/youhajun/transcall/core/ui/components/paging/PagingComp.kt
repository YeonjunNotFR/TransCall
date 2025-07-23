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
    onLoadMore: () -> Unit
) {
    val updatedState by rememberUpdatedState(state)

    val loadMore by remember(updatedState) {
        derivedStateOf {
            when (val scrollState = updatedState) {
                is LazyListState -> scrollState.reachedBottom(buffer)
                is LazyGridState -> scrollState.reachedBottom(buffer)
                is LazyStaggeredGridState -> scrollState.reachedBottom(buffer)
                else -> false
            }
        }
    }

    LaunchedEffect(loadMore) {
        if(loadMore) onLoadMore()
    }
}


private const val INDEX = 1

private fun LazyListState.reachedBottom(buffer: Int): Boolean {
    val lastVisibleItemIdx = this.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
    return lastVisibleItemIdx != 0 && lastVisibleItemIdx >= (this.layoutInfo.totalItemsCount - INDEX - buffer)
}

private fun LazyGridState.reachedBottom(buffer: Int): Boolean {
    val lastVisibleItemIdx = this.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
    return lastVisibleItemIdx != 0 && lastVisibleItemIdx >= (this.layoutInfo.totalItemsCount - INDEX - buffer)
}

private fun LazyStaggeredGridState.reachedBottom(buffer: Int): Boolean {
    val lastVisibleItemIdx = this.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
    return lastVisibleItemIdx != 0 && lastVisibleItemIdx >= (this.layoutInfo.totalItemsCount - INDEX - buffer)
}