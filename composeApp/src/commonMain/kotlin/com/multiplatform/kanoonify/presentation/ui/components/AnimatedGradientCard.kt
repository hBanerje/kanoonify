package com.multiplatform.kanoonify.presentation.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.multiplatform.kanoonify.presentation.theme.Dimens
import kotlin.math.cos
import kotlin.math.sin

/**
 * A rounded surface filled with a slowly-shifting linear gradient.
 *
 * Optional features:
 * - shimmer light sweep over the gradient
 * - press-scale interaction
 * - tinted outer glow shadow
 *
 * Stateless: the caller owns content placement.
 */
@Composable
fun AnimatedGradientCard(
    colors: List<Color>,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: RoundedCornerShape = RoundedCornerShape(Dimens.RadiusXL),
    elevation: Dp = 16.dp,
    glowColor: Color = colors.firstOrNull() ?: Color.Black,
    enableShimmer: Boolean = true,
    contentPadding: Dp = Dimens.SpaceL,
    content: @Composable () -> Unit
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = 0.55f,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "gradientCardScale"
    )

    val transition = rememberInfiniteTransition(label = "gradientCard")
    val angle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 7000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "gradientAngle"
    )

    val rad = angle * (kotlin.math.PI / 180.0).toFloat()
    val start = Offset(
        x = (0.5f + 0.5f * cos(rad)) * 1000f,
        y = (0.5f + 0.5f * sin(rad)) * 1000f
    )
    val end = Offset(
        x = (0.5f - 0.5f * cos(rad)) * 1000f,
        y = (0.5f - 0.5f * sin(rad)) * 1000f
    )

    val gradient = Brush.linearGradient(
        colors = colors,
        start = start,
        end = end
    )

    var base = modifier
        .graphicsLayer { scaleX = scale; scaleY = scale }
        .shadow(
            elevation = elevation,
            shape = shape,
            ambientColor = glowColor,
            spotColor = glowColor
        )
        .clip(shape)
        .background(gradient)

    if (enableShimmer) {
        base = base.shimmerEffect()
    }
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

