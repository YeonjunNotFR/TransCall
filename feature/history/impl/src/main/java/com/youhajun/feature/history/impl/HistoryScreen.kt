package com.youhajun.feature.history.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youhajun.core.design.Colors
import com.youhajun.core.design.R
import com.youhajun.core.design.Typography
import com.youhajun.core.model.DateRange
import com.youhajun.core.model.calling.CallHistory
import com.youhajun.core.route.NavigationEvent
import com.youhajun.transcall.core.ui.components.LazyBackgroundColumn
import com.youhajun.transcall.core.ui.components.VerticalSpacer
import com.youhajun.transcall.core.ui.components.history.CallHistoryItem
import com.youhajun.transcall.core.ui.components.history.DateRangeRow
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

    HistoryScreen(
        state = state,
        onClickDateRange = viewModel::onClickDateRange,
    )
}

@Composable
internal fun HistoryScreen(
    state: HistoryState,
    onClickDateRange: (DateRange) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.FFE5EDF5)
            .padding(horizontal = 12.dp),
    ) {
        VerticalSpacer(8.dp)

        HistoryHeader()

        VerticalSpacer(8.dp)

        HistoryBody(
            callHistoryMap = state.callHistoryDateMap,
            selectedDateRange = state.selectedDateRange,
            onClickDateRange = onClickDateRange
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
            text = stringResource(R.string.screen_title_history),
            modifier = Modifier.weight(1f),
            color = Colors.Black,
            style = Typography.titleLarge.copy(
                fontWeight = FontWeight.W600,
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ColumnScope.HistoryBody(
    selectedDateRange: DateRange,
    onClickDateRange: (DateRange) -> Unit,
    callHistoryMap: ImmutableMap<String, ImmutableList<CallHistory>>,
) {
    DateRangeRow(
        selectedDateRange = selectedDateRange,
        indicatorColor = Colors.FF60A5FA,
        onClick = onClickDateRange,
    )

    VerticalSpacer(12.dp)

    CallHistoryLazyColumn(
        callHistoryMap = callHistoryMap,
    )
}

@Composable
private fun ColumnScope.CallHistoryLazyColumn(
    callHistoryMap: ImmutableMap<String, ImmutableList<CallHistory>>,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
    ) {
        callHistoryMap.forEach { (date, callHistories) ->
            stickyHeader {
                LazyBackgroundColumn(
                    isFirst = true,
                    isLast = false,
                    paddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    VerticalSpacer(4.dp)

                    Text(
                        text = date,
                        color = Colors.Black,
                        style = Typography.titleLarge.copy(
                            fontWeight = FontWeight.W800
                        )
                    )
                }
            }

            itemsIndexed(callHistories) { idx, callHistory ->
                val isLast = idx == callHistories.lastIndex

                LazyBackgroundColumn(
                    isFirst = false,
                    isLast = isLast,
                    paddingValues = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    CallHistoryItem(
                        callHistory = callHistory,
                        createdAtDateFormat = DateFormatPatterns.TIME_ONLY,
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