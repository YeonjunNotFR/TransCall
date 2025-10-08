package com.youhajun.feature.room.list

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youhajun.core.design.Colors
import com.youhajun.core.design.R
import com.youhajun.core.design.Typography
import com.youhajun.core.model.SortDirection
import com.youhajun.core.model.room.CurrentRoomInfo
import com.youhajun.core.model.room.RoomInfo
import com.youhajun.core.permission.PermissionForceHandler
import com.youhajun.core.permission.rememberPermissionRequestController
import com.youhajun.core.route.NavigationEvent
import com.youhajun.feature.call.api.LocalCallIntentFactory
import com.youhajun.hyanghae.graphics.modifier.conditional
import com.youhajun.hyanghae.graphics.modifier.removeParentPadding
import com.youhajun.transcall.core.ui.components.HorizontalSpacer
import com.youhajun.transcall.core.ui.components.VerticalSpacer
import com.youhajun.transcall.core.ui.components.modifier.noRippleClickable
import com.youhajun.transcall.core.ui.components.paging.PagingComp
import com.youhajun.transcall.core.ui.components.room.CurrentRoomItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun RoomListRoute(
    viewModel: RoomListViewModel = hiltViewModel(),
    onNavigate: (NavigationEvent) -> Unit
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val permissionController = rememberPermissionRequestController()
    val callIntentFactory = LocalCallIntentFactory.current

    viewModel.collectSideEffect {
        when (it) {
            RoomListSideEffect.PermissionCheck -> permissionController.request()
            is RoomListSideEffect.Navigation -> onNavigate(it.navigationEvent)
            is RoomListSideEffect.GoToCall -> {
                val callIntent = callIntentFactory.getCallActivityIntent(context, it.roomId)
                context.startActivity(callIntent)
            }
        }
    }

    val lazyListState = rememberLazyListState()
    PagingComp(lazyListState, onLoadMore = viewModel::onRoomNextPage)

    PermissionForceHandler(
        controller = permissionController,
        permissions = persistentListOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        ),
        rationaleMessage = stringResource(R.string.permission_rationale_message_camera_and_mic),
        onPermissionGranted = viewModel::onJoinRoomPermissionGranted
    )

    RoomListScreen(
        state = state,
        roomLazyListState = lazyListState,
        onClickRoom = viewModel::onClickRoom,
        onClickCreateRoom = viewModel::onClickCreateRoom,
        onChangeParticipantCountSort = viewModel::onChangeParticipantCountSort,
        onChangeCreatedAtSort = viewModel::onChangeCreatedAtSort
    )
}

@Composable
internal fun RoomListScreen(
    state: RoomListState,
    roomLazyListState: LazyListState,
    onClickRoom: (RoomInfo) -> Unit,
    onClickCreateRoom: () -> Unit,
    onChangeParticipantCountSort: () -> Unit,
    onChangeCreatedAtSort: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.White)
            .padding(horizontal = 12.dp),
    ) {
        RoomListHeader(
            participantSort = state.participantSort,
            createdAtSort = state.createdAtSort,
            onChangeParticipantCountSort = onChangeParticipantCountSort,
            onChangeCreatedAtSort = onChangeCreatedAtSort,
            onClickCreateRoom = onClickCreateRoom
        )

        HorizontalDivider(modifier = Modifier.removeParentPadding(PaddingValues(horizontal = 12.dp)), 1.dp, Colors.FFE6E9EE)

        RoomListLazyColumn(
            roomList = state.currentRoomList,
            roomLazyListState = roomLazyListState,
            onClickRoom = onClickRoom
        )
    }
}

@Composable
private fun RoomListHeader(
    participantSort: SortDirection,
    createdAtSort: SortDirection,
    onClickCreateRoom: () -> Unit,
    onChangeParticipantCountSort: () -> Unit,
    onChangeCreatedAtSort: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Colors.White)
    ) {
        VerticalSpacer(16.dp)

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.room_list_title),
                modifier = Modifier.weight(1f),
                color = Colors.Black,
                fontWeight = FontWeight.W600,
                style = Typography.titleLarge,
            )

            HorizontalSpacer(8.dp)

            Icon(
                modifier = Modifier
                    .noRippleClickable(onClick = onClickCreateRoom)
                    .size(28.dp),
                imageVector = Icons.Default.AddCircle,
                contentDescription = null,
                tint = Colors.FF0EA5E9
            )
        }

        VerticalSpacer(16.dp)

        SortRow(
            participantCountSort = participantSort,
            createdAtSort = createdAtSort,
            onChangeParticipantCountSort = onChangeParticipantCountSort,
            onChangeCreatedAtSort = onChangeCreatedAtSort
        )

        VerticalSpacer(8.dp)

    }
}

@Composable
private fun SortRow(
    participantCountSort: SortDirection,
    createdAtSort: SortDirection,
    onChangeParticipantCountSort: () -> Unit,
    onChangeCreatedAtSort: () -> Unit
) {
    val participantCountChecked = participantCountSort == SortDirection.DESC
    val createdAtChecked = createdAtSort == SortDirection.DESC

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SortItem(
            text = stringResource(R.string.room_list_participant_count_sort),
            isChecked = participantCountChecked,
            onChangeSort = onChangeParticipantCountSort
        )

        HorizontalSpacer(4.dp)

        SortItem(
            text = stringResource(R.string.room_list_created_at_sort),
            isChecked = createdAtChecked,
            onChangeSort = onChangeCreatedAtSort
        )
    }
}

@Composable
private fun SortItem(
    text: String,
    isChecked: Boolean,
    onChangeSort: () -> Unit
) {
    Text(
        modifier = Modifier
            .noRippleClickable(onClick = onChangeSort)
            .background(if (isChecked) Colors.FFFF5724 else Colors.White, RoundedCornerShape(28.dp))
            .conditional(!isChecked) {
                border(1.dp, Colors.FFD0D7E2, RoundedCornerShape(28.dp))
            }
            .padding(horizontal = 8.dp, vertical = 4.dp),
        text = text,
        color = if (isChecked) Colors.White else Colors.FF1E293B,
        fontWeight = FontWeight.W600,
        overflow = TextOverflow.Ellipsis,
        style = Typography.bodySmall
    )
}

@Composable
private fun ColumnScope.RoomListLazyColumn(
    roomList: ImmutableList<CurrentRoomInfo>,
    roomLazyListState: LazyListState,
    onClickRoom: (RoomInfo) -> Unit
) {
    LazyColumn(
        state = roomLazyListState,
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        contentPadding = PaddingValues(vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = roomList,
            key = { it.roomInfo.roomId }
        ) { roomInfo ->
            CurrentRoomItem(
                currentRoomInfo = roomInfo,
                onClickRoom = onClickRoom,
            )
        }
    }
}

@Preview
@Composable
private fun RoomListPreviewMirror() {
    RoomListPreview()
}