package com.multiplatform.kanoonify.presentation.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.multiplatform.kanoonify.presentation.theme.Dimens
import com.multiplatform.kanoonify.presentation.theme.KanoonifyPremiumColors
import com.multiplatform.kanoonify.presentation.ui.modifiers.glassSurface
import com.multiplatform.kanoonify.presentation.ui.modifiers.neonGlow

/**
 * Dark-glass feature tile for the 2×2 Quick-Access grid.
 *
 *  - Accent-tinted glow shadow (different per tile)
 *  - Glass surface + 1dp bright hairline
 *  - Gradient icon container with glow
 *  - Subtle arrow indicator (top-right)
 *  - Press-scale only (no idle animations → list-safe)
 */
@Composable
fun PremiumQuickAccessCard(
    title: String,
    subtitle: String,
    glyph: String,
    accent: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessMediumLow),
        label = "qaScale"
    )
    val shape = RoundedCornerShape(Dimens.RadiusXXL)

    Box(
        modifier = modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .neonGlow(color = accent, shape = shape, radius = 16.dp, alpha = 0.45f)
            .glassSurface(
                shape = shape,
                fill = Color.White.copy(alpha = 0.05f),
                stroke = accent.copy(alpha = 0.30f)
            )
            .clickable(
                interactionSource = interaction,
                indication = null,
                onClick = onClick
            )
            .padding(Dimens.SpaceL)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconBubble(glyph = glyph, accent = accent)
                Text(
                    text = "\u2197",
                    color = accent,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(Modifier.height(Dimens.SpaceL))
            Text(
                text = title,
                color = KanoonifyPremiumColors.TextHi,
                fontSize = 15.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(Dimens.SpaceXS))
            Text(
                text = subtitle,
                color = KanoonifyPremiumColors.TextLow,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun IconBubble(glyph: String, accent: Color) {
    val shape = RoundedCornerShape(Dimens.RadiusL)
    Box(
        modifier = Modifier
            .size(46.dp)
            .neonGlow(color = accent, shape = shape, radius = 10.dp, alpha = 0.6f)
            .clip(shape)
            .background(
                Brush.linearGradient(
                    listOf(
                        accent.copy(alpha = 0.95f),
                        accent.copy(alpha = 0.55f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(text = glyph, color = Color.White, fontSize = 18.sp)
    }
}

