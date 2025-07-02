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
                imageUrl = null,
                membershipPlan = MembershipPlan.Free,
                remainTime = RemainTime(
                    remainingSeconds = 3600,
                    resetAtEpochSeconds = 0,
                    dailyLimitSeconds = null
                ),
                language = LanguageType.ENGLISH
            ),
            callHistoryPreviewMaxSize = 3,
            callHistoryList = persistentListOf(
                CallHistory(
                    callId = "1234567890",
                    partner = Participant(
                        userId = "67890",
                        displayName = "John Doe",
                        imageUrl = "https://example.com/image.jpg",
                        language = LanguageType.ENGLISH
                    ),
                    startedAtEpochSeconds = 0,
                    durationSeconds = 3600,
                ),
                CallHistory(
                    callId = "0987654321",
                    partner = Participant(
                        userId = "54321",
                        displayName = "Jane Smith",
                        imageUrl = null,
                        language = LanguageType.ENGLISH
                    ),
                    startedAtEpochSeconds = 0,
                    durationSeconds = 7200
                ),
                CallHistory(
                    callId = "1122334455",
                    partner = Participant(
                        userId = "9988776655",
                        displayName = "Alice Johnson",
                        imageUrl = null,
                        language = LanguageType.ENGLISH
                    ),
                    startedAtEpochSeconds = 0,
                    durationSeconds = 1800
                ),
                CallHistory(
                    callId = "5566778899",
                    partner = Participant(
                        userId = "2233445566",
                        displayName = "Bob Brown",
                        imageUrl = null,
                        language = LanguageType.ENGLISH
                    ),
                    startedAtEpochSeconds = 0,
                    durationSeconds = 5400
                )
            ),
        ),
        onClickCallAgain = {},
        onClickHistoryMore = {},
        onClickStartCall = {},
        onClickJoinCall = {}
    )
}