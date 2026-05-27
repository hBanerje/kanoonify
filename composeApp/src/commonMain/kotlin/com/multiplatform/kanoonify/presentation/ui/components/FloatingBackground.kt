package com.multiplatform.kanoonify.presentation.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

/**
 * Decorative full-screen background:
 * - Soft vertical gradient wash
 * - Two slowly drifting/breathing tinted "orbs" that simulate blurred circles
 *
 * Render as the very first child of a parent Box, then overlay actual content above it.
 * Animations use rememberInfiniteTransition and graphicsLayer to avoid recomposition cost.
 */
@Composable
fun FloatingBackground(
    primaryTint: Color,
    secondaryTint: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "floatingBg")

    val orbAOffset by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 9000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orbAOffset"
    )
    val orbBOffset by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 12000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orbBOffset"
    )
    val orbAScale by transition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orbAScale"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        primaryTint.copy(alpha = 0.08f),
                        backgroundColor,
                        secondaryTint.copy(alpha = 0.05f)
                    )
                )
            )
    ) {
        // Orb A — top-right
        Box(
            modifier = Modifier
                .size(260.dp)
                .offset(
                    x = (140 + orbAOffset * 30).dp,
                    y = (-60 + orbAOffset * 20).dp
                )
                .graphicsLayer {
                    scaleX = orbAScale
                    scaleY = orbAScale
                    alpha = 0.55f
                }
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            primaryTint.copy(alpha = 0.35f),
                            Color.Transparent
                        )
                    )
                )
        )

        // Orb B — mid-left
        Box(
            modifier = Modifier
                .size(320.dp)
                .offset(
                    x = (-160 - orbBOffset * 20).dp,
                    y = (260 + orbBOffset * 40).dp
                )
                .graphicsLayer { alpha = 0.45f }
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            secondaryTint.copy(alpha = 0.32f),
                            Color.Transparent
                        )
                    )
                )
        )
    }
}

