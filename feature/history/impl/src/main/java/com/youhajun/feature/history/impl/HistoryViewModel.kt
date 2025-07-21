package com.youhajun.feature.history.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youhajun.core.model.DateRange
import com.youhajun.core.model.calling.CallHistory
import com.youhajun.core.model.pagination.CursorPageRequest
import com.youhajun.core.model.pagination.CursorPagingState
import com.youhajun.domain.history.usecase.GetHistoryListUseCase
import com.youhajun.transcall.core.ui.util.DateFormatPatterns
import com.youhajun.transcall.core.ui.util.toUiDateString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject


@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getHistoryListUseCase: GetHistoryListUseCase,
) : ContainerHost<HistoryState, HistorySideEffect>, ViewModel() {

    override val container: Container<HistoryState, HistorySideEffect> = container(HistoryState()) {
        onInit()
    }

    private var historyPagingState = CursorPagingState(
        request = CursorPageRequest(first = CALL_HISTORY_SIZE_PER_PAGE)
    )

    fun onHistoryNextPage() {
        viewModelScope.launch {
            getHistoryList()
        }
    }

    fun onClickDateRange(dateRange: DateRange) {
        intent {
            reduce { state.copy(selectedDateRange = dateRange) }
        }
    }

    private fun onInit() {
        viewModelScope.launch {
            getHistoryList()
        }
    }

    private suspend fun getHistoryList() {
        if (historyPagingState.canLoadMore()) return
        val isFirstCall = historyPagingState.isFirstCall()
        getHistoryListUseCase(historyPagingState.request).onSuccess {
            historyPagingState = historyPagingState.next(it)
        }.map { it.edges.map { it.node } }.onSuccess {
            val newHistoryMap = groupByDate(it)
            val oldHistoryMap = container.stateFlow.value.callHistoryDateMap
            val mergedMap = if(isFirstCall) newHistoryMap else getMergedHistoryMap(oldHistoryMap, newHistoryMap)

            intent {
                reduce { state.copy(callHistoryDateMap = mergedMap.toImmutableMap()) }
            }
        }
    }

    private fun getMergedHistoryMap(
        oldMap: Map<String, ImmutableList<CallHistory>>,
        newMap: Map<String, ImmutableList<CallHistory>>
    ): Map<String, ImmutableList<CallHistory>> {
        return buildMap {
            putAll(oldMap)

            newMap.forEach { (date, newList) ->
                val existingList = get(date).orEmpty()
                val mergedList = existingList + newList
                put(date, mergedList.toImmutableList())
            }
        }
    }

    private fun groupByDate(historyList: List<CallHistory>): Map<String, ImmutableList<CallHistory>> {
        return historyList
            .groupBy { it.startedAtToEpochTime.toUiDateString(DateFormatPatterns.DATE_ONLY) }
            .mapValues { it.value.toImmutableList() }
    }

    companion object {
        private const val CALL_HISTORY_SIZE_PER_PAGE = 10
    }
}
