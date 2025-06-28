package com.youhajun.feature.call.impl.model

sealed interface CallingScreenType {

    data class FloatingAndFull(
        val floatingCallUser: CallUserUiModel,
        val fullCallUser: CallUserUiModel,
    ): CallingScreenType

    data object Grid: CallingScreenType
}