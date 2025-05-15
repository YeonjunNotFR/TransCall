package com.youhajun.feature.history.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youhajun.core.model.CallHistory
import com.youhajun.core.model.DateRange
import com.youhajun.core.model.pagination.OffsetPageRequest
import com.youhajun.core.model.pagination.OffsetPagingState
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

    private var pagingState = OffsetPagingState(
        request = OffsetPageRequest(offset = 0, limit = CALL_HISTORY_SIZE_PER_PAGE),
        isLastPage = false
    )

    fun onHistoryNextPage() {
        viewModelScope.launch {
            getHistoryList()
        }
    }

    fun onClickCallAgain(userId: String) {

    }

    fun onClickDateRange(dateRange: DateRange) {
        intent {
            reduce { state.copy(selectedDateRange = dateRange) }
        }
    }

    fun onClickFavorite() {

    }

    private fun onInit() {
        viewModelScope.launch {
            getHistoryList()
        }
    }

    private suspend fun getHistoryList() {
        if (pagingState.canLoadMore()) return
        getHistoryListUseCase(pagingState.request).onSuccess { historyList ->
            val newHistoryMap = groupByDate(historyList.data)
            val oldHistoryMap = container.stateFlow.value.callHistoryDateMap
            val mergedMap = if(pagingState.isFirstCall()) newHistoryMap else getMergedHistoryMap(oldHistoryMap, newHistoryMap)
            pagingState.next(historyList)
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
                val existingList = get(date) ?: emptyList()
                val mergedList = existingList + newList
                put(date, mergedList.toImmutableList())
            }
        }
    }

    private fun groupByDate(historyList: List<CallHistory>): Map<String, ImmutableList<CallHistory>> {
        return historyList
            .groupBy { it.startedAtEpochSeconds.toUiDateString(DateFormatPatterns.DATE_ONLY) }
            .mapValues { it.value.toImmutableList() }
    }

    companion object {
        private const val CALL_HISTORY_SIZE_PER_PAGE = 10
    }
}
