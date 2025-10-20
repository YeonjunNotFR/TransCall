package com.youhajun.transcall.core.ui.components.participant

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.youhajun.core.design.Colors
import com.youhajun.core.design.R
import com.youhajun.core.design.Typography
import com.youhajun.core.design.toCountryIcon
import com.youhajun.core.model.CountryType
import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.room.Participant
import com.youhajun.transcall.core.ui.components.VerticalSpacer
import com.youhajun.transcall.core.ui.components.modifier.noRippleClickable

@Composable
fun ParticipantItem(
    participant: Participant,
    onClick: (Participant) -> Unit
) {
    Column(
        modifier = Modifier
            .width(92.dp)
            .aspectRatio(92/126f)
            .shadow(2.dp, RoundedCornerShape(20.dp))
            .background(Colors.White, RoundedCornerShape(20.dp))
            .border(1.dp, Colors.FFE6E9EE, RoundedCornerShape(20.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        VerticalSpacer(11.dp)
        
        AsyncImage(
            model = participant.imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.ic_person),
            placeholder = painterResource(id = R.drawable.ic_person),
            modifier = Modifier
                .noRippleClickable { onClick(participant) }
                .clip(CircleShape)
                .size(56.dp)
        )

        VerticalSpacer(4.dp)

        Text(
            text = participant.displayName,
            color = Colors.Black,
            maxLines = 1,
            fontSize = 11.sp,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W600,
            style = Typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        VerticalSpacer(4.dp)

        Row(
            modifier = Modifier
                .background(Colors.FFEEF6FF, RoundedCornerShape(28.dp))
                .border(1.dp, Colors.FFE6E9EE, RoundedCornerShape(28.dp))
                .padding(horizontal = 7.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = participant.language.code.uppercase() + " • ",
                color = Colors.FF6B7280,
                fontWeight = FontWeight.W600,
                style = Typography.bodySmall,
            )

            Image(
                painter = painterResource(participant.country.toCountryIcon()),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Preview
@Composable
private fun ParticipantItemPreview() {
    ParticipantItem(
        participant = Participant(
            participantId = "1",
            userId = "1",
            displayName = "치킨무빼주세요",
            imageUrl = "",
            language = LanguageType.KOREAN,
            country = CountryType.SOUTH_KOREA
        ),
        onClick = {}
    )
}