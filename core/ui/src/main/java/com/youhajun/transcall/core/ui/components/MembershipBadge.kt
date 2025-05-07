package com.youhajun.transcall.core.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youhajun.core.model.MembershipPlan
import com.youhajun.transcall.core.ui.theme.Colors
import com.youhajun.transcall.core.ui.theme.Typography

@Composable
fun MembershipBadge(
    modifier: Modifier = Modifier,
    plan: MembershipPlan,
) {

    val (textColor, backgroundColor) = when (plan) {
        MembershipPlan.Free -> Pair(Colors.FF374151, Colors.FFE5E7EB)
        MembershipPlan.Pro -> Pair(Color.White, Colors.FF6366F1)
        MembershipPlan.Premium -> Pair(Color.White, Colors.FF2F80ED)
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor,
        contentColor = textColor,
    ) {
        Text(
            text = plan.name,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = Typography.labelSmall,
        )
    }
}

@Composable
@Preview
private fun MembershipBadgeFreePreview() {
    MembershipBadge(
        plan = MembershipPlan.Free,
    )
}

@Composable
@Preview
private fun MembershipBadgeProPreview() {
    MembershipBadge(
        plan = MembershipPlan.Pro,
    )
}

@Composable
@Preview
private fun MembershipBadgePremiumPreview() {
    MembershipBadge(
        plan = MembershipPlan.Premium,
    )
}
