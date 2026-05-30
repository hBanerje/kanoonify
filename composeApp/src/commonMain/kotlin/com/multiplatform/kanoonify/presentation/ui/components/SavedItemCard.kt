package com.multiplatform.kanoonify.presentation.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
 * Reusable card for any saved/bookmarked item (Law, Article, AI chat, News, Note).
 * Stateless. Caller supplies copy + remove handler.
 */
@Composable
fun SavedItemCard(
    title: String,
    subtitle: String,
    glyph: String,
    accent: Color,
    tagLabel: String,
    onClick: () -> Unit,
    onRemove: () -> Unit,
    removeLabel: String,
    modifier: Modifier = Modifier
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessMediumLow),
        label = "savedCardScale"
    )
    val shape = RoundedCornerShape(Dimens.RadiusXL)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .neonGlow(accent, shape, radius = 12.dp, alpha = 0.35f)
            .glassSurface(
                shape = shape,
                fill = Color.White.copy(alpha = 0.05f),
                stroke = accent.copy(alpha = 0.28f)
            )
            .clickable(
                interactionSource = interaction,
                indication = null,
                onClick = onClick
            )
            .padding(Dimens.SpaceM)
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(Dimens.RadiusM))
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
            Text(glyph, color = Color.White, fontSize = 18.sp)
        }
        Spacer(Modifier.width(Dimens.SpaceM))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = tagLabel,
                    color = accent,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.6.sp,
                    modifier = Modifier
                        .clip(RoundedCornerShape(Dimens.RadiusPill))
                        .background(accent.copy(alpha = 0.16f))
                        .padding(horizontal = Dimens.SpaceS, vertical = 2.dp)
                )
            }
            Spacer(Modifier.size(6.dp))
            Text(
                text = title,
                color = KanoonifyPremiumColors.TextHi,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.size(2.dp))
            Text(
                text = subtitle,
                color = KanoonifyPremiumColors.TextLow,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }
        Spacer(Modifier.width(Dimens.SpaceS))
        val removeInteraction = remember { MutableInteractionSource() }
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(Dimens.RadiusS))
                .background(Color.White.copy(alpha = 0.06f))
                .clickable(
                    interactionSource = removeInteraction,
                    indication = null,
                    onClick = onRemove
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("\uD83D\uDDD1", fontSize = 13.sp)
        }
        // a11y label only
        Text(
            text = removeLabel,
            color = Color.Transparent,
            fontSize = 1.sp,
            modifier = Modifier.size(1.dp)
        )
    }
}

