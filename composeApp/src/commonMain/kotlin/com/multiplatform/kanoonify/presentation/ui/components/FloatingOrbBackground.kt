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
import com.multiplatform.kanoonify.presentation.theme.KanoonifyPremiumColors

/**
 * Cinematic full-screen background for the premium dark Landing.
 *
 * Layers (back → front):
 *  1. Deep diagonal gradient wash (`BgDeep` → `BgMid` → `BgSoft`).
 *  2. Three blurred neon orbs that slowly drift + breathe — they imply
 *     depth and motion without spending fillrate every frame (each orb
 *     is a single radial-gradient circle moved via [graphicsLayer]).
 *
 * Render as the very first child of a parent Box, then overlay content
 * above it. Lightweight enough for low-end devices: only 3 composables,
 * no Canvas, no per-frame allocations.
 */
@Composable
fun FloatingOrbBackground(
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "premiumBg")

    val driftA by transition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(11_000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "driftA"
    )
    val driftB by transition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(14_000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "driftB"
    )
    val breath by transition.animateFloat(
        initialValue = 0.92f, targetValue = 1.10f,
        animationSpec = infiniteRepeatable(
            animation = tween(6_500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breath"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    0f   to KanoonifyPremiumColors.BgDeep,
                    0.55f to KanoonifyPremiumColors.BgMid,
                    1f   to KanoonifyPremiumColors.BgSoft
                )
            )
    ) {
        // Orb A — neon blue, top-right
        Orb(
            size = 320,
            color = KanoonifyPremiumColors.NeonBlue,
            xDp = (160 + driftA * 40).toInt(),
            yDp = (-80 + driftA * 30).toInt(),
            scale = breath,
            alpha = 0.45f
        )
        // Orb B — indigo, mid-left
        Orb(
            size = 380,
            color = KanoonifyPremiumColors.NeonIndigo,
            xDp = (-180 - driftB * 30).toInt(),
            yDp = (320 + driftB * 50).toInt(),
            scale = 1f,
            alpha = 0.38f
        )
        // Orb C — soft gold, lower-right
        Orb(
            size = 260,
            color = KanoonifyPremiumColors.GoldMid,
            xDp = (180 + driftB * 20).toInt(),
            yDp = (640 + driftA * 30).toInt(),
            scale = breath,
            alpha = 0.18f
        )
    }
}

@Composable
private fun Orb(
    size: Int,
    color: Color,
    xDp: Int,
    yDp: Int,
    scale: Float,
    alpha: Float
) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .offset(x = xDp.dp, y = yDp.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
            }
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(color.copy(alpha = 0.9f), Color.Transparent)
                )
            )
    )
}

