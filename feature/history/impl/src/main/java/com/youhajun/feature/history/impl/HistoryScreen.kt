package com.youhajun.feature.history.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youhajun.core.model.CallHistory
import com.youhajun.core.model.DateRange
import com.youhajun.core.ui.R
import com.youhajun.transcall.core.ui.components.LazyBackgroundColumn
import com.youhajun.transcall.core.ui.components.VerticalSpacer
import com.youhajun.transcall.core.ui.components.history.CallHistoryItem
import com.youhajun.transcall.core.ui.components.history.DateRangeRow
import com.youhajun.transcall.core.ui.theme.Colors
import com.youhajun.transcall.core.ui.theme.Typography
import com.youhajun.transcall.core.ui.util.DateFormatPatterns
import com.youhajun.transcall.core.ui.util.noRippleClickable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun HistoryRoute(
    viewModel: HistoryViewModel = hiltViewModel(),
    onNavigate: (HistorySideEffect.Navigation) -> Unit
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    viewModel.collectSideEffect {
        when (it) {
            is HistorySideEffect.Navigation -> onNavigate(it)
        }
    }

    HistoryScreen(
        state = state,
        onClickCallAgain = viewModel::onClickCallAgain,
        onClickDateRange = viewModel::onClickDateRange,
        onClickFavorite = viewModel::onClickFavorite
    )
}

@Composable
private fun HistoryScreen(
    state: HistoryState,
    onClickCallAgain: (String) -> Unit,
    onClickFavorite: () -> Unit,
    onClickDateRange: (DateRange) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.FFB2F2FF)
            .padding(horizontal = 16.dp),
    ) {
        VerticalSpacer(8.dp)

        HistoryHeader(onClickFavorite)

        VerticalSpacer(16.dp)

        HistoryBody(
            callHistoryMap = state.callHistoryDateMap,
            selectedDateRange = state.selectedDateRange,
            onClickCallAgain = onClickCallAgain,
            onClickDateRange = onClickDateRange
        )
    }
}

@Composable
private fun HistoryHeader(
    onClickFavorite: () -> Unit,
) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = stringResource(R.string.screen_title_history),
            modifier = Modifier.weight(1f),
            color = Colors.Black,
            style = Typography.displayMedium
        )

        Icon(
            modifier = Modifier
                .noRippleClickable(onClick = onClickFavorite)
                .size(28.dp),
            painter = painterResource(R.drawable.ic_home),
            contentDescription = null
        )
    }
}

@Composable
private fun ColumnScope.HistoryBody(
    selectedDateRange: DateRange,
    onClickDateRange: (DateRange) -> Unit,
    callHistoryMap: ImmutableMap<String, ImmutableList<CallHistory>>,
    onClickCallAgain: (String) -> Unit,
) {
    DateRangeRow(
        selectedDateRange = selectedDateRange,
        onClick = onClickDateRange,
    )

    VerticalSpacer(12.dp)

    CallHistoryLazyColumn(
        callHistoryMap = callHistoryMap,
        onClickCallAgain = onClickCallAgain
    )
}

@Composable
private fun ColumnScope.CallHistoryLazyColumn(
    callHistoryMap: ImmutableMap<String, ImmutableList<CallHistory>>,
    onClickCallAgain: (String) -> Unit,
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
                        dateFormat = DateFormatPatterns.TIME_ONLY,
                        onClickCallAgain = onClickCallAgain
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
private fun HistoryScreenPreview() {
    HistoryScreen(
        state = HistoryState(
            callHistoryDateMap = persistentMapOf(
                "2023-10-01" to persistentListOf(
                    CallHistory(
                        callId = "1",
                        partner = null,
                        startedAtEpochSeconds = 1633072800L,
                        durationSeconds = 12001,
                        isFavorite = false
                    ),
                    CallHistory(
                        callId = "2",
                        partner = null,
                        startedAtEpochSeconds = 1633072800L,
                        durationSeconds = 12001,
                        isFavorite = false
                    ),
                    CallHistory(
                        callId = "3",
                        partner = null,
                        startedAtEpochSeconds = 1633072800L,
                        durationSeconds = 12001,
                        isFavorite = false
                    )
                ),
                "2023-10-02" to persistentListOf(
                    CallHistory(
                        callId = "4",
                        partner = null,
                        startedAtEpochSeconds = 1633072800L,
                        durationSeconds = 12001,
                        isFavorite = false
                    ),
                    CallHistory(
                        callId = "5",
                        partner = null,
                        startedAtEpochSeconds = 1633072800L,
                        durationSeconds = 12001,
                        isFavorite = false
                    )
                )
            )
        ),
        onClickCallAgain = {},
        onClickDateRange = {},
        onClickFavorite = {}
    )
}