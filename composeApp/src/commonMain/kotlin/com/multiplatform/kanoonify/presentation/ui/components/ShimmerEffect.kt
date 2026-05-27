package com.multiplatform.kanoonify.presentation.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color

/**
 * Adds an animated diagonal "light sweep" highlight on top of any composable.
 * Render after the background but before content so it sits over the surface but
 * under text/icons. Pair with .clip(...) on the parent to constrain the sweep.
 */
fun Modifier.shimmerEffect(
    highlightColor: Color = Color.White.copy(alpha = 0.22f),
    durationMillis: Int = 2200
): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerProgress"
    )

    drawWithCache {
        onDrawWithContent {
            drawContent()
            val width = size.width
            val sweepWidth = width * 0.55f
            // Move the sweep from far-left (-sweepWidth) to far-right (width + sweepWidth)
            val travel = (width + sweepWidth * 2)
            val startX = -sweepWidth + progress * travel
            val brush = Brush.linearGradient(
                colors = listOf(
                    Color.Transparent,
                    highlightColor,
                    Color.Transparent
                ),
                start = Offset(startX, 0f),
                end = Offset(startX + sweepWidth, size.height)
            )
            drawRect(brush = brush, blendMode = BlendMode.SrcOver)
        }
    }
}

/**
 * Sample placeholder shimmer (for skeleton loaders). Renders a tinted band that
 * sweeps across the receiver. Use on a Box with a fixed size and a clip shape.
 */
@Composable
fun rememberShimmerHighlight(): Color = Color.White.copy(alpha = 0.22f)

