package com.multiplatform.kanoonify.presentation.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.multiplatform.kanoonify.presentation.theme.Dimens
import com.multiplatform.kanoonify.presentation.theme.KanoonifyPremiumColors

/**
 * Premium cinematic hero header.
 *
 * Composition (left column / right ornament):
 *  - Top tagline pill
 *  - Massive 2-line title with a gold-gradient highlighted word
 *  - Subtitle line
 *  - "Trusted by …" floating pill
 *  - Right side: low-opacity dome+pillars+scales silhouette built from
 *    primitives (no image asset required) with a soft halo behind it.
 *
 * Stateless. Animations are only on the ornament halo (one cheap
 * `rememberInfiniteTransition`).
 */
@Composable
fun PremiumHeroSection(
    tagline: String,
    titlePrefix: String,
    titleHighlight: String,
    subtitle: String,
    trustText: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            TopTaglinePill(text = tagline)

            Spacer(Modifier.height(Dimens.SpaceL))

            HeroTitle(prefix = titlePrefix, highlight = titleHighlight)

            Spacer(Modifier.height(Dimens.SpaceS))

            Text(
                text = subtitle,
                fontSize = 15.sp,
                lineHeight = 22.sp,
                color = KanoonifyPremiumColors.TextMid,
                fontWeight = FontWeight.Normal
            )

            Spacer(Modifier.height(Dimens.SpaceL))

            TrustPill(text = trustText)
        }

        Spacer(Modifier.size(Dimens.SpaceM))

        LegalOrnament(modifier = Modifier.size(118.dp))
    }
}

@Composable
private fun HeroTitle(prefix: String, highlight: String) {
    val goldBrush = Brush.linearGradient(
        colors = listOf(
            KanoonifyPremiumColors.GoldLight,
            KanoonifyPremiumColors.GoldMid,
            KanoonifyPremiumColors.GoldDeep
        )
    )
    val annotated = buildAnnotatedString {
        withStyle(
            SpanStyle(
                color = KanoonifyPremiumColors.TextHi,
                fontWeight = FontWeight.Bold
            )
        ) { append("$prefix\n") }
        withStyle(
            SpanStyle(
                brush = goldBrush,
                fontWeight = FontWeight.Black
            )
        ) { append(highlight) }
    }
    Text(
        text = annotated,
        fontSize = 36.sp,
        lineHeight = 42.sp,
        letterSpacing = (-0.5).sp
    )
}

@Composable
private fun TopTaglinePill(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(Dimens.RadiusPill))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        KanoonifyPremiumColors.NeonBlue.copy(alpha = 0.14f),
                        KanoonifyPremiumColors.NeonIndigo.copy(alpha = 0.14f)
                    )
                )
            )
            .padding(horizontal = Dimens.SpaceM, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = KanoonifyPremiumColors.NeonBlue,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.4.sp
        )
    }
}

@Composable
private fun TrustPill(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(Dimens.RadiusPill))
            .background(Color.White.copy(alpha = 0.06f))
            .padding(horizontal = Dimens.SpaceM, vertical = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(KanoonifyPremiumColors.NeonCyan)
        )
        Spacer(Modifier.size(Dimens.SpaceS))
        Text(
            text = text,
            color = KanoonifyPremiumColors.TextMid,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.3.sp
        )
    }
}

/**
 * Stylised low-opacity Supreme-Court silhouette (dome + pillars + scales)
 * built from primitives so no drawable asset is needed and it stays vector-crisp.
 */
@Composable
private fun LegalOrnament(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "ornament")
    val haloAlpha by transition.animateFloat(
        initialValue = 0.18f, targetValue = 0.40f,
        animationSpec = infiniteRepeatable(
            animation = tween(3200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ornamentHalo"
    )

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        // Halo
        Box(
            modifier = Modifier
                .size(118.dp)
                .graphicsLayer { alpha = haloAlpha }
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(
                            KanoonifyPremiumColors.NeonBlue.copy(alpha = 0.55f),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.graphicsLayer { alpha = 0.55f }
        ) {
            // Dome
            Box(
                modifier = Modifier
                    .size(width = 60.dp, height = 30.dp)
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                    .background(KanoonifyPremiumColors.TextMid.copy(alpha = 0.7f))
            )
            // Architrave
            Box(
                modifier = Modifier
                    .size(width = 68.dp, height = 4.dp)
                    .background(KanoonifyPremiumColors.TextMid.copy(alpha = 0.6f))
            )
            // Pillars
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(top = 2.dp)
            ) {
                repeat(5) {
                    Box(
                        modifier = Modifier
                            .size(width = 8.dp, height = 30.dp)
                            .background(KanoonifyPremiumColors.TextMid.copy(alpha = 0.55f))
                    )
                }
            }
            // Base
            Box(
                modifier = Modifier
                    .size(width = 74.dp, height = 3.dp)
                    .background(KanoonifyPremiumColors.TextMid.copy(alpha = 0.7f))
            )
            Spacer(Modifier.height(4.dp))
            // Scales glyph
            Text(
                text = "\u2696",
                color = KanoonifyPremiumColors.GoldMid.copy(alpha = 0.85f),
                fontSize = 18.sp
            )
        }
    }
}

