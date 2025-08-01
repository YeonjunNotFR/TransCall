package com.youhajun.feature.home.impl

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.MembershipPlan
import com.youhajun.core.model.RemainTime
import com.youhajun.core.model.calling.CallHistory
import com.youhajun.core.model.room.Participant
import com.youhajun.core.model.user.MyInfo
import kotlinx.collections.immutable.persistentListOf

@Preview(showBackground = true)
@Composable
internal fun HomePreview() {
    HomeScreen(
        state = HomeState(
            myInfo = MyInfo(
                userId = "1234567890",
                displayName = "John Doe",
                imageUrl = "https://example.com/profile.jpg",
                membershipPlan = MembershipPlan.Free,
                remainTime = RemainTime(
                    remainingSeconds = 3600,
                    resetAtEpochSeconds = 0,
                ),
                language = LanguageType.ENGLISH
            ),
            callHistoryPreviewMaxSize = 3,
            callHistoryList = persistentListOf(
                CallHistory(
                    historyId = "12345",
                    title = "Sample Call History Title",
                    summary = "This is a sample call history summary that is quite long and should be truncated if it exceeds the maximum line limit.",
                    participants = persistentListOf(
                        Participant(
                            userId = "67890",
                            displayName = "John Doe dwaafwojfdkawdkadwkd dawkawd akwdkaw dawkd wa dwakd",
                            imageUrl = "https://example.com/image.jpg",
                            language = LanguageType.ENGLISH
                        )
                    ),
                    startedAtToEpochTime = 1633072800L,
                    endedAtToEpochTime = 1633076400L,
                    durationSeconds = 12001,
                    memo = "",
                    isLiked = false
                ),
                CallHistory(
                    historyId = "12346",
                    title = "Sample Call History Title",
                    summary = "This is a sample call history summary that is quite long and should be truncated if it exceeds the maximum line limit.",
                    participants = persistentListOf(
                        Participant(
                            userId = "67890",
                            displayName = "John Doe dwaafwojfdkawdkadwkd dawkawd akwdkaw dawkd wa dwakd",
                            imageUrl = "https://example.com/image.jpg",
                            language = LanguageType.ENGLISH
                        )
                    ),
                    startedAtToEpochTime = 1633072800L,
                    endedAtToEpochTime = 1633076400L,
                    durationSeconds = 12001,
                    memo = "",
                    isLiked = false
                ),
                CallHistory(
                    historyId = "12347",
                    title = "Sample Call History Title",
                    summary = "This is a sample call history summary that is quite long and should be truncated if it exceeds the maximum line limit.",
                    participants = persistentListOf(
                        Participant(
                            userId = "67890",
                            displayName = "John Doe dwaafwojfdkawdkadwkd dawkawd akwdkaw dawkd wa dwakd",
                            imageUrl = "https://example.com/image.jpg",
                            language = LanguageType.ENGLISH
                        )
                    ),
                    startedAtToEpochTime = 1633072800L,
                    endedAtToEpochTime = 1633076400L,
                    durationSeconds = 12001,
                    memo = "",
                    isLiked = false
                ),
                CallHistory(
                    historyId = "12348",
                    title = "Sample Call History Title",
                    summary = "This is a sample call history summary that is quite long and should be truncated if it exceeds the maximum line limit.",
                    participants = persistentListOf(
                        Participant(
                            userId = "67890",
                            displayName = "John Doe dwaafwojfdkawdkadwkd dawkawd akwdkaw dawkd wa dwakd",
                            imageUrl = "https://example.com/image.jpg",
                            language = LanguageType.ENGLISH
                        )
                    ),
                    startedAtToEpochTime = 1633072800L,
                    endedAtToEpochTime = 1633076400L,
                    durationSeconds = 12001,
                    memo = "",
                    isLiked = false
                ),
            ),
        ),
        onClickHistoryMore = {},
        onClickCreateRoom = {},
        onClickJoinCall = {}
    )
}

@Preview(showBackground = true)
@Composable
internal fun HomePreviewEmpty() {
    HomeScreen(
        state = HomeState(
            myInfo = MyInfo(
                userId = "1234567890",
                displayName = "John Doe",
                imageUrl = "https://example.com/profile.jpg",
                membershipPlan = MembershipPlan.Free,
                remainTime = RemainTime(
                    remainingSeconds = 3600,
                    resetAtEpochSeconds = 0,
                ),
                language = LanguageType.ENGLISH
            ),
            callHistoryPreviewMaxSize = 3,
        ),
        onClickHistoryMore = {},
        onClickCreateRoom = {},
        onClickJoinCall = {}
    )
}