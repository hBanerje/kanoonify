package com.multiplatform.kanoonify.news.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.multiplatform.kanoonify.presentation.theme.KanoonifyPremiumColors

/**
 * News-card image surface.
 *
 *  - No 3rd-party image-loader dependency: paints a stable gradient seeded
 *    from the article id + an accent overlay. Works on all KMP targets,
 *    air-gapped builds and unit tests with zero IO.
 *  - When a real loader is added later (e.g. Coil 3 multiplatform), only
 *    this composable changes — call-sites stay identical.
 *  - Accepts a [Shape] so it can be used for hero, card and thumbnail roles.
 */
@Composable
fun NewsImage(
    imageUrl: String,
    seed: String,
    accent: Color,
    shape: Shape,
    modifier: Modifier = Modifier,
    overlay: (@Composable () -> Unit)? = null,
    showShimmer: Boolean = false,
    fallbackGlyph: String = "\uD83D\uDCF0"
) {
    // Stable two-color gradient derived from the seed so the same article
    // always renders the same background.
    val palette = gradientPaletteFor(seed, accent)

    Box(
        modifier = modifier
            .clip(shape)
            .background(
                Brush.linearGradient(
                    colors = palette,
                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end   = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                )
            )
    ) {
        if (showShimmer) {
            ShimmerOverlay(modifier = Modifier.fillMaxSize())
        }
        // Subtle bottom-to-top dark vignette so overlaid text always reads.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f   to Color.Transparent,
                        0.6f to Color.Black.copy(alpha = 0.20f),
                        1f   to Color.Black.copy(alpha = 0.55f)
                    )
                )
        )
        // Fallback glyph centered when no overlay supplied.
        if (overlay == null && imageUrl.isBlank()) {
            Text(
                text = fallbackGlyph,
                fontSize = 36.sp,
                color = Color.White.copy(alpha = 0.55f),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        overlay?.invoke()
    }
}

@Composable
private fun ShimmerOverlay(modifier: Modifier) {
    val transition = rememberInfiniteTransition(label = "newsImageShimmer")
    val translate by transition.animateFloat(
        initialValue = -0.4f, targetValue = 1.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslate"
    )
    Box(
        modifier = modifier
            .graphicsLayer { alpha = 0.55f }
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.White.copy(alpha = 0.18f),
                        Color.Transparent
                    ),
                    start = androidx.compose.ui.geometry.Offset(translate * 1000f, 0f),
                    end   = androidx.compose.ui.geometry.Offset((translate + 0.3f) * 1000f, 0f)
                )
            )
    )
}

/** Deterministic 2-stop gradient palette derived from [seed]. */
private fun gradientPaletteFor(seed: String, accent: Color): List<Color> {
    val palettes = listOf(
        listOf(KanoonifyPremiumColors.NeonBlue,   KanoonifyPremiumColors.NeonViolet),
        listOf(KanoonifyPremiumColors.NeonViolet, KanoonifyPremiumColors.NeonIndigo),
        listOf(KanoonifyPremiumColors.NeonCyan,   KanoonifyPremiumColors.NeonBlue),
        listOf(KanoonifyPremiumColors.NeonIndigo, accent),
        listOf(accent,                            KanoonifyPremiumColors.NeonViolet),
        listOf(KanoonifyPremiumColors.GoldMid,    KanoonifyPremiumColors.AccentEmergency),
        listOf(KanoonifyPremiumColors.AccentLawyer, KanoonifyPremiumColors.NeonBlue),
        listOf(KanoonifyPremiumColors.AccentConstitution, KanoonifyPremiumColors.NeonCyan)
    )
    val idx = ((seed.hashCode() and 0x7FFFFFFF) % palettes.size)
    return palettes[idx]
}

