package com.youhajun.transcall.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.youhajun.core.design.Colors
import com.youhajun.core.design.Typography

@Composable
fun FilledActionButton(
    text: String,
    icon: Painter? = null,
    textStyle: TextStyle = Typography.titleLarge,
    iconSize: Dp = 24.dp,
    containerColor: Color = Colors.White,
    contentColor: Color = Colors.PrimaryLight,
    enabled: Boolean = true,
    paddingValues: PaddingValues = PaddingValues(horizontal = 8.dp),
    onClick: () -> Unit,
) {

    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(12.dp),
        contentPadding = paddingValues,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.padding(4.dp).fillMaxWidth()
        ) {
            if (icon != null) {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    modifier = Modifier.size(iconSize),
                )
                HorizontalSpacer(8.dp)
            }

            Text(
                text = text,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = textStyle
            )
        }
    }
}

@Preview(name = "Short Text")
@Composable
private fun FilledActionButtonShortTextPreview() {
    FilledActionButton(
        text = "Start",
        icon = rememberVectorPainter(Icons.Default.Call),
        onClick = {}
    )
}

@Preview(name = "Long English")
@Composable
fun ActionButtonLongEnglishPreview() {
    FilledActionButton(
        text = "Very Long Long Long Long Loooooooooooong Text",
        icon = rememberVectorPainter(Icons.Default.Call),
        onClick = {}
    )
}