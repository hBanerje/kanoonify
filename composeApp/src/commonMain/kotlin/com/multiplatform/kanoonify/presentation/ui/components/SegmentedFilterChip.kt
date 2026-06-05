package com.multiplatform.kanoonify.presentation.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.multiplatform.kanoonify.presentation.theme.Dimens
import com.multiplatform.kanoonify.presentation.theme.KanoonifyPremiumColors

@Composable
fun SegmentedFilterChip(
    label: String,
    selected: Boolean,
    accent: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg by animateColorAsState(
        targetValue = if (selected) accent.copy(alpha = 0.20f) else Color.White.copy(alpha = 0.04f),
        label = "chipBg"
    )
    val border by animateColorAsState(
        targetValue = if (selected) accent.copy(alpha = 0.55f) else KanoonifyPremiumColors.GlassStroke,
        label = "chipBorder"
    )
    val fg by animateColorAsState(
        targetValue = if (selected) KanoonifyPremiumColors.TextHi else KanoonifyPremiumColors.TextMid,
        label = "chipFg"
    )
    val shape = RoundedCornerShape(Dimens.RadiusPill)
    val interaction = remember { MutableInteractionSource() }
    Box(
        modifier = modifier
            .clip(shape)
            .background(bg)
            .border(width = 1.dp, color = border, shape = shape)
            .clickable(
                interactionSource = interaction,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = Dimens.SpaceL, vertical = 8.dp)
    ) {
        Text(
            text = label,
            color = fg,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
            letterSpacing = 0.3.sp
        )
    }
}
