package com.youhajun.feature.home.impl

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youhajun.core.model.pagination.CursorPageRequest
import com.youhajun.core.route.NavigationEvent
import com.youhajun.domain.history.usecase.GetHistoryListUseCase
import com.youhajun.domain.room.usecase.CreateRoomUseCase
import com.youhajun.domain.room.usecase.JoinRoomUseCase
import com.youhajun.feature.history.api.HistoryNavRoute
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
    private val createRoomUseCase: CreateRoomUseCase,
    private val joinRoomUseCase: JoinRoomUseCase,
    private val getHistoryListUseCase: GetHistoryListUseCase,
) : ContainerHost<HomeState, HomeSideEffect>, ViewModel() {

    override val container: Container<HomeState, HomeSideEffect> = container(HomeState(
        callHistoryPreviewMaxSize = CALL_HISTORY_PREVIEW_MAX_SIZE,
    )) {
        onInit()
    }

    fun onClickStartCall() {
        viewModelScope.launch {
            createRoomUseCase()
                .onSuccess { roomInfo ->
                    intent {
                        postSideEffect(HomeSideEffect.GoToCall(roomInfo.code))
                    }
                }
        }
    }

    fun onClickJoinCall() {
        intent {
            reduce { state.copy(isShowJoinBottomSheet = true) }
        }
    }

    fun onClickHistoryMore() {
        intent {
            postSideEffect(HomeSideEffect.Navigation(
                NavigationEvent.NavigateBottomBar(
                    route = HistoryNavRoute.HistoryList,
                    launchSingleTop = true
                )
            ))
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

    fun onClickJoinBottomSheetConfirm(roomCode: String) {
        viewModelScope.launch {
            joinRoomUseCase(roomCode)
                .onSuccess { roomInfo ->
                    intent {
                        postSideEffect(HomeSideEffect.GoToCall(roomInfo.code))
                    }
                }
        }
    }

    private fun onInit() {
        viewModelScope.launch {
            getHistoryList()
        }
    }

    private suspend fun getHistoryList() {
        val request = CursorPageRequest(first = 3)
        getHistoryListUseCase(request).onSuccess { historyList ->
            val histories = historyList.edges.map { it.node }.toImmutableList()
            intent {
                reduce { state.copy(callHistoryList = histories) }
            }
        }
    }

    companion object {
        private const val CALL_HISTORY_PREVIEW_MAX_SIZE = 3
    }
}
