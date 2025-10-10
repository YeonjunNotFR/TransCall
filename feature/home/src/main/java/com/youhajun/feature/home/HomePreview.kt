package com.youhajun.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.MembershipPlan
import com.youhajun.core.model.RemainTime
import com.youhajun.core.model.history.CallHistory
import com.youhajun.core.model.room.CurrentRoomInfo
import com.youhajun.core.model.room.Participant
import com.youhajun.core.model.room.RoomInfo
import com.youhajun.core.model.user.MyInfo
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf

@Preview(showBackground = true)
@Composable
internal fun HomePreview() {
    HomeScreen(
        state = HomeState(
            currentRoomInfo = CurrentRoomInfo(
                roomInfo = RoomInfo(
                    roomId = "12345",
                    title = "Sample Room Title",
                    tags = persistentSetOf("요리", "낚시", "배고프다", "잠온다", "아이고", "언제 끝나려나",)
                ),
                currentParticipants = persistentListOf(
                    Participant(
                        participantId = "67890",
                        userId = "3",
                        displayName = "John Doe",
                        imageUrl = "https://example.com/image.jpg",
                        language = LanguageType.ENGLISH
                    ),
                    Participant(
                        participantId = "12345",
                        userId = "2",
                        displayName = "Jane Smith",
                        imageUrl = "https://example.com/image2.jpg",
                        language = LanguageType.SPANISH
                    ),
                    Participant(
                        participantId = "33445",
                        userId = "1",
                        displayName = "Alice Johnson",
                        imageUrl = "https://example.com/image3.jpg",
                        language = LanguageType.SPANISH
                    )
                )
            ),
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
                            participantId = "67890",
                            userId = "1",
                            displayName = "John Doe dwaafwojfdkawdkadwkd dawkawd akwdkaw dawkd wa dwakd",
                            imageUrl = "https://example.com/image.jpg",
                            language = LanguageType.ENGLISH
                        )
                    ),
                    joinedAtToEpochTime = 1633072800L,
                    leftAtToEpochTime = 1633076400L,
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
                            participantId = "67890",
                            userId = "1",
                            displayName = "John Doe dwaafwojfdkawdkadwkd dawkawd akwdkaw dawkd wa dwakd",
                            imageUrl = "https://example.com/image.jpg",
                            language = LanguageType.ENGLISH
                        )
                    ),
                    joinedAtToEpochTime = 1633072800L,
                    leftAtToEpochTime = 1633076400L,
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
                            participantId = "67890",
                            userId = "1",
                            displayName = "John Doe dwaafwojfdkawdkadwkd dawkawd akwdkaw dawkd wa dwakd",
                            imageUrl = "https://example.com/image.jpg",
                            language = LanguageType.ENGLISH
                        )
                    ),
                    joinedAtToEpochTime = 1633072800L,
                    leftAtToEpochTime = 1633076400L,
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
                            participantId = "67890",
                            userId = "1",
                            displayName = "John Doe dwaafwojfdkawdkadwkd dawkawd akwdkaw dawkd wa dwakd",
                            imageUrl = "https://example.com/image.jpg",
                            language = LanguageType.ENGLISH
                        )
                    ),
                    joinedAtToEpochTime = 1633072800L,
                    leftAtToEpochTime = 1633076400L,
                    durationSeconds = 12001,
                    memo = "",
                    isLiked = false
                ),
            ),
        ),
        onClickHistoryMore = {},
        onClickCreateRoom = {},
        onClickJoinCall = {},
        onClickCurrentRoom = {},
        onClickHistory = {}
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
        onClickJoinCall = {},
        onClickCurrentRoom = {},
        onClickHistory = {}
    )
}