package com.youhajun.transcall.core.ui.components.calling

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youhajun.core.model.calling.payload.TranslationMessage
import com.youhajun.core.model.room.Participant
import com.youhajun.transcall.core.ui.components.VerticalSpacer
import kotlinx.coroutines.delay

@Composable
fun SubtitleStack(
    participant: Participant,
    conversation: TranslationMessage,
    modifier: Modifier = Modifier,
    maxStackSize: Int = 3,
    displayDurationMillis: Long = 3000L
) {
    val subtitleList = remember { mutableStateListOf<TranslationMessage>() }
    val animationDuration = 500L

    LaunchedEffect(conversation.conversationId) {
        if (subtitleList.size >= maxStackSize) {
            subtitleList.removeAt(0)
        }
        subtitleList.add(conversation)
    }


    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        subtitleList.forEach { conversation ->
            key(conversation.conversationId) {
                var visible by remember { mutableStateOf(true) }

                LaunchedEffect(conversation.conversationId) {
                    delay(displayDurationMillis)
                    visible = false
                    delay(animationDuration)
                    subtitleList.remove(conversation)
                }

                AnimatedVisibility(
                    visible = visible,
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "[${participant.displayName}] ${conversation.transText ?: conversation.originText}",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                    VerticalSpacer(4.dp)
                }
            }
        }
    }
}
