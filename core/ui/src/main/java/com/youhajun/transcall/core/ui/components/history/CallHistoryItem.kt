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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youhajun.core.design.Colors
import com.youhajun.core.design.R
import com.youhajun.core.design.Typography
import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.history.CallHistory
import com.youhajun.core.model.room.Participant
import com.youhajun.transcall.core.ui.components.HorizontalSpacer
import com.youhajun.transcall.core.ui.components.VerticalSpacer
import com.youhajun.transcall.core.ui.components.image.MultipleCircleProfileImage
import com.youhajun.transcall.core.ui.components.modifier.noRippleClickable
import com.youhajun.transcall.core.ui.util.DateFormatPatterns
import com.youhajun.transcall.core.ui.util.TimePatterns
import com.youhajun.transcall.core.ui.util.toUiDateString
import com.youhajun.transcall.core.ui.util.toUiDurationString
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
fun CallHistoryItem(
    callHistory: CallHistory,
    dateFormat: DateFormatPatterns = DateFormatPatterns.MONTH_DAY_HOUR_MINUTE,
    onClickHistory: (CallHistory) -> Unit,
) {

    val imageUrls = remember(callHistory.participants) {
        callHistory.participants.map { it.imageUrl }.toImmutableList()
    }

    Column(
        modifier = Modifier
            .noRippleClickable { onClickHistory(callHistory) }
            .fillMaxWidth()
            .background(Colors.White, shape = RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = Colors.FFB2F2FF,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp, top = 20.dp),
    ) {
        Text(
            text = callHistory.title,
            color = Colors.Black,
            style = Typography.titleSmall,
            fontWeight = FontWeight.W600,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        if(callHistory.summary.isNotBlank()) {
            VerticalSpacer(4.dp)

            Text(
                text = callHistory.summary,
                color = Colors.Black,
                style = Typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }

        VerticalSpacer(8.dp)

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = Colors.Gray700,
                    modifier = Modifier.size(12.dp)
                )

                HorizontalSpacer(2.dp)

                Text(
                    text = buildString {
                        append(callHistory.joinedAtToEpochTime.toUiDateString(dateFormat))
                        val leftAt = callHistory.leftAtToEpochTime
                        if(leftAt != null) {
                            append(" - ")
                            append(leftAt.toUiDateString(dateFormat))
                        }
                    },
                    color = Colors.Gray700,
                    style = Typography.bodySmall,
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.ic_clock),
                    contentDescription = null,
                    tint = Colors.Gray700,
                    modifier = Modifier.size(12.dp)
                )

                HorizontalSpacer(2.dp)

                Text(
                    text = callHistory.durationSeconds?.toUiDurationString(TimePatterns.LABEL_HOUR_MIN_SEC) ?: stringResource(R.string.history_duration_calling),
                    color = Colors.Gray700,
                    style = Typography.bodySmall
                )
            }
        }

        VerticalSpacer(6.dp)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MultipleCircleProfileImage(
                modifier = Modifier.weight(1f),
                imageUrls = imageUrls,
                circleSize = 24.dp,
                overlap = 8.dp,
                circleBorderColor = Colors.PrimaryLight,
                maxVisibleCount = 5
            )

            Row {
                val likeIcon = if(callHistory.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder
                val likeTint = if(callHistory.isLiked) Colors.FFFFAD42 else Colors.Gray700
                val memoTint = if(callHistory.memo.isNotEmpty()) Colors.PrimaryLight else Colors.Gray700

                Icon(
                    imageVector = likeIcon,
                    contentDescription = null,
                    tint = likeTint,
                    modifier = Modifier.size(16.dp)
                )

                HorizontalSpacer(8.dp)

                Icon(
                    painter = painterResource(R.drawable.ic_memo),
                    contentDescription = null,
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
                    userId = "1",
                    participantId = "67890",
                    displayName = "John Doe dwaafwojfdkawdkadwkd dawkawd akwdkaw dawkd wa dwakd",
                    imageUrl = "https://example.com/image.jpg",
                    language = LanguageType.ENGLISH
                )
            ),
            joinedAtToEpochTime = 1760157304,
            leftAtToEpochTime = 1760157319,
            durationSeconds = 10,
            memo = "coapepock",
            isLiked = true
        ),
        dateFormat = DateFormatPatterns.MONTH_DAY_HOUR_MINUTE,
        onClickHistory = {}
    )
}