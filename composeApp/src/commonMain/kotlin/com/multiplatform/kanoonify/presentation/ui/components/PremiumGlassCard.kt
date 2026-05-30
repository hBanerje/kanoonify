package com.multiplatform.kanoonify.presentation.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.multiplatform.kanoonify.presentation.theme.Dimens
import com.multiplatform.kanoonify.presentation.theme.KanoonifyPremiumColors
import com.multiplatform.kanoonify.presentation.ui.modifiers.glassSurface
import com.multiplatform.kanoonify.presentation.ui.modifiers.neonGlow

/**
 * Reusable glassmorphism container with optional accent glow + press-scale.
 * Stateless — the caller owns layout of [content].
 *
 *  - `accentGlow` adds a tinted shadow halo (set to [Color.Transparent] to disable)
 *  - `onClick` is optional; when null the card is non-interactive
 *  - Animations are press-only (no infinite transitions) → zero idle cost.
 */
@Composable
fun PremiumGlassCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: RoundedCornerShape = RoundedCornerShape(Dimens.RadiusXXL),
    accentGlow: Color = Color.Transparent,
    glowRadius: Dp = 20.dp,
    contentPadding: Dp = Dimens.SpaceL,
    fill: Color = KanoonifyPremiumColors.GlassFill,
    stroke: Color = KanoonifyPremiumColors.GlassStroke,
    content: @Composable () -> Unit
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessMediumLow),
        label = "glassCardScale"
    )

    var base: Modifier = modifier.graphicsLayer { scaleX = scale; scaleY = scale }
    if (accentGlow.alpha > 0f) {
        base = base.neonGlow(color = accentGlow, shape = shape, radius = glowRadius)
    }
    base = base.glassSurface(shape = shape, fill = fill, stroke = stroke)
    if (onClick != null) {
        base = base.clickable(
            interactionSource = interaction,
            indication = null,
            onClick = onClick
        )
    }

    Box(modifier = base.padding(contentPadding)) {
        content()
    }
}

