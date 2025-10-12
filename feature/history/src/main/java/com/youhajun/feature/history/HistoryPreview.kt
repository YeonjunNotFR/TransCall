package com.youhajun.feature.history

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.history.CallHistory
import com.youhajun.core.model.room.Participant
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
                        historyId = "12345",
                        title = "Sample Call History Title",
                        summary = "This is a sample call history summary that is quite long and should be truncated if it exceeds the maximum line limit.",
                        participants = persistentListOf(
                            Participant(
                                participantId = "67890",
                                userId = "1",
                                displayName = "John Doe dwaafwojfdkawdkadwkd dawkawd akwdkaw dawkd wa dwakd",
                                imageUrl = "https://example.com/image.jpg",
                                language = LanguageType.ENGLISH
                            )
                        ),
                        joinedAtToEpochTime = 1760157304,
                        leftAtToEpochTime = 1760157319,
                        durationSeconds = 0,
                        memo = "",
                        isLiked = false
                    ),
                    CallHistory(
                        historyId = "67890",
                        title = "Another Call History",
                        summary = "This is another call history summary that is also quite long and should be truncated if it exceeds the maximum line limit.",
                        participants = persistentListOf(
                            Participant(
                                participantId = "12345",
                                userId = "1",
                                displayName = "Jane Smith",
                                imageUrl = "https://example.com/image2.jpg",
                                language = LanguageType.SPANISH
                            )
                        ),
                        joinedAtToEpochTime = 1633072800L,
                        leftAtToEpochTime = 1633076400L,
                        durationSeconds = 600,
                        memo = "This is a memo for the second call history.",
                        isLiked = true
                    ),
                ),
                "2023-10-02" to persistentListOf(
                    CallHistory(
                        historyId = "11223",
                        title = "Third Call History",
                        summary = "This is the third call history summary, which is also quite long and should be truncated if it exceeds the maximum line limit.",
                        participants = persistentListOf(
                            Participant(
                                participantId = "33445",
                                userId = "1",
                                displayName = "Alice Johnson",
                                imageUrl = "https://example.com/image3.jpg",
                                language = LanguageType.SPANISH
                            )
                        ),
                        joinedAtToEpochTime = 1633072800L,
                        leftAtToEpochTime = 1633076400L,
                        durationSeconds = 300,
                        memo = "This is a memo for the third call history.",
                        isLiked = false
                    )
                )
            ),
        ),
        historyListState = rememberLazyListState(),
        onClickDateRange = {},
        onClickHistory = {}
    )
}