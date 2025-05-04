package com.youhajun.feature.call.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.youhajun.feature.call.api.CallNavGraphRegistrar
import com.youhajun.feature.call.api.CallNavRoute
import javax.inject.Inject

internal class CallNavGraphRegistrarImpl @Inject constructor() : CallNavGraphRegistrar {
    override fun register(builder: NavGraphBuilder) {
        builder.composable<CallNavRoute.Calling> {

        }

        builder.composable<CallNavRoute.Wait> {

        }
    }
}