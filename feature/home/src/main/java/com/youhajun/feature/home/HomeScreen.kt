package com.youhajun.feature.home

import android.Manifest
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.youhajun.core.design.Colors
import com.youhajun.core.design.R
import com.youhajun.core.design.Typography
import com.youhajun.core.model.history.CallHistory
import com.youhajun.core.model.room.CurrentRoomInfo
import com.youhajun.core.model.room.RoomInfo
import com.youhajun.core.model.room.RoomVisibility
import com.youhajun.core.model.user.MyInfo
import com.youhajun.core.permission.PermissionForceHandler
import com.youhajun.core.permission.PermissionSoftHandler
import com.youhajun.core.permission.rememberPermissionRequestController
import com.youhajun.core.route.NavigationEvent
import com.youhajun.feature.call.api.LocalCallIntentFactory
import com.youhajun.feature.call.api.LocalCallServiceMainContract
import com.youhajun.transcall.core.ui.components.FilledActionButton
import com.youhajun.transcall.core.ui.components.HorizontalSpacer
import com.youhajun.transcall.core.ui.components.MembershipBadge
import com.youhajun.transcall.core.ui.components.VerticalSpacer
import com.youhajun.transcall.core.ui.components.bottomSheet.JoinWithCodeBottomSheet
import com.youhajun.transcall.core.ui.components.history.CallHistoryItem
import com.youhajun.transcall.core.ui.components.modifier.noRippleClickable
import com.youhajun.transcall.core.ui.components.room.CurrentRoomCard
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigate: (NavigationEvent) -> Unit
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val permissionController = rememberPermissionRequestController()
    val callIntentFactory = LocalCallIntentFactory.current
    val callServiceMainContract = LocalCallServiceMainContract.current

    viewModel.collectSideEffect {
        when (it) {
            HomeSideEffect.CallPermissionCheck -> permissionController.request()
            is HomeSideEffect.Navigation -> onNavigate(it.navigationEvent)
            is HomeSideEffect.GoToCall -> {
                val callIntent = callIntentFactory.getCallActivityIntent(context, it.roomId)
                context.startActivity(callIntent)
            }
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.onResume()
    }

    LaunchedEffect(callServiceMainContract) {
        viewModel.setCallServiceMainContract(callServiceMainContract)
    }

    PermissionForceHandler(
        controller = permissionController,
        permissions = persistentListOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO),
        rationaleMessage = stringResource(R.string.permission_rationale_message_camera_and_mic),
        onPermissionGranted = viewModel::onJoinRoomPermissionGranted
    )

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        PermissionSoftHandler(
            controller = permissionController,
            permissions = persistentListOf(Manifest.permission.POST_NOTIFICATIONS),
            rationaleMessage = stringResource(R.string.permission_rationale_message_notification),
            onPermissionGranted = {}
        )
    }

    HomeScreen(
        state = state,
        onClickHistoryMore = viewModel::onClickHistoryMore,
        onClickCreateRoom = viewModel::onClickCreateRoom,
        onClickJoinCall = viewModel::onClickJoinCall,
        onClickCurrentRoom = viewModel::onClickCurrentRoom,
        onClickHistory = viewModel::onClickHistory
    )

    JoinWithCodeBottomSheet(
        isVisible = state.isShowJoinBottomSheet,
        onVisibleChanged = viewModel::onChangedJoinBottomSheetVisibility,
        onClickCancel = viewModel::onClickJoinBottomSheetCancel,
        onClickConfirm = viewModel::onClickJoinBottomSheetConfirm
    )
}

@Composable
internal fun HomeScreen(
    state: HomeState,
    onClickHistoryMore: () -> Unit,
    onClickCreateRoom: () -> Unit,
    onClickJoinCall: () -> Unit,
    onClickCurrentRoom: (RoomInfo) -> Unit,
    onClickHistory: (CallHistory) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.White)
    ) {
        val (background, topRow, profileCenter, profileCard, bodyColumn) = createRefs()

        Box(
            modifier = Modifier
                .constrainAs(background) {
                    top.linkTo(parent.top)
                    bottom.linkTo(profileCenter.top)
                    height = Dimension.fillToConstraints
                }
                .fillMaxWidth()
                .background(Colors.FFB2F2FF)
        )

        Box(modifier = Modifier
            .constrainAs(profileCenter) {
                top.linkTo(profileCard.top)
                bottom.linkTo(profileCard.bottom)
            }
            .fillMaxWidth()
        )

        HomeHeader(
            modifier = Modifier
                .constrainAs(topRow) {
                    top.linkTo(parent.top, margin = 8.dp)
                }
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )

        ProfileCard(
            modifier = Modifier
                .constrainAs(profileCard) {
                    top.linkTo(topRow.bottom, margin = 12.dp)
                }
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            myInfo = state.myInfo
        )

        HomeBody(
            modifier = Modifier
                .constrainAs(bodyColumn) {
                    top.linkTo(profileCard.bottom, margin = 16.dp)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            currentRoomInfo = state.currentRoomInfo,
            previewMaxSize = state.callHistoryPreviewMaxSize,
            callHistoryList = state.callHistoryList,
            onClickHistoryMore = onClickHistoryMore,
            onClickCreateRoom = onClickCreateRoom,
            onClickJoinCall = onClickJoinCall,
            onClickCurrentRoom = onClickCurrentRoom,
            onClickHistory = onClickHistory
        )
    }
}

@Composable
private fun HomeHeader(
    modifier: Modifier
) {
    Row(
        modifier = modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .size(28.dp),
            painter = painterResource(R.drawable.ic_home),
            contentDescription = null
        )

        HorizontalSpacer(4.dp)

        Text(
            text = stringResource(R.string.app_name),
            modifier = Modifier.weight(1f),
            color = Colors.Black,
            fontWeight = FontWeight.W600,
            style = Typography.titleLarge
        )
    }
}

@Composable
private fun ProfileCard(
    modifier: Modifier,
    myInfo: MyInfo
) {
    Row(
        modifier = modifier
            .background(
                Brush.horizontalGradient(listOf(Colors.PrimaryLight, Colors.FF50A7F3)),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = myInfo.imageUrl,
            contentDescription = null,
            placeholder = painterResource(R.drawable.ic_person),
            error = painterResource(R.drawable.ic_person),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .size(68.dp)
        )

        HorizontalSpacer(12.dp)

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = myInfo.displayName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Colors.White,
                style = Typography.displaySmall
            )

            Text(
                text = stringResource(
                    R.string.home_profile_remain_time_minute,
                    myInfo.remainTime.remainingMinutes
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Colors.White,
                style = Typography.bodyLarge
            )

            VerticalSpacer(4.dp)

            MembershipBadge(
                plan = myInfo.membershipPlan
            )
        }
    }
}

@Composable
private fun HomeBody(
    modifier: Modifier,
    currentRoomInfo: CurrentRoomInfo?,
    previewMaxSize: Int,
    callHistoryList: ImmutableList<CallHistory>,
    onClickHistoryMore: () -> Unit,
    onClickCreateRoom: () -> Unit,
    onClickJoinCall: () -> Unit,
    onClickCurrentRoom: (RoomInfo) -> Unit,
    onClickHistory: (CallHistory) -> Unit
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        if(currentRoomInfo == null) {
            FilledActionButton(
                text = stringResource(R.string.home_call_start),
                icon = painterResource(R.drawable.ic_home),
                containerColor = Colors.PrimaryLight,
                contentColor = Colors.White,
                paddingValues = PaddingValues(12.dp),
                iconSize = 34.dp,
                onClick = onClickCreateRoom,
            )

            VerticalSpacer(12.dp)

            FilledActionButton(
                text = stringResource(R.string.home_call_join),
                icon = painterResource(R.drawable.ic_home),
                containerColor = Colors.Secondary,
                contentColor = Colors.White,
                iconSize = 34.dp,
                paddingValues = PaddingValues(12.dp),
                onClick = onClickJoinCall,
            )
        } else {
            CurrentRoomCard(currentRoomInfo = currentRoomInfo, onClickCurrentRoom = onClickCurrentRoom)
        }

        VerticalSpacer(24.dp)

        Text(
            text = stringResource(R.string.home_call_history),
            color = Colors.Black,
            style = Typography.titleLarge
        )

        VerticalSpacer(8.dp)

        if (callHistoryList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Colors.Gray100, shape = RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.home_call_history_empty),
                    color = Colors.Gray500,
                    style = Typography.bodyLarge
                )
            }
        } else {
            CallHistorySection(
                callHistoryList = callHistoryList,
                previewMaxSize = previewMaxSize,
                onClickHistoryMore = onClickHistoryMore,
                onClickHistory = onClickHistory
            )
        }
    }
}

@Composable
private fun ColumnScope.CallHistorySection(
    callHistoryList: ImmutableList<CallHistory>,
    previewMaxSize: Int,
    onClickHistoryMore: () -> Unit,
    onClickHistory: (CallHistory) -> Unit
) {
    callHistoryList.take(previewMaxSize).forEach { history ->
        key(history.historyId) {
            CallHistoryItem(callHistory = history, onClickHistory = onClickHistory)

            VerticalSpacer(8.dp)
        }
    }

    if (callHistoryList.size > previewMaxSize) {
        Row(
            modifier = Modifier
                .align(Alignment.End)
                .noRippleClickable(onClick = onClickHistoryMore),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.common_more),
                color = Colors.PrimaryLight,
                style = Typography.bodyLarge,
            )

            HorizontalSpacer(2.dp)

            Icon(
                painter = painterResource(R.drawable.ic_home),
                contentDescription = null,
                tint = Colors.PrimaryLight,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}


@Composable
@Preview(showBackground = false)
private fun HomePreviewMirror() {
    HomePreview()
}

@Composable
@Preview(showBackground = false)
private fun HomePreviewEmptyMirror() {
    HomePreviewEmpty()
}