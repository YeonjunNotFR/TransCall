package com.youhajun.transcall.core.ui.components.room

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youhajun.core.design.Colors
import com.youhajun.core.design.Typography
import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.room.CurrentRoomInfo
import com.youhajun.core.model.room.Participant
import com.youhajun.core.model.room.RoomInfo
import com.youhajun.core.model.room.RoomVisibility
import com.youhajun.transcall.core.ui.components.HorizontalSpacer
import com.youhajun.transcall.core.ui.components.VerticalSpacer
import com.youhajun.transcall.core.ui.components.image.MultipleCircleProfileImage
import com.youhajun.transcall.core.ui.components.modifier.noRippleClickable
import com.youhajun.transcall.core.ui.util.RelativeTimePatterns
import com.youhajun.transcall.core.ui.util.toUiRelativeString
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList

@Composable
fun CurrentRoomItem(
    currentRoomInfo: CurrentRoomInfo,
    circleProfileMaxCount: Int = 4,
    tagMaxVisibleCount: Int = 4,
    tagMaxLength: Int = 10,
    onClickRoom: (RoomInfo) -> Unit
) {

    val roomInfo = currentRoomInfo.roomInfo
    val imageUrls = currentRoomInfo.currentParticipants.map { it.imageUrl }.toImmutableList()
    val overflowCount = roomInfo.tags.size - tagMaxVisibleCount

    Column(
        modifier = Modifier
            .noRippleClickable(onClick = { onClickRoom(roomInfo) })
            .fillMaxWidth()
            .background(Colors.White, shape = RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = Colors.FFB2F2FF,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp, top = 16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = rememberVectorPainter(Icons.Default.Person),
                    contentDescription = null,
                    tint = Colors.FF6E6E73,
                    modifier = Modifier.size(16.dp)
                )
                HorizontalSpacer(4.dp)
                Text(
                    text = "${roomInfo.currentParticipantCount}/${roomInfo.maxParticipantCount} 명",
                    style = Typography.bodyMedium,
                    color = Colors.FF6E6E73
                )
            }

            MultipleCircleProfileImage(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .weight(1f),
                imageUrls = imageUrls,
                circleSize = 24.dp,
                circleBorderWidth = 2.dp,
                circleBorderColor = Colors.PrimaryLight,
                overlap = 8.dp,
                maxVisibleCount = circleProfileMaxCount
            )

            Text(
                text = roomInfo.createdAtToEpochTime.toUiRelativeString(RelativeTimePatterns.AUTO_HOUR_MIN),
                fontWeight = FontWeight.W600,
                style = Typography.bodySmall,
                color = Colors.FF6B7280,
            )
        }

        VerticalSpacer(6.dp)

        Text(
            text = roomInfo.title,
            style = Typography.titleLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = Colors.Black
        )

        if (roomInfo.tags.isNotEmpty()) {
            VerticalSpacer(12.dp)

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                itemVerticalAlignment = Alignment.CenterVertically,
            ) {
                roomInfo.tags.take(tagMaxVisibleCount).forEach {
                    Text(
                        modifier = Modifier
                            .background(Colors.FFEEF6FF, RoundedCornerShape(28.dp))
                            .border(1.dp, Colors.FFE6E9EE, RoundedCornerShape(28.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        text = if (it.length > tagMaxLength) "# ${it.take(tagMaxLength)}…" else "# $it",
                        color = Colors.Black,
                        fontWeight = FontWeight.W600,
                        overflow = TextOverflow.Ellipsis,
                        style = Typography.bodySmall
                    )
                }

                if (overflowCount > 0) {
                    Text(
                        modifier = Modifier
                            .background(Colors.FF6E6E73, RoundedCornerShape(48.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        text = "+$overflowCount",
                        color = Colors.FFF6F8FA,
                        fontWeight = FontWeight.W600,
                        style = Typography.bodySmall
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun CurrentRoomPreview() {
    CurrentRoomItem(
        currentRoomInfo = CurrentRoomInfo(
            roomInfo = RoomInfo(
                roomId = "",
                title = "Let's talk about anything!",
                currentParticipantCount = 5,
                maxParticipantCount = 10,
                visibility = RoomVisibility.PUBLIC,
                tags = persistentSetOf(
                    "fun",
                    "talk",
                    "klamccmacmcacalcelcealclacelka",
                    "english",
                    "random",
                    "what",
                    "the",
                )
            ),
            currentParticipants = persistentListOf(
                Participant(
                    participantId = "67890",
                    userId = "3",
                    displayName = "John Doe",
                    imageUrl = "https://example.com/image.jpg",
                    language = LanguageType.ENGLISH
                ),
                Participant(
                    participantId = "12345",
                    userId = "2",
                    displayName = "Jane Smith",
                    imageUrl = "https://example.com/image2.jpg",
                    language = LanguageType.SPANISH
                ),
                Participant(
                    participantId = "33445",
                    userId = "1",
                    displayName = "Alice Johnson",
                    imageUrl = "https://example.com/image3.jpg",
                    language = LanguageType.SPANISH
                )
            )
        ),
        onClickRoom = {}
    )
}