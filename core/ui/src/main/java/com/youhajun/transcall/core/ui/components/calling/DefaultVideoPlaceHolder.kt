package com.youhajun.transcall.core.ui.components.calling

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.youhajun.core.design.Colors
import com.youhajun.core.design.R
import com.youhajun.core.design.Typography
import com.youhajun.core.design.toCountryIcon
import com.youhajun.core.model.CountryType
import com.youhajun.core.model.LanguageType
import com.youhajun.core.model.room.Participant
import com.youhajun.transcall.core.ui.components.HorizontalSpacer
import com.youhajun.transcall.core.ui.components.VerticalSpacer
import com.youhajun.transcall.core.ui.components.modifier.speakingGlow

@Composable
fun DefaultVideoPlaceHolder(
    modifier: Modifier = Modifier,
    audioLevel: Float,
    participant: Participant?,
    displayNameTextStyle: TextStyle = Typography.displayLarge.copy(fontWeight = FontWeight.W800),
    languageTextStyle: TextStyle = Typography.bodyLarge.copy(fontWeight = FontWeight.W500),
    languageIconSize: Dp = 24.dp,
    maxBlurValue: Dp = 50.dp,
) {
    BoxWithConstraints(modifier = modifier) {
        val totalHeight = maxHeight
        val spacingImageName = totalHeight * 0.05f
        val spacingNameLang = totalHeight * 0.01f

        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = participant?.imageUrl,
                contentDescription = null,
                error = painterResource(id = R.drawable.ic_person),
                placeholder = painterResource(id = R.drawable.ic_person),
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .aspectRatio(1f)
                    .speakingGlow(audioLevel, CircleShape, maxBlurDp = maxBlurValue, outlineWidth = 2.dp)
                    .padding(1.dp)
                    .clip(CircleShape)
            )

            VerticalSpacer(spacingImageName)

            Text(
                text = participant?.displayName ?: "",
                color = Colors.White,
                style = displayNameTextStyle
            )

            if (participant != null) {
                VerticalSpacer(spacingNameLang)

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        painter = painterResource(participant.country.toCountryIcon()),
                        contentDescription = null,
                        modifier = Modifier.size(languageIconSize)
                    )

                    HorizontalSpacer(4.dp)

                    Text(
                        text = participant.language.code.uppercase(),
                        color = Colors.Gray500,
                        style = languageTextStyle,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun VideoPlaceHolderPreview() {
    DefaultVideoPlaceHolder(
        modifier = Modifier.fillMaxSize().background(Colors.SurfaceDark),
        audioLevel = 1f,
        participant = Participant(
            participantId = "1",
            userId = "1",
            displayName = "John Doe",
            imageUrl = "https://example.com/image.jpg",
            language = LanguageType.ENGLISH,
            country = CountryType.UNITED_STATES
        ),
    )
}