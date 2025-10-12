package com.youhajun.feature.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youhajun.core.design.Colors
import com.youhajun.core.design.R
import com.youhajun.core.design.Typography
import com.youhajun.core.model.DateRange
import com.youhajun.core.model.history.CallHistory
import com.youhajun.core.route.NavigationEvent
import com.youhajun.transcall.core.ui.components.LazyBackgroundColumn
import com.youhajun.transcall.core.ui.components.VerticalSpacer
import com.youhajun.transcall.core.ui.components.history.CallHistoryItem
import com.youhajun.transcall.core.ui.components.history.DateRangeRow
import com.youhajun.transcall.core.ui.components.paging.PagingComp
import com.youhajun.transcall.core.ui.util.DateFormatPatterns
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun HistoryRoute(
    viewModel: HistoryViewModel = hiltViewModel(),
    onNavigate: (NavigationEvent) -> Unit
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    viewModel.collectSideEffect {
        when (it) {
            is HistorySideEffect.Navigation -> onNavigate(it.navigationEvent)
        }
    }

    val lazyListState = rememberLazyListState()
    PagingComp(lazyListState, onLoadMore = viewModel::onHistoryNextPage)

    HistoryScreen(
        state = state,
        historyListState = lazyListState,
        onClickDateRange = viewModel::onClickDateRange,
        onClickHistory = viewModel::onClickHistory
    )
}

@Composable
internal fun HistoryScreen(
    state: HistoryState,
    historyListState: LazyListState,
    onClickDateRange: (DateRange) -> Unit,
    onClickHistory: (CallHistory) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.White)
            .padding(horizontal = 12.dp),
    ) {
        VerticalSpacer(8.dp)

        HistoryHeader()

        VerticalSpacer(8.dp)

        HistoryBody(
            historyListState = historyListState,
            callHistoryMap = state.callHistoryDateMap,
            selectedDateRange = state.selectedDateRange,
            onClickDateRange = onClickDateRange,
            onClickHistory = onClickHistory
        )
    }
}

@Composable
private fun HistoryHeader() {
    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.history_title),
            modifier = Modifier.weight(1f),
            color = Colors.Black,
            fontWeight = FontWeight.W600,
            style = Typography.titleLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ColumnScope.HistoryBody(
    historyListState: LazyListState,
    selectedDateRange: DateRange,
    callHistoryMap: ImmutableMap<String, ImmutableList<CallHistory>>,
    onClickDateRange: (DateRange) -> Unit,
    onClickHistory: (CallHistory) -> Unit
) {
    DateRangeRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Colors.FFEEF6FF, RoundedCornerShape(12.dp))
            .border(1.dp, Colors.FFB2F2FF, RoundedCornerShape(12.dp)),
        selectedDateRange = selectedDateRange,
        indicatorColor = Colors.FF60A5FA,
        onClick = onClickDateRange,
    )

    VerticalSpacer(12.dp)

    CallHistoryLazyColumn(
        callHistoryMap = callHistoryMap,
        state = historyListState,
        onClickHistory = onClickHistory
    )
}

@Composable
private fun ColumnScope.CallHistoryLazyColumn(
    callHistoryMap: ImmutableMap<String, ImmutableList<CallHistory>>,
    state: LazyListState,
    onClickHistory: (CallHistory) -> Unit
) {
    LazyColumn(
        state = state,
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
    ) {
        callHistoryMap.forEach { (date, callHistories) ->
            stickyHeader(key = date) {
                LazyBackgroundColumn(
                    color = Colors.FFEEF6FF,
                    isFirst = true,
                    isLast = false,
                    paddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    VerticalSpacer(4.dp)

                    Text(
                        text = date,
                        color = Colors.Black,
                        fontWeight = FontWeight.W800,
                        style = Typography.titleLarge
                    )
                }
            }

            itemsIndexed(
                items = callHistories,
                key = { index, callHistory -> callHistory.historyId }
            ) { idx, callHistory ->
                val isLast = idx == callHistories.lastIndex

                LazyBackgroundColumn(
                    color = Colors.FFEEF6FF,
                    isFirst = false,
                    isLast = isLast,
                    paddingValues = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    CallHistoryItem(
                        callHistory = callHistory,
                        dateFormat = DateFormatPatterns.HOUR_MINUTE,
                        onClickHistory = onClickHistory
                    )

                    if (isLast) {
                        VerticalSpacer(8.dp)
                    }
                }

                if (isLast) {
                    VerticalSpacer(12.dp)
                }
            }
        }
    }
}

@Preview
@Composable
private fun HistoryPreviewMirror() {
    HistoryPreview()
}