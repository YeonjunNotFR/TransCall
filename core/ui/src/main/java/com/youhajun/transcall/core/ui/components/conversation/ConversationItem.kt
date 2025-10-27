package com.youhajun.transcall.core.ui.components.conversation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youhajun.core.design.Colors
import com.youhajun.core.design.R
import com.youhajun.core.design.Typography
import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.calling.payload.TranslationMessage
import com.youhajun.core.model.conversation.ConversationState
import com.youhajun.core.model.room.Participant
import com.youhajun.hyanghae.graphics.modifier.conditional
import com.youhajun.transcall.core.ui.components.HorizontalSpacer
import com.youhajun.transcall.core.ui.components.VerticalSpacer
import com.youhajun.transcall.core.ui.util.DateFormatPatterns
import com.youhajun.transcall.core.ui.util.toUiDateString

@Composable
fun ConversationItem(
    isMine: Boolean,
    translationMessage: TranslationMessage,
    senderInfo: Participant
) {
    ConversationContainer(isMine = isMine, {
        ConversationContentsHeader(isMine, translationMessage, senderInfo)

        VerticalSpacer(4.dp)

        ConversationContentsBody(isMine, translationMessage)
    })
}

@Composable
private fun ConversationContentsHeader(
    isMine: Boolean,
    translationMessage: TranslationMessage,
    senderInfo: Participant
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = senderInfo.displayName,
            color = if(isMine) Colors.White else Colors.Black,
            style = Typography.bodySmall,
            fontSize = 13.sp,
            fontWeight = FontWeight.W500,
        )

        HorizontalSpacer(8.dp)

        Text(
            text = translationMessage.createdAtToEpochTime.toUiDateString(DateFormatPatterns.HOUR_MINUTE),
            color = Colors.Gray900,
            style = Typography.bodySmall,
            fontWeight = FontWeight.W400,
        )
    }
}

@Composable
private fun ConversationContentsBody(
    isMine: Boolean,
    translationMessage: TranslationMessage,
) {
    val transText = translationMessage.transText
    val transLang = translationMessage.transLanguage

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        ConversationMessage(
            isMine = isMine,
            messageText = translationMessage.originText,
            languageText = stringResource(R.string.conversation_language_origin, translationMessage.originLanguage.code.uppercase()),
        )

        if(transText != null && transLang != null) {
            VerticalSpacer(8.dp)

            ConversationMessage(
                isMine = isMine,
                messageText = transText,
                languageText = stringResource(R.string.conversation_language_translated, transLang.code.uppercase()),
            )
        }
    }
}

@Composable
private fun ConversationMessage(
    isMine: Boolean,
    messageText: String,
    languageText: String
) {
    Text(
        text = messageText,
        color = if (isMine) Colors.White else Colors.Black,
        style = Typography.bodyMedium,
        fontSize = 15.sp,
        fontWeight = FontWeight.W600,
    )

    VerticalSpacer(6.dp)

    Text(
        modifier = Modifier
            .background(Colors.FFEEF6FF, RoundedCornerShape(28.dp))
            .border(1.dp, Colors.FFE6E9EE, RoundedCornerShape(28.dp))
            .padding(horizontal = 7.dp, vertical = 2.dp),
        fontSize = 11.sp,
        text = languageText,
        color = Colors.FF6B7280,
        style = Typography.bodySmall,
        fontWeight = FontWeight.W500,
    )
}

@Composable
private fun ConversationContainer(
    isMine: Boolean,
    content: @Composable ColumnScope.() -> Unit
) {
    val backgroundColor = if(isMine) Brush.linearGradient(listOf(Colors.FF3B82F6, Colors.FF0EA5E9)) else SolidColor(Colors.White)
    val shape = RoundedCornerShape(
        topStart = if(isMine) 16.dp else 6.dp,
        topEnd = if(isMine) 6.dp else 16.dp,
        bottomStart = 16.dp,
        bottomEnd = 16.dp
    )

    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (isMine) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        val minW = maxWidth * 0.5f
        val maxW = maxWidth * 0.8f

        Column(
            modifier = Modifier
                .widthIn(min = minW, max = maxW)
                .width(IntrinsicSize.Max)
                .shadow(2.dp, shape)
                .background(backgroundColor, shape)
                .conditional(!isMine) {
                    border(1.dp, Colors.FFE6E9EE, shape)
                }
                .padding(horizontal = 13.dp, vertical = 11.dp),
            content = content
        )
    }
}

@Preview
@Composable
private fun ConversationItemMinePreview() {
    ConversationItem(
        isMine = true,
        translationMessage = TranslationMessage(
            conversationId = "1",
            roomId = "1",
            senderId = "1",
            state = ConversationState.PENDING,
            originText = "안녕하세요 반갑습니다",
            originLanguage = LanguageType.KOREAN,
            transText = "Hello, Nice meet you",
            transLanguage = LanguageType.ENGLISH,
            createdAtToEpochTime = 1760157304,
            updatedAtToEpochTime = 1760157304,
        ),
        senderInfo = Participant(
            participantId = "1",
            userId = "1",
            displayName = "홍길동",
            imageUrl = "",
            language = LanguageType.KOREAN
        )
    )
}

@Preview
@Composable
private fun ConversationItemOtherPreview() {
    ConversationItem(
        isMine = false,
        translationMessage = TranslationMessage(
            conversationId = "1",
            roomId = "1",
            state = ConversationState.PENDING,
            senderId = "1",
            originText = "안녕하세요",
            originLanguage = LanguageType.KOREAN,
            transText = "Hello",
            transLanguage = LanguageType.ENGLISH,
            createdAtToEpochTime = 1760157304,
            updatedAtToEpochTime = 1760157304,
        ),
        senderInfo = Participant(
            participantId = "1",
            userId = "1",
            displayName = "홍길동",
            imageUrl = "",
            language = LanguageType.KOREAN
        )
    )
}