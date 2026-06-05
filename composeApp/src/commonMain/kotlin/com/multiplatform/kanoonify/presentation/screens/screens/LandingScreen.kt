package com.multiplatform.kanoonify.presentation.screens.screens

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.multiplatform.kanoonify.presentation.theme.Dimens
import com.multiplatform.kanoonify.presentation.theme.KanoonifyPremiumColors
import com.multiplatform.kanoonify.presentation.ui.components.AnimatedEntrance
import com.multiplatform.kanoonify.presentation.ui.components.AskKanoonifyHeroCard
import com.multiplatform.kanoonify.presentation.ui.components.EmergencyBanner
import com.multiplatform.kanoonify.presentation.ui.components.FloatingBottomBar
import com.multiplatform.kanoonify.presentation.ui.components.FloatingOrbBackground
import com.multiplatform.kanoonify.presentation.ui.components.NeonTrendingChip
import com.multiplatform.kanoonify.presentation.ui.components.PremiumHeroSection
import com.multiplatform.kanoonify.presentation.ui.components.PremiumQuickAccessCard
import com.multiplatform.kanoonify.presentation.ui.components.SearchHistoryCard
import kanoonify.composeapp.generated.resources.*
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/* -------------------------- view-data (UI-only) ---------------------------- */

private data class QuickAccessFeature(
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

private data class RecentSearch(
    val queryRes: StringResource,
    val timestamp: String
)

/* ------------------------------- Landing ----------------------------------- */

/**
 * Premium cinematic landing screen.
 *
 * Architecture notes:
 *  - Pure UI — no business logic, no data fetching, no NavController.
 *  - All navigation is dispatched via the `on*Click` callback parameters.
 *  - The screen paints its own dark cinematic surface independent of the
 *    global [com.multiplatform.kanoonify.presentation.theme.KanoonifyTheme],
 *    so other screens remain on the light scheme until they opt-in.
 *  - `recentSearches` defaults to the three localized samples from
 *    `strings.xml`; a future `LandingViewModel` can supply real data without
 *    changing the composable contract.
 */
@Composable
fun LandingScreen(
    onAskClick: () -> Unit,
    onBrowseLawsClick: () -> Unit,
    onCoiClick: () -> Unit,
    onConsultLawyerClick: () -> Unit,
    onEmergencyRightsClick: () -> Unit = onAskClick,
    onTrendingClick: (String) -> Unit = { onAskClick() },
    onRecentSearchClick: (String) -> Unit = { onAskClick() },
    onNewsTabClick: () -> Unit = { /* reserved for future News screen */ },
    onSavedTabClick: () -> Unit = { /* reserved for future Saved screen */ },
    onProfileTabClick: () -> Unit = { /* reserved for future Profile screen */ }
) {
    val features = remember {
        listOf(
            QuickAccessFeature(
                Res.string.landing_feature_browse_laws_title,
                Res.string.landing_feature_browse_laws_subtitle,
                "\u2696", KanoonifyPremiumColors.AccentLaws, onBrowseLawsClick
            ),
            QuickAccessFeature(
                Res.string.landing_feature_constitution_title,
                Res.string.landing_feature_constitution_subtitle,
                "\uD83D\uDCDC", KanoonifyPremiumColors.AccentConstitution, onCoiClick
            ),
            QuickAccessFeature(
                Res.string.landing_feature_consult_lawyer_title,
                Res.string.landing_feature_consult_lawyer_subtitle,
                "\uD83D\uDC68\u200D\u2696\uFE0F", KanoonifyPremiumColors.AccentLawyer, onConsultLawyerClick
            ),
            QuickAccessFeature(
                Res.string.landing_feature_emergency_title,
                Res.string.landing_feature_emergency_subtitle,
                "\uD83D\uDEA8", KanoonifyPremiumColors.AccentEmergency, onEmergencyRightsClick
            )
        )
    }

    val trending = remember {
        listOf(
            TrendingTopic(Res.string.trending_police_stops, "\uD83D\uDED1", KanoonifyPremiumColors.AlertRed),
            TrendingTopic(Res.string.trending_traffic_rules, "\uD83D\uDEA6", KanoonifyPremiumColors.AlertOrange),
            TrendingTopic(Res.string.trending_women_safety, "\uD83D\uDEE1", KanoonifyPremiumColors.NeonViolet),
            TrendingTopic(Res.string.trending_cyber_crime, "\uD83D\uDCBB", KanoonifyPremiumColors.NeonBlue),
            TrendingTopic(Res.string.trending_fir_rights, "\uD83D\uDCDD", KanoonifyPremiumColors.NeonIndigo)
        )
    }

    val recent = remember {
        listOf(
            RecentSearch(Res.string.landing_recent_example_1, "2 hours ago"),
            RecentSearch(Res.string.landing_recent_example_2, "Yesterday"),
            RecentSearch(Res.string.landing_recent_example_3, "3 days ago")
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(KanoonifyPremiumColors.BgDeep)
    ) {
        FloatingOrbBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Dimens.ScreenHorizontal)
        ) {
            Spacer(Modifier.height(Dimens.SpaceXL))

            AnimatedEntrance(durationMillis = 520, slidePx = 24f) {
                PremiumHeroSection(
                    tagline = stringResource(Res.string.landing_top_tagline),
                    titlePrefix = stringResource(Res.string.landing_hero_title_prefix),
                    titleHighlight = stringResource(Res.string.landing_hero_title_highlight),
                    subtitle = stringResource(Res.string.landing_hero_subtitle),
                    trustText = stringResource(Res.string.landing_hero_trust_pill)
                )
            }

            Spacer(Modifier.height(Dimens.SpaceXXL))

            AnimatedEntrance(delayMillis = 140, durationMillis = 520, slidePx = 28f) {
                AskKanoonifyHeroCard(
                    title = stringResource(Res.string.landing_ask_card_title),
                    subtitle = stringResource(Res.string.landing_ask_card_subtitle),
                    aiBadge = stringResource(Res.string.landing_ask_card_badge_ai),
                    alwaysOnBadge = stringResource(Res.string.landing_ask_card_badge_247),
                    confidentialBadge = stringResource(Res.string.landing_ask_card_badge_confidential),
                    onClick = onAskClick
                )
            }

            Spacer(Modifier.height(Dimens.SpaceXXL))

            AnimatedEntrance(delayMillis = 240) {
                SectionTitle(
                    title = stringResource(Res.string.landing_section_quick_access_title),
                    caption = stringResource(Res.string.landing_section_quick_access_caption)
                )
            }

            Spacer(Modifier.height(Dimens.SpaceL))

            QuickAccessGrid(features = features)

            Spacer(Modifier.height(Dimens.SpaceXXL))

            AnimatedEntrance(delayMillis = 380) {
                EmergencyBanner(
                    title = stringResource(Res.string.landing_emergency_banner_title),
                    actionLabel = stringResource(Res.string.landing_emergency_banner_action),
                    onClick = onEmergencyRightsClick
                )
            }

            Spacer(Modifier.height(Dimens.SpaceXXL))

            AnimatedEntrance(delayMillis = 440) {
                SectionTitle(
                    title = stringResource(Res.string.landing_section_trending_title),
                    caption = stringResource(Res.string.landing_section_trending_caption)
                )
            }

            Spacer(Modifier.height(Dimens.SpaceM))

            AnimatedEntrance(delayMillis = 500) {
                TrendingRow(items = trending, onClick = onTrendingClick)
            }

            Spacer(Modifier.height(Dimens.SpaceXXL))

            AnimatedEntrance(delayMillis = 560) {
                SectionTitle(
                    title = stringResource(Res.string.landing_section_recent_title),
                    caption = stringResource(Res.string.landing_section_recent_caption)
                )
            }

            Spacer(Modifier.height(Dimens.SpaceM))

            RecentSearchesColumn(items = recent, onClick = onRecentSearchClick)

            // Bottom safe area so content clears the floating bottom bar.
            Spacer(Modifier.height(120.dp))
        }

        // Floating glass bottom navigation
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = Dimens.SpaceL)
        ) {
            FloatingBottomBar(
                homeLabel = stringResource(Res.string.landing_bottom_nav_home),
                newsLabel = stringResource(Res.string.landing_bottom_nav_news),
                askLabel = stringResource(Res.string.landing_bottom_nav_ask),
                savedLabel = stringResource(Res.string.landing_bottom_nav_saved),
                profileLabel = stringResource(Res.string.landing_bottom_nav_profile),
                selectedIndex = 0,
                onHomeClick = { /* already here */ },
                onNewsClick = onNewsTabClick,
                onAskClick = onAskClick,
                onSavedClick = onSavedTabClick,
                onProfileClick = onProfileTabClick
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String, caption: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            color = KanoonifyPremiumColors.TextHi,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.2.sp
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = caption,
            color = KanoonifyPremiumColors.TextLow,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun QuickAccessGrid(features: List<QuickAccessFeature>) {
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
                            slidePx = 32f
                        ) {
                            PremiumQuickAccessCard(
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
private fun TrendingRow(
    items: List<TrendingTopic>,
    onClick: (String) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceS),
        contentPadding = PaddingValues(end = Dimens.SpaceL)
    ) {
        items(items) { topic ->
            val label = stringResource(topic.labelRes)
            NeonTrendingChip(
                label = label,
                glyph = topic.glyph,
                accent = topic.accent,
                onClick = { onClick(label) }
            )
        }
    }
}

@Composable
private fun RecentSearchesColumn(
    items: List<RecentSearch>,
    onClick: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceS)) {
        items.forEachIndexed { index, item ->
            val query = stringResource(item.queryRes)
            AnimatedEntrance(
                delayMillis = 600L + index * 80L,
                durationMillis = 420,
                slidePx = 20f
            ) {
                SearchHistoryCard(
                    query = query,
                    timestamp = item.timestamp,
                    onClick = { onClick(query) }
                )
            }
        }
    }
}
