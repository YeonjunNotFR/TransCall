package com.youhajun.feature.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.youhajun.core.model.history.CallHistory
import com.youhajun.core.model.pagination.CursorPageRequest
import com.youhajun.core.model.room.RoomInfo
import com.youhajun.core.route.NavigationEvent
import com.youhajun.domain.history.usecase.GetHistoryListUseCase
import com.youhajun.domain.room.usecase.GetCurrentRoomInfoUseCase
import com.youhajun.domain.room.usecase.JoinRoomCheckByCodeUseCase
import com.youhajun.domain.user.usecase.GetMyInfoUseCase
import com.youhajun.feature.history.api.HistoryNavRoute
import com.youhajun.feature.home.api.HomeNavRoute
import com.youhajun.room.api.RoomNavRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getMyInfoUseCase: GetMyInfoUseCase,
    private val joinRoomCheckByCodeUseCase: JoinRoomCheckByCodeUseCase,
    private val getHistoryListUseCase: GetHistoryListUseCase,
    private val getCurrentRoomInfoUseCase: GetCurrentRoomInfoUseCase
) : ContainerHost<HomeState, HomeSideEffect>, ViewModel() {

    override val container: Container<HomeState, HomeSideEffect> =
        container(HomeState(callHistoryPreviewMaxSize = CALL_HISTORY_PREVIEW_MAX_SIZE))

    private var permissionPendingRoomCode: String? = null
    private var ongoingCallRoomId: String? = null

    fun onResume() = intent {
        supervisorScope {
            launch { getHistoryList() }
            launch { getMyInfo() }
            launch { updateCurrentRoomInfo() }
        }
    }

    fun setOngoingCallRoomId(roomId: String?) = intent {
        ongoingCallRoomId = roomId
        updateCurrentRoomInfo()
    }

    fun onClickCreateRoom() = intent {
        val event = NavigationEvent.Navigate(route = RoomNavRoute.CreateRoom, launchSingleTop = true)
        postSideEffect(HomeSideEffect.Navigation(event))
    }

    fun onClickCurrentRoom(roomInfo: RoomInfo) = intent {
        postSideEffect(HomeSideEffect.GoToCall(roomInfo.roomId))
    }

    fun onClickJoinCall() {
        onChangedJoinBottomSheetVisibility(true)
    }

    fun onClickHistoryMore() = intent {
        val event = NavigationEvent.NavigateBottomBar(
            route = HistoryNavRoute.HistoryList,
            launchSingleTop = true
        )
        postSideEffect(HomeSideEffect.Navigation(event))
    }

    fun onClickJoinBottomSheetCancel() {
        onChangedJoinBottomSheetVisibility(false)
    }

    fun onChangedJoinBottomSheetVisibility(isVisible: Boolean) = intent {
        reduce { state.copy(isShowJoinBottomSheet = isVisible) }
    }

    fun onClickJoinBottomSheetConfirm(roomCode: String) = intent {
        onChangedJoinBottomSheetVisibility(false)
        permissionPendingRoomCode = roomCode
        postSideEffect(HomeSideEffect.CallPermissionCheck)
    }

    fun onJoinRoomPermissionGranted() = intent {
        val roomCode = permissionPendingRoomCode ?: return@intent
        joinRoomCheckByCodeUseCase(roomCode).onSuccess { roomInfo ->
            postSideEffect(HomeSideEffect.GoToCall(roomInfo.roomId))
        }
    }

    fun onClickHistory(callHistory: CallHistory) = intent {
        val destination = HistoryNavRoute.HistoryDetail(callHistory.historyId)
        val event = NavigationEvent.NavigateAndSave(destination, true, HomeNavRoute.Home)
        postSideEffect(HomeSideEffect.Navigation(event))
    }

    @OptIn(OrbitExperimental::class)
    private suspend fun getHistoryList() = subIntent {
        val request = CursorPageRequest(first = CALL_HISTORY_PREVIEW_MAX_SIZE + 1)
        getHistoryListUseCase(request).onSuccess { historyList ->
            val histories = historyList.edges.map { it.node }.toImmutableList()
            reduce { state.copy(callHistoryList = histories) }
        }
    }

    @OptIn(OrbitExperimental::class)
    private suspend fun getMyInfo() = subIntent {
        getMyInfoUseCase().onSuccess {
            reduce { state.copy(myInfo = it) }
        }
    }

    @OptIn(OrbitExperimental::class)
    private suspend fun updateCurrentRoomInfo() = subIntent {
        val currentRoomInfo = ongoingCallRoomId?.let { getCurrentRoomInfoUseCase(it) }?.getOrNull()
        reduce { state.copy(currentRoomInfo = currentRoomInfo) }
    }

    companion object {
        private const val CALL_HISTORY_PREVIEW_MAX_SIZE = 3
    }
}
