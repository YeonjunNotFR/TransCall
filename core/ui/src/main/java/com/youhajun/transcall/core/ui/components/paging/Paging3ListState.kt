package com.youhajun.transcall.core.ui.components.paging

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.launch

/**
 * https://issuetracker.google.com/issues/177245496
 */
@Composable
fun <T : Any> LazyPagingItems<T>.rememberPaging3ListState(): LazyListState {
    val state = androidx.compose.foundation.lazy.rememberLazyListState()
    val scrollIndex = rememberSaveable { mutableIntStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

    return when (itemCount) {
        0 -> rememberSaveable(saver = LazyListState.Saver) {
            scrollIndex.intValue = state.firstVisibleItemIndex
            LazyListState(state.firstVisibleItemIndex, state.firstVisibleItemScrollOffset)
        }
        else -> {
            if(scrollIndex.intValue in 1..<itemCount) {
                LaunchedEffect("") {
                    coroutineScope.launch {
                        state.animateScrollToItem(scrollIndex.intValue, state.firstVisibleItemScrollOffset)
                        scrollIndex.intValue = 0
                    }
                }
            }
            state
        }
    }
}