package com.multiplatform.kanoonify.presentation.screens.screens

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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.multiplatform.kanoonify.presentation.theme.Dimens
import com.multiplatform.kanoonify.presentation.ui.components.AnimatedEntrance
import com.multiplatform.kanoonify.presentation.ui.components.AnimatedGradientCard
import com.multiplatform.kanoonify.presentation.ui.components.AnimatedHeroSection
import com.multiplatform.kanoonify.presentation.ui.components.FloatingBackground
import com.multiplatform.kanoonify.presentation.ui.components.PremiumFeatureCard
import com.multiplatform.kanoonify.presentation.ui.components.TrendingChip
import kanoonify.composeapp.generated.resources.*
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

private data class HomeFeature(
    val titleRes: StringResource,
    val subtitleRes: StringResource,
    val glyph: String,
    val accent: Color,
    val onClick: () -> Unit
)

private data class TrendingTopic(
    val labelRes: StringResource,
    val glyph: String,
    val accent: Color
)

@Composable
fun LandingScreen(
    onAskClick: () -> Unit,
    onBrowseLawsClick: () -> Unit,
    onCoiClick: () -> Unit,
    onConsultLawyerClick: () -> Unit,
    onEmergencyRightsClick: () -> Unit = onAskClick,
    onTrendingClick: (String) -> Unit = { onAskClick() }
) {
    val features = remember1 {
        listOf(
            HomeFeature(
                Res.string.landing_feature_browse_laws_title,
                Res.string.landing_feature_browse_laws_subtitle,
                "\u2696", Color(0xFF1E88E5), onBrowseLawsClick
            ),
            HomeFeature(
                Res.string.landing_feature_constitution_title,
                Res.string.landing_feature_constitution_subtitle,
                "\uD83D\uDCDC", Color(0xFF8E24AA), onCoiClick
            ),
            HomeFeature(
                Res.string.landing_feature_consult_lawyer_title,
                Res.string.landing_feature_consult_lawyer_subtitle,
                "\uD83D\uDCAC", Color(0xFF00897B), onConsultLawyerClick
            ),
            HomeFeature(
                Res.string.landing_feature_emergency_title,
                Res.string.landing_feature_emergency_subtitle,
                "\uD83D\uDEA8", Color(0xFFE53935), onEmergencyRightsClick
            )
        )
    }

    val trending = remember1 {
        listOf(
            TrendingTopic(Res.string.trending_police_stops, "\uD83D\uDED1", Color(0xFFE53935)),
            TrendingTopic(Res.string.trending_traffic_rules, "\uD83D\uDEA6", Color(0xFFEF6C00)),
            TrendingTopic(Res.string.trending_women_safety, "\uD83D\uDEE1", Color(0xFFD81B60)),
            TrendingTopic(Res.string.trending_cyber_crime, "\uD83D\uDCBB", Color(0xFF1E88E5)),
            TrendingTopic(Res.string.trending_fir_rights, "\uD83D\uDCDD", Color(0xFF6A1B9A))
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Decorative animated background
        FloatingBackground(
            primaryTint = MaterialTheme.colorScheme.primary,
            secondaryTint = MaterialTheme.colorScheme.secondary,
            backgroundColor = MaterialTheme.colorScheme.background
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Dimens.ScreenHorizontal)
        ) {
            Spacer(Modifier.height(Dimens.SpaceXXL))

            AnimatedEntrance(durationMillis = 520, slidePx = 28f) {
                AnimatedHeroSection(
                    title = stringResource(Res.string.landing_hero_title),
                    subtitle = stringResource(Res.string.landing_hero_subtitle),
                    badgeText = stringResource(Res.string.landing_hero_badge_beta)
                )
            }

            Spacer(Modifier.height(Dimens.SpaceXXL))

            AnimatedEntrance(delayMillis = 160, durationMillis = 520, slidePx = 32f) {
                AskKanoonifyCard(onClick = onAskClick)
            }

            Spacer(Modifier.height(Dimens.SpaceXXL))

            AnimatedEntrance(delayMillis = 240) {
                SectionTitle(
                    title = stringResource(Res.string.landing_section_quick_access_title),
                    caption = stringResource(Res.string.landing_section_quick_access_caption)
                )
            }

            Spacer(Modifier.height(Dimens.SpaceL))

            FeatureGrid(features)

            Spacer(Modifier.height(Dimens.SpaceXXL))

            AnimatedEntrance(delayMillis = 440) {
                SectionTitle(
                    title = stringResource(Res.string.landing_section_trending_title),
                    caption = stringResource(Res.string.landing_section_trending_caption)
                )
            }

            Spacer(Modifier.height(Dimens.SpaceM))

            AnimatedEntrance(delayMillis = 500) {
                TrendingRow(trending, onTrendingClick)
            }

            Spacer(Modifier.height(Dimens.SpaceXXL))
            Spacer(Modifier.height(Dimens.SpaceL))
        }
    }
}

/* --------------------------- internal building blocks ---------------------- */

@Composable
private fun AskKanoonifyCard(onClick: () -> Unit) {
    val primary = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary

    AnimatedGradientCard(
        colors = listOf(
            primary,
            secondary,
            primary.copy(
                red = (primary.red * 0.85f).coerceIn(0f, 1f),
                blue = (primary.blue * 1.1f).coerceIn(0f, 1f)
            )
        ),
        onClick = onClick,
        glowColor = primary,
        elevation = 20.dp,
        modifier = Modifier.fillMaxWidth(),
        enableShimmer = true
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            GlowingAiIcon()
            Spacer(Modifier.width(Dimens.SpaceL))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(Res.string.landing_ask_card_title),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = Color.White
                )
                Spacer(Modifier.height(Dimens.SpaceXS))
                Text(
                    text = stringResource(Res.string.landing_ask_card_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
            Spacer(Modifier.width(Dimens.SpaceM))
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Text("\u2192", style = MaterialTheme.typography.titleMedium, color = Color.White)
            }
        }
    }
}

/** Pulsing/glowing AI icon used inside the Ask Kanoonify card. */
@Composable
private fun GlowingAiIcon() {
    val transition = rememberInfiniteTransition(label = "aiIcon")
    val pulse by transition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "aiPulse"
    )
    val haloAlpha by transition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "aiHalo"
    )

    Box(contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .graphicsLayer {
                    scaleX = pulse
                    scaleY = pulse
                    alpha = haloAlpha
                }
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.55f),
                            Color.Transparent
                        )
                    )
                )
        )
        Box(
            modifier = Modifier
                .size(56.dp)
                .shadow(elevation = 12.dp, shape = CircleShape, spotColor = Color.White)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.22f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "\u2728",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String, caption: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(Dimens.SpaceXS))
        Text(
            text = caption,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun FeatureGrid(features: List<HomeFeature>) {
    val rows = features.chunked(2)
    Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceM)) {
        rows.forEachIndexed { rowIndex, row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceM)
            ) {
                row.forEachIndexed { colIndex, feature ->
                    val index = rowIndex * 2 + colIndex
                    val title = stringResource(feature.titleRes)
                    val subtitle = stringResource(feature.subtitleRes)
                    Box(modifier = Modifier.weight(1f)) {
                        AnimatedEntrance(
                            delayMillis = (300L + index * 90L),
                            durationMillis = 460,
                            slidePx = 36f
                        ) {
                            PremiumFeatureCard(
                                title = title,
                                subtitle = subtitle,
                                glyph = feature.glyph,
                                accent = feature.accent,
                                onClick = feature.onClick,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun TrendingRow(items: List<TrendingTopic>, onClick: (String) -> Unit) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceS),
        contentPadding = PaddingValues(end = Dimens.SpaceL)
    ) {
        items(items) { topic ->
            val label = stringResource(topic.labelRes)
            TrendingChip(
                label = label,
                glyph = topic.glyph,
                accent = topic.accent,
                onClick = { onClick(label) }
            )
        }
    }
}

/* ----- tiny `remember` alias used above to keep declarations terse ---------- */
@Composable
private fun <T> remember1(calculation: () -> T): T =
    androidx.compose.runtime.remember(calculation)
