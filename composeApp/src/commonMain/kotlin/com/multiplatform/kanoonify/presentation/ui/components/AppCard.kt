package com.multiplatform.kanoonify.presentation.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import com.multiplatform.kanoonify.presentation.theme.Dimens
import kotlinx.coroutines.delay

/**
 * Standard rounded surface used for content cards.
 * Subtle elevation, consistent radius, optional press-scale interaction.
 */
@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    cornerRadius: Dp = Dimens.RadiusL,
    elevation: Dp = Dimens.ElevationCard,
    contentPadding: Dp = Dimens.SpaceL,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMediumLow)
    )

    val shape = RoundedCornerShape(cornerRadius)
    val base = modifier
        .graphicsLayer { scaleX = scale; scaleY = scale }
        .shadow(elevation = elevation, shape = shape, clip = false)
        .clip(shape)
        .background(MaterialTheme.colorScheme.surface)

    val withClick = if (onClick != null) {
        base.clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick
        )
    } else base

    androidx.compose.foundation.layout.Box(
        modifier = withClick.padding(contentPadding)
    ) { content() }
}

/**
 * Fade-in + slide-up entrance wrapper for list items.
 * Triggers once on first composition.
 */
@Composable
fun AnimatedEntrance(
    delayMillis: Long = 0L,
    durationMillis: Int = 380,
    slidePx: Float = 24f,
    content: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (delayMillis > 0) delay(delayMillis)
        visible = true
    }
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis, easing = FastOutSlowInEasing)
    )
    val translation by animateFloatAsState(
        targetValue = if (visible) 0f else slidePx,
        animationSpec = tween(durationMillis, easing = FastOutSlowInEasing)
    )
    androidx.compose.foundation.layout.Box(
        modifier = Modifier.graphicsLayer {
            this.alpha = alpha
            translationY = translation
        }
    ) { content() }
}

