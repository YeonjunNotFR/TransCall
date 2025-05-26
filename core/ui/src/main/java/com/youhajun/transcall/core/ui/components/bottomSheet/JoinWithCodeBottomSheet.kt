package com.youhajun.transcall.core.ui.components.bottomSheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youhajun.transcall.core.ui.components.HorizontalSpacer
import com.youhajun.transcall.core.ui.components.VerticalSpacer
import com.youhajun.core.design.Colors
import com.youhajun.core.design.Typography
import com.youhajun.core.design.R
import com.youhajun.transcall.core.ui.util.noRippleClickable

@Composable
fun JoinWithCodeBottomSheet(
    isVisible: Boolean,
    onVisibleChanged: (Boolean) -> Unit,
    onClickCancel: () -> Unit,
    onClickConfirm: (String) -> Unit
) {

    var code by remember { mutableStateOf("") }

    BottomSheetComp(
        isVisible = isVisible,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        containerColor = Color.White,
        isSkipPartiallyExpanded = true,
        dragHandler = null,
        onVisibilityChange = onVisibleChanged
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Text(
                text = stringResource(R.string.dialog_join_code_title),
                color = Color.Black,
                style = Typography.headlineMedium.copy(
                    fontWeight = FontWeight.W800
                )
            )

            VerticalSpacer(16.dp)

            OutlinedTextField(
                value = code,
                onValueChange = { code = it },
                placeholder = {
                    Text(
                        text = stringResource(R.string.dialog_join_code_title),
                        color = Colors.Gray500,
                        style = Typography.bodyLarge
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    errorBorderColor = Colors.Gray300,
                    disabledBorderColor = Colors.Gray300,
                    focusedBorderColor = Colors.Gray300,
                    unfocusedBorderColor = Colors.Gray300,
                ),
                textStyle = Typography.bodyLarge.copy(
                    color = Color.Black
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
            )

            VerticalSpacer(20.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(R.string.common_cancel),
                    color = Colors.PrimaryLight,
                    style = Typography.titleLarge.copy(
                        fontWeight = FontWeight.W600
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .noRippleClickable(onClick = onClickCancel)
                        .background(Colors.White, shape = RoundedCornerShape(16.dp))
                        .padding(vertical = 16.dp)
                        .weight(1f)
                )

                HorizontalSpacer(4.dp)

                Text(
                    text = stringResource(R.string.common_join),
                    color = Colors.White,
                    style = Typography.titleLarge.copy(
                        fontWeight = FontWeight.W600
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .noRippleClickable(onClick = { onClickConfirm(code) })
                        .background(Colors.PrimaryLight, shape = RoundedCornerShape(16.dp))
                        .padding(vertical = 16.dp)
                        .weight(1f)
                )

            }
        }
    }
}

@Composable
@Preview
private fun JoinWithCodeBottomSheetPreview() {
    JoinWithCodeBottomSheet(
        isVisible = true,
        onVisibleChanged = {},
        onClickConfirm = {},
        onClickCancel = {}
    )
}