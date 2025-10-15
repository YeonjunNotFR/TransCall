package com.youhajun.feature.call.model

sealed interface CallingScreenType {

    data object Waiting: CallingScreenType

    data class FloatingAndFull(
        val floatingMediaKey: String,
        val fullMediaKey: String,
    ): CallingScreenType

    data object Grid: CallingScreenType

    data class PipMode(
        val popupScreenType: CallingScreenType,
        val pipFirstMediaKey: String,
        val pipSecondMediaKey: String?,
    ): CallingScreenType

    data object ENDED : CallingScreenType
}