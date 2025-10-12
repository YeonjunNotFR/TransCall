package com.youhajun.feature.history.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.youhajun.core.design.Colors
import com.youhajun.core.design.R
import com.youhajun.core.design.Typography
import com.youhajun.core.model.history.CallHistory
import com.youhajun.core.model.calling.payload.TranslationMessage
import com.youhajun.core.model.room.Participant
import com.youhajun.core.route.NavigationEvent
import com.youhajun.transcall.core.ui.components.HorizontalSpacer
import com.youhajun.transcall.core.ui.components.VerticalSpacer
import com.youhajun.transcall.core.ui.components.conversation.ConversationItem
import com.youhajun.transcall.core.ui.components.history.HistoryTimeInfoRow
import com.youhajun.transcall.core.ui.components.modifier.noRippleClickable
import com.youhajun.transcall.core.ui.components.paging.rememberPaging3ListState
import com.youhajun.transcall.core.ui.components.participant.ParticipantItem
import kotlinx.collections.immutable.ImmutableList
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun HistoryDetailRoute(
    viewModel: HistoryDetailViewModel = hiltViewModel(),
    onNavigate: (NavigationEvent) -> Unit
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val messagesPaging = viewModel.messages.collectAsLazyPagingItems()
    val pagingListState = messagesPaging.rememberPaging3ListState()

    viewModel.collectSideEffect {
        when (it) {
            is HistoryDetailSideEffect.Navigation -> onNavigate(it.navigationEvent)
        }
    }

    HistoryDetailScreen(
        state = state,
        messagesPaging = messagesPaging,
        pagingListState = pagingListState,
        onClickParticipant = viewModel::onClickParticipant,
        onClickViewMemo = viewModel::onClickViewMemo,
        onClickViewSummary = viewModel::onClickViewSummary
    )
}

@Composable
internal fun HistoryDetailScreen(
    state: HistoryDetailState,
    pagingListState: LazyListState,
    messagesPaging: LazyPagingItems<TranslationMessage>,
    onClickParticipant: (Participant) -> Unit,
    onClickViewMemo: () -> Unit,
    onClickViewSummary: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.White)
    ) {
        HistoryDetailHeader(state.history)

        HistoryTimeInfoRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            joinedAtToEpochTime = state.history.joinedAtToEpochTime,
            leftAtToEpochTime = state.history.leftAtToEpochTime,
            durationSeconds = state.history.durationSeconds,
        )

        HorizontalDivider(modifier = Modifier, 1.dp, Colors.FFE6E9EE)

        ConversationsLazyColumn(
            messagesPaging = messagesPaging,
            pagingListState = pagingListState,
            myUserId = state.myUserId,
            participants = state.history.participants
        )

        VerticalSpacer(4.dp)

        Text(
            text = stringResource(R.string.history_detail_participants),
            color = Colors.FF6B7280,
            fontSize = 13.sp,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = Typography.titleSmall,
        )

        VerticalSpacer(8.dp)

        ParticipantsLazyRow(
            participants = state.history.participants,
            onClickParticipant = onClickParticipant
        )

        VerticalSpacer(8.dp)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ViewSummaryButton(onClickViewSummary)
            ViewMemoButton(onClickViewMemo)
        }

        VerticalSpacer(8.dp)
    }
}

@Composable
private fun HistoryDetailHeader(
    history: CallHistory
) {
    val likeIcon = if(history.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder
    val likeTint = if(history.isLiked) Colors.FFFFAD42 else Colors.Gray700

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Colors.White)
            .padding(start = 12.dp, end = 12.dp, top = 16.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = history.title,
            color = Colors.Black,
            fontWeight = FontWeight.W600,
            style = Typography.titleLarge,
            modifier = Modifier.weight(1f)
        )

        HorizontalSpacer(8.dp)

        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = likeIcon,
            contentDescription = null,
            tint = likeTint
        )
    }
}

@Composable
private fun ColumnScope.ConversationsLazyColumn(
    messagesPaging: LazyPagingItems<TranslationMessage>,
    pagingListState: LazyListState,
    myUserId: String,
    participants: ImmutableList<Participant>
) {
    LazyColumn(
        state = pagingListState,
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (messagesPaging.loadState.refresh == LoadState.Loading) {
            item(
                key = "network",
                contentType = { "network" }
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally),
                    text = stringResource(R.string.common_waiting_network),
                    style = Typography.labelMedium,
                    color = Colors.Gray500
                )
            }
        }

        items(
            count = messagesPaging.itemCount,
            key = { messagesPaging[it]?.conversationId ?: "placeholder-$it" },
            contentType = { "message" }
        ) {
            messagesPaging[it]?.let { message ->
                val isMine = message.senderId == myUserId
                val participant = participants.firstOrNull { it.userId == message.senderId }

                ConversationItem(
                    isMine = isMine,
                    translationMessage = message,
                    senderInfo = participant ?: Participant("")
                )
            }
        }

        if (messagesPaging.loadState.append == LoadState.Loading) {
            item(
                key = "loading",
                contentType = { "loading" }
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally),
                    color = Colors.Gray500
                )
            }
        }
    }
}

@Composable
private fun ParticipantsLazyRow(
    participants: ImmutableList<Participant>,
    onClickParticipant: (Participant) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = participants,
            key = { it.userId }
        ) {
            ParticipantItem(
                participant = it,
                onClick = onClickParticipant
            )
        }
    }
}

@Composable
private fun ViewSummaryButton(
    onClickViewSummary: () -> Unit
) {
    Row(
        modifier = Modifier
            .height(45.dp)
            .aspectRatio(165/45f)
            .noRippleClickable(onClick = onClickViewSummary)
            .shadow(2.dp, RoundedCornerShape(24.dp))
            .background(Colors.FFEEF6FF, RoundedCornerShape(24.dp))
            .border(1.dp, Colors.FFE6E9EE, RoundedCornerShape(24.dp)),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_book),
            tint = Colors.FF1659A8,
            contentDescription = null,
            modifier = Modifier.size(18.dp).offset(y = 1.dp)
        )

        HorizontalSpacer(8.dp)

        Text(
            text = stringResource(R.string.history_detail_view_summary),
            color = Colors.FF1659A8,
            fontWeight = FontWeight.W700,
            style = Typography.bodyLarge,
        )
    }
}

@Composable
private fun ViewMemoButton(
    onClickViewMemo: () -> Unit
) {

    Row(
        modifier = Modifier
            .height(45.dp)
            .aspectRatio(165/45f)
            .noRippleClickable(onClick = onClickViewMemo)
            .shadow(2.dp, RoundedCornerShape(24.dp))
            .background(Brush.linearGradient(listOf(Colors.FF3B82F6, Colors.FF0EA5E9)), RoundedCornerShape(24.dp)),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_memo),
            tint = Colors.White,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )

        HorizontalSpacer(8.dp)

        Text(
            text = stringResource(R.string.history_detail_view_memo),
            color = Colors.White,
            fontWeight = FontWeight.W700,
            style = Typography.bodyLarge,
        )
    }
}

@Preview
@Composable
private fun HistoryDetailPreviewMirror() {
    HistoryDetailPreview()
}
