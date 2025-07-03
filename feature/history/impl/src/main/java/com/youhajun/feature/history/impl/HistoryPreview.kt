package com.youhajun.feature.history.impl

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.youhajun.core.model.calling.CallHistory
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

@Preview
@Composable
internal fun HistoryPreview() {
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