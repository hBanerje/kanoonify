package com.multiplatform.kanoonify.presentation.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedGradientBorder(
    modifier: Modifier = Modifier,
    colors: List<Color>,
    strokeWidth: Dp = 1.5.dp,
    cornerRadius: Dp = 28.dp,
    durationMillis: Int = 5500,
    content: @Composable () -> Unit
) {
    require(colors.size >= 2) { "AnimatedGradientBorder needs at least 2 colours" }

    val transition = rememberInfiniteTransition(label = "gradientBorder")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "gradientBorderPhase"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .drawBehind {
                val sw = strokeWidth.toPx()
                val cr = cornerRadius.toPx()

                val n = colors.size
                val k = phase * n
                val baseIdx = k.toInt()
                val frac = k - baseIdx
                val shifted = List(n) { i ->
                    val a = colors[(baseIdx + i) % n]
                    val b = colors[(baseIdx + i + 1) % n]
                    lerp(a, b, frac)
                }

                val ring = shifted + shifted.first()

                val brush = Brush.sweepGradient(
                    colors = ring,
                    center = Offset(size.width / 2f, size.height / 2f)
                )

                drawRoundRect(
                    brush = brush,
                    topLeft = Offset(sw / 2f, sw / 2f),
                    size = Size(size.width - sw, size.height - sw),
                    cornerRadius = CornerRadius(cr, cr),
                    style = Stroke(width = sw),
                    alpha = 0.95f
                )
            }
    ) {
        Box(modifier = Modifier.padding(strokeWidth)) {
            content()
        }
    }
}
