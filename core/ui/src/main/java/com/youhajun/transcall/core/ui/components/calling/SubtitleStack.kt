package com.youhajun.transcall.core.ui.components.calling

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youhajun.core.model.conversation.Conversation
import com.youhajun.transcall.core.ui.components.VerticalSpacer
import kotlinx.coroutines.delay

@Composable
fun SubtitleStack(
    conversation: Conversation?,
    modifier: Modifier = Modifier,
    maxStackSize: Int = 3,
    displayDurationMillis: Long = 3000L
) {
    val subtitleList = remember { mutableStateListOf<Conversation>() }

    LaunchedEffect(conversation?.id) {
        conversation?.let { newConv ->
            if (subtitleList.size >= maxStackSize) {
                subtitleList.removeAt(1)
            }
            subtitleList.add(newConv)
            delay(displayDurationMillis)
            subtitleList.remove(newConv)
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        subtitleList.forEach { conversation ->
            key(conversation.id) {
                Box(
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "[${conversation.senderInfo.displayName}] ${conversation.transText ?: conversation.originText}",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
                VerticalSpacer(4.dp)
            }
        }
    }
}
