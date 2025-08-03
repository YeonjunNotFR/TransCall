package com.youhajun.feature.call.impl.calling

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youhajun.domain.conversation.usecase.ObserveRecentConversation
import com.youhajun.domain.room.usecase.RoomInfoUseCase
import com.youhajun.feature.call.impl.model.CallControlAction
import com.youhajun.feature.call.impl.model.CallUserUiModel
import com.youhajun.feature.call.impl.model.CallingScreenType
import com.youhajun.feature.call.impl.service.CallServiceContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class CallingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getRoomInfoUseCase: RoomInfoUseCase,
    private val observeRecentConversation: ObserveRecentConversation
) : ContainerHost<CallingState, CallingSideEffect>, ViewModel() {

    companion object {
        const val INTENT_KEY_ROOM_ID = "room_id"
    }

    override val container: Container<CallingState, CallingSideEffect> = container(CallingState()) {
        onInit()
    }

    private val roomId: String by lazy {
        checkNotNull(savedStateHandle[INTENT_KEY_ROOM_ID])
    }

    private var callServiceContract: CallServiceContract? = null

    fun setCallServiceContract(contract: CallServiceContract?) {
        callServiceContract = contract
        callServiceMediaUsersCollect()
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
            val myCamera = state.myCameraCallUser ?: return@intent
            val floatingUser = if(targetCallUser.mediaUser.key == myCamera.mediaUser.key) {
                state.callUserUiModelList.firstOrNull { it.mediaUser.key != myCamera.mediaUser.key } ?: return@intent
            } else {
                myCamera
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

    private fun onInit() {
        getRoomInfo()
        recentConversationCollect()
    }

    private fun getRoomInfo() {
        viewModelScope.launch {
            getRoomInfoUseCase(roomId).onSuccess {
                intent {
                    reduce {
                        state.copy(roomInfo = it)
                    }
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

    private fun callServiceMediaUsersCollect() {
        viewModelScope.launch {
            callServiceContract?.mediaUsersFlow?.collect {
                intent {
                    reduce {
                        state.copy(callMediaUsers = it.toImmutableList())
                    }
                }
            }
        }
    }

    private fun showAudioDeviceChangeDialog() {
        intent {
            reduce { state.copy(isShowAudioDeviceChangeDialog = true) }
        }
    }
}