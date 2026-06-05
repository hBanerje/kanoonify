package com.multiplatform.kanoonify.presentation.ui.modifiers

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.multiplatform.kanoonify.presentation.theme.KanoonifyPremiumColors

fun Modifier.neonGlow(
    color: Color,
    shape: Shape,
    radius: Dp = 18.dp,
    alpha: Float = 0.55f
): Modifier = this.shadow(
    elevation = radius,
    shape = shape,
    ambientColor = color.copy(alpha = alpha),
    spotColor = color.copy(alpha = alpha)
)

fun Modifier.glassSurface(
    shape: Shape,
    fill: Color = KanoonifyPremiumColors.GlassFill,
    stroke: Color = KanoonifyPremiumColors.GlassStroke,
    strokeWidth: Dp = 1.dp
): Modifier = this
    .clip(shape)
    .background(
        Brush.verticalGradient(
            colors = listOf(
                fill.copy(alpha = (fill.alpha + 0.04f).coerceAtMost(1f)),
                fill
            )
        )
    )
    .border(width = strokeWidth, color = stroke, shape = shape)
