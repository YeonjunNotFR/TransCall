package com.youhajun.feature.history.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.history.CallHistory
import com.youhajun.core.model.calling.payload.TranslationMessage
import com.youhajun.core.model.conversation.ConversationState
import com.youhajun.core.model.room.Participant
import com.youhajun.transcall.core.ui.components.paging.rememberPaging3ListState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow

@Preview
@Composable
internal fun HistoryDetailPreview() {

    val conversations = getDummyTranslationMessages(20)
    val flow = remember { MutableStateFlow(PagingData.from(conversations)) }
    val messagesPaging = flow.collectAsLazyPagingItems()

    HistoryDetailScreen(
        state = HistoryDetailState(
            history = CallHistory(
                historyId = "12345",
                title = "Sample Call History Title",
                summary = "This is a sample call history summary that is quite long and should be truncated if it exceeds the maximum line limit.",
                memo = "",
                participants = persistentListOf(
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
                ),
                joinedAtToEpochTime = 1760157304,
                leftAtToEpochTime = 1760157319,
                durationSeconds = 10,
                isLiked = false
            ),
            myUserId = "1"
        ),
        messagesPaging = messagesPaging,
        pagingListState = messagesPaging.rememberPaging3ListState(),
        onClickParticipant = {},
        onClickViewMemo = {},
        onClickViewSummary = {}
    )
}

private fun getDummyTranslationMessages(count: Int = 20): List<TranslationMessage> {
    val now = System.currentTimeMillis()
    return List(count) { idx ->
        val i = idx + 1
        val createdAt = now - (count - i) * 60000L
        TranslationMessage(
            conversationId = "conv-$i",
            roomId = "room-123",
            state = ConversationState.PENDING,
            senderId = "${(i % 3)+1}",
            originText = "원문 메시지 $i",
            originLanguage = LanguageType.ENGLISH,
            transText = if (i % 2 == 0) "Translated message $i" else null,
            transLanguage = if (i % 2 == 0) LanguageType.KOREAN else null,
            createdAtToEpochTime = createdAt,
            updatedAtToEpochTime = createdAt
        )
    }
}