package com.youhajun.feature.room.list

import androidx.lifecycle.ViewModel
import com.youhajun.core.model.SortDirection
import com.youhajun.core.model.pagination.CursorPageRequest
import com.youhajun.core.model.pagination.CursorPagingState
import com.youhajun.core.model.room.CurrentRoomListRequest
import com.youhajun.core.model.room.RoomInfo
import com.youhajun.core.route.NavigationEvent
import com.youhajun.domain.room.usecase.GetCurrentRoomListUseCase
import com.youhajun.domain.room.usecase.JoinRoomCheckByCodeUseCase
import com.youhajun.room.api.RoomNavRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class RoomListViewModel @Inject constructor(
    private val getCurrentRoomListUseCase: GetCurrentRoomListUseCase,
    private val joinRoomCheckByCodeUseCase: JoinRoomCheckByCodeUseCase
) : ContainerHost<RoomListState, RoomListSideEffect>, ViewModel() {

    override val container: Container<RoomListState, RoomListSideEffect> = container(RoomListState()) {
        onInit(SortDirection.DESC, SortDirection.DESC)
    }

    private var permissionPendingRoomInfo: RoomInfo? = null

    private var roomPagingState = CursorPagingState(
        request = CursorPageRequest(first = ROOM_SIZE_PER_PAGE)
    )

    fun onRoomNextPage() = intent {
        getRoomList(state.participantSort, state.createdAtSort)
    }

    fun onChangeParticipantCountSort() = intent {
        onInit(state.participantSort.toggle(), state.createdAtSort)
    }

    fun onChangeCreatedAtSort() = intent {
        onInit(state.participantSort, state.createdAtSort.toggle())
    }

    fun onClickCreateRoom() = intent {
        val event = NavigationEvent.NavigateAndSave(RoomNavRoute.CreateRoom, true, RoomNavRoute.RoomList)
        postSideEffect(RoomListSideEffect.Navigation(event))
    }

    fun onClickRoom(roomInfo: RoomInfo) = intent {
        permissionPendingRoomInfo = roomInfo
        postSideEffect(RoomListSideEffect.PermissionCheck)
    }

    fun onJoinRoomPermissionGranted() = intent {
        val roomInfo = permissionPendingRoomInfo ?: return@intent
        val roomCode = roomInfo.roomCode
        joinRoomCheckByCodeUseCase(roomCode).onSuccess {
            postSideEffect(RoomListSideEffect.GoToCall(it.roomId))
        }
    }

    private suspend fun onInit(participantSort: SortDirection, createdAtSort: SortDirection) {
        roomPagingState = CursorPagingState(request = CursorPageRequest(first = ROOM_SIZE_PER_PAGE))
        getRoomList(participantSort, createdAtSort)
    }

    @OptIn(OrbitExperimental::class)
    private suspend fun getRoomList(participantSort: SortDirection, createdAtSort: SortDirection) = subIntent {
        if (!roomPagingState.canLoadMore()) return@subIntent

        val isFirstCall = roomPagingState.isFirstCall()
        val request = CurrentRoomListRequest(participantSort, createdAtSort, roomPagingState.request)
        getCurrentRoomListUseCase(request = request).onSuccess {
            roomPagingState = roomPagingState.next(it)
        }.map { it.edges.map { it.node } }.onSuccess {
            val mergedList = if (isFirstCall) it else state.currentRoomList + it
            reduce {
                state.copy(
                    currentRoomList = mergedList.toImmutableList(),
                    createdAtSort = createdAtSort,
                    participantSort = participantSort
                )
            }
        }
    }

    companion object {
        private const val ROOM_SIZE_PER_PAGE = 15
    }
}
