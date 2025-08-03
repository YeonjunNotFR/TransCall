package com.youhajun.feature.home.impl

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youhajun.core.model.pagination.CursorPageRequest
import com.youhajun.core.route.NavigationEvent
import com.youhajun.domain.history.usecase.GetHistoryListUseCase
import com.youhajun.domain.room.usecase.CreateRoomUseCase
import com.youhajun.domain.room.usecase.JoinRoomUseCase
import com.youhajun.domain.user.usecase.GetMyInfoUseCase
import com.youhajun.feature.history.api.HistoryNavRoute
import com.youhajun.room.api.RoomNavRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getMyInfoUseCase: GetMyInfoUseCase,
    private val joinRoomUseCase: JoinRoomUseCase,
    private val getHistoryListUseCase: GetHistoryListUseCase,
) : ContainerHost<HomeState, HomeSideEffect>, ViewModel() {

    override val container: Container<HomeState, HomeSideEffect> = container(HomeState(callHistoryPreviewMaxSize = CALL_HISTORY_PREVIEW_MAX_SIZE,)) {
        onInit()
    }

    fun onClickCreateRoom() {
        val event = NavigationEvent.Navigate(route = RoomNavRoute.CreateRoom, launchSingleTop = true)
        intent {
            postSideEffect(HomeSideEffect.Navigation(event))
        }
    }

    fun onClickJoinCall() {
        intent {
            reduce { state.copy(isShowJoinBottomSheet = true) }
        }
    }

    fun onClickHistoryMore() {
        val event = NavigationEvent.NavigateBottomBar(route = HistoryNavRoute.HistoryList, launchSingleTop = true)
        intent {
            postSideEffect(HomeSideEffect.Navigation(event))
        }
    }

    fun onChangedJoinBottomSheetVisibility(isVisible: Boolean) {
        intent {
            reduce { state.copy(isShowJoinBottomSheet = isVisible) }
        }
    }

    fun onClickJoinBottomSheetCancel() {
        intent {
            reduce { state.copy(isShowJoinBottomSheet = false) }
        }
    }

    fun onClickJoinBottomSheetConfirm(roomId: String) {
        viewModelScope.launch {
            joinRoomUseCase(roomId)
                .onSuccess { roomInfo ->
                    intent {
                        postSideEffect(HomeSideEffect.GoToCall(roomInfo.code))
                    }
                }
        }
    }

    private fun onInit() {
        getHistoryList()
        getMyInfo()
    }

    private fun getHistoryList() = viewModelScope.launch {
        val request = CursorPageRequest(first = 3)
        getHistoryListUseCase(request).onSuccess { historyList ->
            val histories = historyList.edges.map { it.node }.toImmutableList()
            intent {
                reduce { state.copy(callHistoryList = histories) }
            }
        }
    }

    private fun getMyInfo() = viewModelScope.launch {
        getMyInfoUseCase().onSuccess {
            intent {
                reduce { state.copy(myInfo = it) }
            }
        }
    }

    companion object {
        private const val CALL_HISTORY_PREVIEW_MAX_SIZE = 3
    }
}
