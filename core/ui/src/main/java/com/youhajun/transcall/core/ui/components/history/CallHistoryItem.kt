package com.youhajun.transcall.core.ui.components.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youhajun.core.design.Colors
import com.youhajun.core.design.R
import com.youhajun.core.design.Typography
import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.calling.CallHistory
import com.youhajun.core.model.room.Participant
import com.youhajun.transcall.core.ui.components.HorizontalSpacer
import com.youhajun.transcall.core.ui.components.VerticalSpacer
import com.youhajun.transcall.core.ui.components.image.MultipleCircleProfileImage
import com.youhajun.transcall.core.ui.components.modifier.noRippleClickable
import com.youhajun.transcall.core.ui.util.DateFormatPatterns
import com.youhajun.transcall.core.ui.util.TimeFormatPatterns
import com.youhajun.transcall.core.ui.util.toUiDateString
import com.youhajun.transcall.core.ui.util.toUiDurationString
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
fun CallHistoryItem(
    callHistory: CallHistory,
    createdAtDateFormat: DateFormatPatterns = DateFormatPatterns.DATE_TIME_WITHOUT_SECONDS,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Colors.FFB2F2FF,
                shape = RoundedCornerShape(8.dp)
            )
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp, top = 20.dp),
    ) {
        Text(
            text = callHistory.title,
            color = Colors.Black,
            style = Typography.titleSmall.copy(
                fontWeight = FontWeight.W600,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        VerticalSpacer(2.dp)

        Text(
            text = callHistory.summary,
            color = Colors.Gray700,
            style = Typography.bodySmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        VerticalSpacer(8.dp)

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_clock),
                contentDescription = "App Logo",
                tint = Colors.Gray700,
                modifier = Modifier.size(12.dp)
            )

            HorizontalSpacer(2.dp)

            Text(
                text = callHistory.durationSeconds.toUiDurationString(TimeFormatPatterns.LABEL_HOUR_MIN),
                color = Colors.Gray700,
                style = Typography.bodySmall,
                modifier = Modifier.alignByBaseline()
            )

            HorizontalSpacer(10.dp)

            Text(
                text = callHistory.startedAt.toUiDateString(createdAtDateFormat),
                color = Colors.Gray700,
                style = Typography.bodySmall,
                modifier = Modifier.alignByBaseline()
            )

            HorizontalSpacer(8.dp)
        }

        VerticalSpacer(6.dp)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MultipleCircleProfileImage(
                modifier = Modifier.weight(1f),
                imageUrls = callHistory.participants.map { it.imageUrl }.toImmutableList(),
                circleSize = 24.dp,
                circleBorderColor = Colors.PrimaryLight,
                maxVisibleCount = 6
            )

            Row {
                val likeIcon = if(callHistory.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder
                val likeTint = if(callHistory.isLiked) Colors.FFFFAD42 else Colors.Gray700
                val memoTint = if(callHistory.memo.isNotEmpty()) Colors.PrimaryLight else Colors.Gray700

                Icon(
                    imageVector = likeIcon,
                    contentDescription = "App Logo",
                    tint = likeTint,
                    modifier = Modifier.size(16.dp)
                )

                HorizontalSpacer(8.dp)

                Icon(
                    painter = painterResource(R.drawable.ic_memo),
                    contentDescription = "App Logo",
                    tint = memoTint,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CallHistoryFullDateItemPreview() {
    CallHistoryItem(
        callHistory = CallHistory(
            historyId = "12345",
            title = "Sample Call History Title",
            summary = "This is a sample call history summary that is quite long and should be truncated if it exceeds the maximum line limit.",
            participants = persistentListOf(
                Participant(
                    userId = "67890",
                    displayName = "John Doe dwaafwojfdkawdkadwkd dawkawd akwdkaw dawkd wa dwakd",
                    imageUrl = "https://example.com/image.jpg",
                    language = LanguageType.ENGLISH
                )
            ),
            startedAt = 1633072800L,
            endedAt = 1633076400L,
            durationSeconds = 12001,
            memo = "coapepock",
            isLiked = true
        ),
        createdAtDateFormat = DateFormatPatterns.DATE_TIME_WITHOUT_SECONDS,
    )
}