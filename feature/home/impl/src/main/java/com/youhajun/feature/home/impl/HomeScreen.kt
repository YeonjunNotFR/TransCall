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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youhajun.core.model.CallHistory
import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.MembershipPlan
import com.youhajun.core.model.MyInfo
import com.youhajun.core.model.Participant
import com.youhajun.core.model.RemainTime
import com.youhajun.core.ui.R
import com.youhajun.transcall.core.ui.components.CircleAsyncImage
import com.youhajun.transcall.core.ui.components.FilledActionButton
import com.youhajun.transcall.core.ui.components.HorizontalSpacer
import com.youhajun.transcall.core.ui.components.MembershipBadge
import com.youhajun.transcall.core.ui.components.VerticalSpacer
import com.youhajun.transcall.core.ui.components.bottomSheet.JoinWithCodeBottomSheet
import com.youhajun.transcall.core.ui.components.history.CallHistoryItem
import com.youhajun.transcall.core.ui.theme.Colors
import com.youhajun.transcall.core.ui.theme.Typography
import com.youhajun.transcall.core.ui.util.noRippleClickable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigate: (HomeSideEffect.Navigation) -> Unit
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    viewModel.collectSideEffect {
        when (it) {
            is HomeSideEffect.Navigation -> onNavigate(it)
        }
    }

    HomeScreen(
        state = state,
        onClickCallAgain = viewModel::onClickCallAgain,
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
private fun HomeScreen(
    state: HomeState,
    onClickCallAgain: (String) -> Unit,
    onClickHistoryMore: () -> Unit,
    onClickStartCall: () -> Unit,
    onClickJoinCall: () -> Unit,
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
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
            onClickCallAgain = onClickCallAgain,
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
                .size(36.dp),
            painter = painterResource(R.drawable.ic_home),
            contentDescription = null
        )

        HorizontalSpacer(4.dp)

        Text(
            text = stringResource(R.string.app_name),
            modifier = Modifier.weight(1f),
            color = Colors.Black,
            style = Typography.displayMedium
        )

        Icon(
            modifier = Modifier.size(28.dp),
            painter = painterResource(R.drawable.ic_home),
            contentDescription = null
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
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    listOf(Colors.PrimaryLight, Colors.FF50A7F3)
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleAsyncImage(
            imageUrl = myInfo.imageUrl,
            size = 68.dp
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
    onClickCallAgain: (String) -> Unit,
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

        CallHistorySection(
            callHistoryList = callHistoryList,
            onClickCallAgain = onClickCallAgain,
            onClickHistoryMore = onClickHistoryMore,
            previewMaxSize = previewMaxSize
        )
    }
}


@Composable
private fun ColumnScope.CallHistorySection(
    callHistoryList: ImmutableList<CallHistory>,
    onClickCallAgain: (String) -> Unit,
    onClickHistoryMore: () -> Unit,
    previewMaxSize: Int,
) {
    callHistoryList.take(previewMaxSize).forEach { call ->
        CallHistoryItem(call, onClickCallAgain = onClickCallAgain)

        VerticalSpacer(8.dp)
    }

    if (callHistoryList.size > previewMaxSize) {
        Row(
            modifier = Modifier.align(Alignment.End).noRippleClickable(onClick = onClickHistoryMore),
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

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
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