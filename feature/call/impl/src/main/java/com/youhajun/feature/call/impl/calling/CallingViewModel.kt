package com.youhajun.feature.call.impl.calling

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.youhajun.core.model.calling.payload.ChangedRoom
import com.youhajun.core.model.calling.payload.ConnectedRoom
import com.youhajun.core.model.room.CurrentParticipant
import com.youhajun.core.model.room.RoomInfo
import com.youhajun.core.model.room.RoomStatus
import com.youhajun.domain.conversation.usecase.ObserveRecentConversation
import com.youhajun.domain.user.usecase.GetMyInfoUseCase
import com.youhajun.feature.call.api.CallNavRoute
import com.youhajun.feature.call.impl.model.CallControlAction
import com.youhajun.feature.call.impl.model.CallUserUiModel
import com.youhajun.feature.call.impl.model.CallingScreenType
import com.youhajun.feature.call.impl.service.CallServiceContract
import com.youhajun.webrtc.model.CallMediaUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class CallingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMyInfoUseCase: GetMyInfoUseCase,
    private val observeRecentConversation: ObserveRecentConversation
) : ContainerHost<CallingState, CallingSideEffect>, ViewModel() {

    override val container: Container<CallingState, CallingSideEffect> = container(CallingState()) {
        initMyInfo()
        recentConversationCollect()
    }

    private val roomId: String by lazy {
        savedStateHandle.toRoute<CallNavRoute.Calling>().roomId
    }

    private var serviceContractJob: Job? = null
    private var callServiceContract: CallServiceContract? = null

    fun setCallServiceContract(contract: CallServiceContract?) {
        if (callServiceContract == contract) return
        callServiceContract = contract

        viewModelScope.launch {
            serviceContractJob?.cancelAndJoin()
            serviceContractJob = null

            if (contract == null) return@launch

            serviceContractJob = launch {
                supervisorScope {
                    launch { callingMessageCollect() }
                    launch { callServiceMediaUsersCollect() }
                }
            }
        }
    }

    fun onClickRoomCodeCopy() {

    }

    fun onClickRoomCodeShare() {

    }

    fun onClickCallAction(action: CallControlAction) {
        when(action) {
            CallControlAction.CallingLeft -> callServiceContract?.callingLeft()
            is CallControlAction.FlipCamera -> callServiceContract?.flipCamera()
            is CallControlAction.ToggleCameraEnable -> callServiceContract?.setCameraEnabled(action.isCameraEnabled)
            is CallControlAction.ToggleMicMute -> callServiceContract?.setMuteEnable(action.isMute)
            is CallControlAction.SelectAudioDevice -> showAudioDeviceChangeDialog()
        }
    }

    fun onTabCallingScreen() {
        intent {
            reduce {
                state.copy(isShowBottomCallController = !state.isShowBottomCallController)
            }
        }
    }

    fun onDoubleTapGrid(targetCallUser: CallUserUiModel) {
        intent {
            val myDefault = state.myDefaultCallUser ?: return@intent
            val floatingUser = if(targetCallUser.mediaUser.key == myDefault.mediaUser.key) {
                state.callUserUiModelList.firstOrNull { it.mediaUser.key != myDefault.mediaUser.key } ?: return@intent
            } else {
                myDefault
            }

            reduce {
                state.copy(
                    callingScreenType = CallingScreenType.FloatingAndFull(
                        fullCallUser = targetCallUser,
                        floatingCallUser = floatingUser
                    )
                )
            }
        }
    }

    fun onDoubleTapFloating() {
        intent {
            reduce {
                state.copy(callingScreenType = CallingScreenType.Grid)
            }
        }
    }

    fun onDoubleTapFull() {
        intent {
            val current = state.callingScreenType as? CallingScreenType.FloatingAndFull ?: return@intent
            reduce {
                state.copy(
                    callingScreenType = CallingScreenType.FloatingAndFull(
                        fullCallUser = current.floatingCallUser,
                        floatingCallUser = current.fullCallUser
                    )
                )
            }
        }
    }

    private fun initMyInfo() {
        viewModelScope.launch {
            getMyInfoUseCase().onSuccess {
                intent {
                    reduce { state.copy(myUserId = it.userId) }
                }
            }
        }
    }

    private fun recentConversationCollect() {
        viewModelScope.launch {
            observeRecentConversation(roomId).collect { conversation ->
                intent {
                    reduce {
                        state.copy(recentConversation = conversation)
                    }
                }
            }
        }
    }

    private suspend fun callingMessageCollect() {
        callServiceContract?.messageFlow?.collect {
            when(val type = it.payload) {
                is ConnectedRoom -> {
                    handleRoomInfo(type.roomInfo)
                    handleParticipants(type.participants)
                }
                is ChangedRoom -> {
                    handleRoomInfo(type.roomInfo)
                    handleParticipants(type.participants)
                }
                else -> Unit
            }
        }
    }

    private fun handleParticipants(participants: ImmutableList<CurrentParticipant>) = intent {
        reduce {
            state.copy(participants = participants)
        }
    }

    private fun handleRoomInfo(roomInfo: RoomInfo) = intent {
        val isSameStatus = state.roomInfo.status == roomInfo.status
        val screenType = if (isSameStatus) state.callingScreenType else roomInfo.status.toScreenType()

        reduce {
            state.copy(
                roomInfo = roomInfo,
                callingScreenType = screenType
            )
        }
    }

    private suspend fun callServiceMediaUsersCollect() {
        callServiceContract?.mediaUsersFlow?.collect {
            intent {
                reduce {
                    state.copy(callMediaUsers = it.toImmutableList())
                }
            }
        }
    }

    private fun showAudioDeviceChangeDialog() {
        intent {
            reduce { state.copy(isShowAudioDeviceChangeDialog = true) }
        }
    }

    private fun RoomStatus.toScreenType() = when (this) {
        RoomStatus.WAITING -> CallingScreenType.Waiting
        RoomStatus.IN_PROGRESS -> CallingScreenType.Grid
        RoomStatus.ENDED -> CallingScreenType.Summary
    }
}