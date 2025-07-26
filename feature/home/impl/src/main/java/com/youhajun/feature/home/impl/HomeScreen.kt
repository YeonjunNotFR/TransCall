package com.youhajun.feature.home.impl

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.youhajun.core.design.Colors
import com.youhajun.core.design.R
import com.youhajun.core.design.Typography
import com.youhajun.core.model.calling.CallHistory
import com.youhajun.core.model.user.MyInfo
import com.youhajun.core.route.NavigationEvent
import com.youhajun.transcall.core.ui.components.FilledActionButton
import com.youhajun.transcall.core.ui.components.HorizontalSpacer
import com.youhajun.transcall.core.ui.components.MembershipBadge
import com.youhajun.transcall.core.ui.components.VerticalSpacer
import com.youhajun.transcall.core.ui.components.bottomSheet.JoinWithCodeBottomSheet
import com.youhajun.transcall.core.ui.components.history.CallHistoryItem
import com.youhajun.transcall.core.ui.components.modifier.noRippleClickable
import kotlinx.collections.immutable.ImmutableList
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigate: (NavigationEvent) -> Unit
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    viewModel.collectSideEffect {
        when (it) {
            is HomeSideEffect.Navigation -> onNavigate(it.navigationEvent)
            is HomeSideEffect.GoToCall -> Unit
        }
    }

    HomeScreen(
        state = state,
        onClickHistoryMore = viewModel::onClickHistoryMore,
        onClickStartCall = viewModel::onClickStartCall,
        onClickJoinCall = viewModel::onClickJoinCall,
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
    onClickStartCall: () -> Unit,
    onClickJoinCall: () -> Unit,
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
            previewMaxSize = state.callHistoryPreviewMaxSize,
            callHistoryList = state.callHistoryList,
            onClickHistoryMore = onClickHistoryMore,
            onClickStartCall = onClickStartCall,
            onClickJoinCall = onClickJoinCall
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
            style = Typography.titleLarge.copy(
                fontWeight = FontWeight.W600
            )
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
    previewMaxSize: Int,
    callHistoryList: ImmutableList<CallHistory>,
    onClickHistoryMore: () -> Unit,
    onClickStartCall: () -> Unit,
    onClickJoinCall: () -> Unit,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        FilledActionButton(
            text = stringResource(R.string.home_call_start),
            icon = painterResource(R.drawable.ic_home),
            containerColor = Colors.PrimaryLight,
            contentColor = Colors.White,
            paddingValues = PaddingValues(12.dp),
            iconSize = 34.dp,
            onClick = onClickStartCall,
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
                onClickHistoryMore = onClickHistoryMore,
                previewMaxSize = previewMaxSize
            )
        }
    }
}


@Composable
private fun ColumnScope.CallHistorySection(
    callHistoryList: ImmutableList<CallHistory>,
    onClickHistoryMore: () -> Unit,
    previewMaxSize: Int,
) {
    callHistoryList.take(previewMaxSize).forEach { call ->
        key(call.historyId) {
            CallHistoryItem(call)

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