package com.youhajun.feature.history.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.youhajun.core.model.TimeRange
import com.youhajun.core.model.history.CallHistory
import com.youhajun.core.model.calling.payload.TranslationMessage
import com.youhajun.core.model.room.Participant
import com.youhajun.domain.conversation.usecase.GetConversationPagingDataFlowUseCase
import com.youhajun.domain.history.usecase.GetHistoryDetailUseCase
import com.youhajun.domain.user.usecase.GetMyInfoUseCase
import com.youhajun.feature.history.api.HistoryNavRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class HistoryDetailViewModel @Inject constructor(
    private val getHistoryDetailUseCase: GetHistoryDetailUseCase,
    private val getConversationPagingDataFlowUseCase: GetConversationPagingDataFlowUseCase,
    private val getMyInfoUseCase: GetMyInfoUseCase,
    private val savedStateHandle: SavedStateHandle
) : ContainerHost<HistoryDetailState, HistoryDetailSideEffect>, ViewModel() {

    override val container: Container<HistoryDetailState, HistoryDetailSideEffect> = container(HistoryDetailState()) {
        supervisorScope {
            launch { getMyInfo() }
            launch { getHistoryDetail() }
        }
    }

    private val historyId: String by lazy {
        savedStateHandle.toRoute<HistoryNavRoute.HistoryDetail>().historyId
    }

    private val historyStateFlow: MutableStateFlow<CallHistory?> = MutableStateFlow(null)

    val messages: Flow<PagingData<TranslationMessage>> =
        historyStateFlow
            .mapNotNull { it?.toParams() }
            .distinctUntilChanged()
            .flatMapLatest { (roomId, timeRange) ->
                getConversationPagingDataFlowUseCase(
                    roomId = roomId,
                    timeRange = timeRange,
                    pageSize = CONVERSATION_SIZE_PER_PAGE,
                    prefetchDistance = 2,
                    initialLoadSize = CONVERSATION_SIZE_PER_PAGE
                )
            }
            .cachedIn(viewModelScope)

    fun onClickParticipant(participant: Participant) {

    }

    fun onClickViewMemo() {

    }

    fun onClickViewSummary() {

    }

    @OptIn(OrbitExperimental::class)
    private suspend fun getMyInfo() = subIntent {
        getMyInfoUseCase().onSuccess {
            reduce { state.copy(myUserId = it.userId) }
        }
    }

    @OptIn(OrbitExperimental::class)
    private suspend fun getHistoryDetail() = subIntent {
        getHistoryDetailUseCase(historyId).onSuccess {
            reduce { state.copy(history = it) }
            historyStateFlow.value = it
        }
    }

    private fun CallHistory.toParams(): Pair<String, TimeRange> {
        val range = TimeRange(joinedAtToEpochTime, leftAtToEpochTime)
        return roomId to range
    }

    companion object {
        private const val CONVERSATION_SIZE_PER_PAGE = 20
    }

}
