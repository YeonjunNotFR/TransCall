package com.youhajun.feature.call.calling

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.youhajun.core.model.calling.payload.ChangedRoom
import com.youhajun.core.model.calling.payload.ConnectedRoom
import com.youhajun.core.model.room.RoomInfo
import com.youhajun.core.model.room.RoomStatus
import com.youhajun.domain.conversation.usecase.GetRecentConversationFlowUseCase
import com.youhajun.domain.room.usecase.GetRoomInfoUseCase
import com.youhajun.domain.room.usecase.GetRoomParticipantFlowUseCase
import com.youhajun.domain.user.usecase.GetMyInfoUseCase
import com.youhajun.feature.call.api.CallNavRoute
import com.youhajun.feature.call.model.CallControlAction
import com.youhajun.feature.call.model.CallUserUiModel
import com.youhajun.feature.call.model.CallingScreenType
import com.youhajun.feature.call.api.service.CallServiceContract
import com.youhajun.webrtc.model.stream.AudioDeviceType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class CallingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMyInfoUseCase: GetMyInfoUseCase,
    private val getRoomInfoUseCase: GetRoomInfoUseCase,
    private val getRoomParticipantFlowUseCase: GetRoomParticipantFlowUseCase,
    private val getRecentConversationFlowUseCase: GetRecentConversationFlowUseCase
) : ContainerHost<CallingState, CallingSideEffect>, ViewModel() {

    override val container: Container<CallingState, CallingSideEffect> = container(CallingState()) {
        supervisorScope {
            launch { initMyInfo() }
            launch { initRoomInfo() }
            launch { collectRoomParticipants(roomId) }
            launch { collectRecentConversation(roomId) }
        }
    }

    private val roomId: String by lazy {
        savedStateHandle.toRoute<CallNavRoute.Calling>().roomId
    }

    private var serviceContractJob: Job? = null
    private var callServiceContract: CallServiceContract? = null

    fun setCallServiceContract(contract: CallServiceContract?) = intent {
        if (callServiceContract == contract) return@intent
        serviceContractJob?.cancel()
        callServiceContract = contract

        if (contract == null) return@intent

        serviceContractJob = supervisorScope {
            launch { callingMessageCollect() }
            launch { callServiceMediaUsersCollect() }
        }
    }

    fun onEnterPipMode() = intent {
        val screenType = state.callingScreenType.toPipMode()
        reduce { state.copy(callingScreenType = screenType) }
    }

    fun onExitPipMode() = intent {
        val pipScreenType = state.callingScreenType as? CallingScreenType.PipMode ?: return@intent
        reduce { state.copy(callingScreenType = pipScreenType.popupScreenType) }
    }

    fun onSelectAudioDevice(deviceType: AudioDeviceType) {
        callServiceContract?.setAudioDeviceChange(deviceType)
    }

    fun onDismissAudioDeviceSelector() = intent {
        reduce { state.copy(isShowAudioDeviceChangeDialog = false) }
    }

    fun onClickRoomCodeCopy() {

    }

    fun onClickRoomCodeShare() {

    }

    fun onClickCallAction(action: CallControlAction) {
        when (action) {
            CallControlAction.LeaveCall -> callServiceContract?.leaveCall()
            is CallControlAction.FlipCamera -> callServiceContract?.flipCamera()
            is CallControlAction.ToggleCameraEnable -> callServiceContract?.setCameraEnabled(!action.isCameraEnabled)
            is CallControlAction.ToggleMicEnable -> callServiceContract?.setMicEnabled(!action.isMicEnabled)
            is CallControlAction.SelectAudioDevice -> showAudioDeviceChangeDialog()
        }
    }

    fun onTapCallingScreen() = intent {
        reduce { state.copy(isShowBottomCallController = !state.isShowBottomCallController) }
    }

    fun onDoubleTapGrid(targetCallUser: CallUserUiModel) = intent {
        val myDefault = state.myDefaultCallUser ?: return@intent
        val floatingMediaKey = if (targetCallUser.mediaKey == myDefault.mediaKey) {
            state.callUserUiModelList.firstOrNull { it.mediaKey != myDefault.mediaKey }?.mediaKey ?: return@intent
        } else {
            myDefault.mediaKey
        }

        reduce {
            state.copy(
                callingScreenType = CallingScreenType.FloatingAndFull(
                    fullMediaKey = targetCallUser.mediaKey,
                    floatingMediaKey = floatingMediaKey
                )
            )
        }
    }

    fun onDoubleTapFloating() = intent {
        val current = state.callingScreenType as? CallingScreenType.FloatingAndFull ?: return@intent
        reduce {
            state.copy(
                callingScreenType = CallingScreenType.FloatingAndFull(
                    fullMediaKey = current.floatingMediaKey,
                    floatingMediaKey = current.fullMediaKey
                )
            )
        }
    }

    fun onDoubleTapFull() = intent {
        reduce { state.copy(callingScreenType = CallingScreenType.Grid) }
    }

    private fun initMyInfo() = intent {
        getMyInfoUseCase().onSuccess {
            reduce { state.copy(myUserId = it.userId) }
        }
    }

    private fun initRoomInfo() = intent {
        getRoomInfoUseCase(roomId).onSuccess {
            handleRoomInfo(it)
        }
    }

    @OptIn(OrbitExperimental::class)
    private suspend fun collectRoomParticipants(roomId: String) = subIntent {
        repeatOnSubscription {
            getRoomParticipantFlowUseCase(roomId).collect {
                val participantsMap = it.associateBy { it.userId }.toImmutableMap()
                reduce { state.copy(participantsMap = participantsMap) }
            }
        }
    }

    @OptIn(OrbitExperimental::class)
    private suspend fun collectRecentConversation(roomId: String) = subIntent {
        repeatOnSubscription {
            getRecentConversationFlowUseCase(roomId).collect {
                reduce { state.copy(recentConversation = it) }
            }
        }
    }

    @OptIn(OrbitExperimental::class)
    private suspend fun callingMessageCollect() = subIntent {
        repeatOnSubscription {
            callServiceContract?.messageFlow?.collect {
                when (val type = it.payload) {
                    is ConnectedRoom -> {
                        handleRoomInfo(type.roomInfo)
                    }

                    is ChangedRoom -> {
                        handleRoomInfo(type.roomInfo)
                    }

                    else -> Unit
                }
            }
        }
    }

    @OptIn(OrbitExperimental::class)
    private suspend fun handleRoomInfo(roomInfo: RoomInfo) = subIntent {
        val isSameStatus = state.roomInfo.status == roomInfo.status
        val isPipMode = state.callingScreenType is CallingScreenType.PipMode
        val screenType = if (isSameStatus) state.callingScreenType else roomInfo.status.toScreenType(isPipMode)

        reduce {
            state.copy(
                roomInfo = roomInfo,
                callingScreenType = screenType
            )
        }
    }

    @OptIn(OrbitExperimental::class)
    private suspend fun callServiceMediaUsersCollect() = subIntent {
        repeatOnSubscription {
            callServiceContract?.mediaUsersFlow?.collect {
                reduce { state.copy(callMediaUsers = it.toImmutableList()) }
            }
        }
    }

    private fun showAudioDeviceChangeDialog() = intent {
        reduce { state.copy(isShowAudioDeviceChangeDialog = true) }
    }

    private fun RoomStatus.toScreenType(isPipMode: Boolean) = when (this) {
        RoomStatus.WAITING -> if (isPipMode) CallingScreenType.Waiting.toPipMode() else CallingScreenType.Waiting
        RoomStatus.IN_PROGRESS -> if (isPipMode) CallingScreenType.Grid.toPipMode() else CallingScreenType.Grid
        RoomStatus.ENDED -> CallingScreenType.ENDED
    }

    private fun CallingScreenType.toPipMode(): CallingScreenType = when (this) {
        is CallingScreenType.ENDED -> CallingScreenType.ENDED
        is CallingScreenType.FloatingAndFull -> {
            CallingScreenType.PipMode(
                popupScreenType = this,
                pipFirstMediaKey = this.fullMediaKey,
                pipSecondMediaKey = this.floatingMediaKey,
            )
        }

        CallingScreenType.Grid -> {
            val state = container.stateFlow.value
            val myDefault = state.myDefaultCallUser ?: return this
            val otherMedia = state.callUserUiModelList.firstOrNull { it.mediaKey != myDefault.mediaKey } ?: return this
            CallingScreenType.PipMode(
                popupScreenType = this,
                pipFirstMediaKey = myDefault.mediaKey,
                pipSecondMediaKey = otherMedia.mediaKey,
            )
        }

        CallingScreenType.Waiting -> {
            val state = container.stateFlow.value
            val myDefaultMediaKey = state.myDefaultCallUser?.mediaKey ?: return this
            CallingScreenType.PipMode(
                popupScreenType = this,
                pipFirstMediaKey = myDefaultMediaKey,
                pipSecondMediaKey = null,
            )
        }

        is CallingScreenType.PipMode -> this
    }
}