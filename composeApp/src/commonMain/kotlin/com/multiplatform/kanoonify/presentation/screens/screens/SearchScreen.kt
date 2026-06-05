package com.multiplatform.kanoonify.presentation.screens.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.multiplatform.kanoonify.presentation.screens.viewmodel.RecentSearchItem
import com.multiplatform.kanoonify.presentation.screens.viewmodel.SearchUiEvent
import com.multiplatform.kanoonify.presentation.screens.viewmodel.SearchViewModel
import com.multiplatform.kanoonify.presentation.theme.Dimens
import com.multiplatform.kanoonify.presentation.theme.KanoonifyPremiumColors
import com.multiplatform.kanoonify.presentation.ui.components.AnimatedEntrance
import com.multiplatform.kanoonify.presentation.ui.components.EmptyLibraryState
import com.multiplatform.kanoonify.presentation.ui.components.FloatingBottomBar
import com.multiplatform.kanoonify.presentation.ui.components.FloatingOrbBackground
import com.multiplatform.kanoonify.presentation.ui.components.NeonTrendingChip
import com.multiplatform.kanoonify.presentation.ui.components.PremiumQuickAccessCard
import com.multiplatform.kanoonify.presentation.ui.components.PremiumSearchField
import com.multiplatform.kanoonify.presentation.ui.components.SearchHistoryCard
import kanoonify.composeapp.generated.resources.Res
import kanoonify.composeapp.generated.resources.*
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

private data class TopicVD(val labelRes: StringResource, val glyph: String, val accent: Color)
private data class QuickAccessVD(
    val titleRes: StringResource,
    val subtitleRes: StringResource,
    val glyph: String,
    val accent: Color,
    val onClick: () -> Unit
)

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onAskClick: (seedQuery: String?) -> Unit,
    onBrowseLawsClick: () -> Unit,
    onCoiClick: () -> Unit,
    onConsultLawyerClick: () -> Unit,
    onEmergencyClick: () -> Unit,
    onHomeTabClick: () -> Unit,
    onNewsTabClick: () -> Unit,
    onSavedTabClick: () -> Unit,
    onProfileTabClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is SearchUiEvent.NavigateToAsk     -> onAskClick(event.seedQuery)
                SearchUiEvent.NavigateToLaws       -> onBrowseLawsClick()
                SearchUiEvent.NavigateToCoi        -> onCoiClick()
                SearchUiEvent.NavigateToLawyers    -> onConsultLawyerClick()
                SearchUiEvent.NavigateToEmergency  -> onEmergencyClick()
                is SearchUiEvent.OpenResult        -> Unit
            }
        }
    }

    val popular = remember {
        listOf(
            TopicVD(Res.string.search_topic_police_rights,  "\uD83D\uDC6E", KanoonifyPremiumColors.NeonBlue),
            TopicVD(Res.string.search_topic_traffic_rules,  "\uD83D\uDEA6", KanoonifyPremiumColors.AlertOrange),
            TopicVD(Res.string.search_topic_women_safety,   "\uD83D\uDEE1", KanoonifyPremiumColors.NeonViolet),
            TopicVD(Res.string.search_topic_cyber_crime,    "\uD83D\uDCBB", KanoonifyPremiumColors.NeonCyan),
            TopicVD(Res.string.search_topic_fir_rights,     "\uD83D\uDCDD", KanoonifyPremiumColors.NeonIndigo),
            TopicVD(Res.string.search_topic_constitution,   "\uD83D\uDCDC", KanoonifyPremiumColors.AccentConstitution)
        )
    }
    val trending = remember {
        listOf(
            TopicVD(Res.string.search_topic_police_rights,  "\uD83D\uDED1", KanoonifyPremiumColors.AlertRed),
            TopicVD(Res.string.search_topic_traffic_rules,  "\uD83D\uDEA6", KanoonifyPremiumColors.AlertOrange),
            TopicVD(Res.string.search_topic_women_safety,   "\uD83D\uDEE1", KanoonifyPremiumColors.NeonViolet),
            TopicVD(Res.string.search_topic_cyber_crime,    "\uD83D\uDCBB", KanoonifyPremiumColors.NeonBlue),
            TopicVD(Res.string.search_topic_fir_rights,     "\uD83D\uDCDD", KanoonifyPremiumColors.NeonIndigo)
        )
    }
    val quick = remember {
        listOf(
            QuickAccessVD(
                Res.string.search_quick_browse_laws_title,
                Res.string.search_quick_browse_laws_subtitle,
                "\u2696", KanoonifyPremiumColors.AccentLaws,
                onClick = viewModel::onBrowseLaws
            ),
            QuickAccessVD(
                Res.string.search_quick_coi_title,
                Res.string.search_quick_coi_subtitle,
                "\uD83D\uDCDC", KanoonifyPremiumColors.AccentConstitution,
                onClick = viewModel::onOpenConstitution
            ),
            QuickAccessVD(
                Res.string.search_quick_emergency_title,
                Res.string.search_quick_emergency_subtitle,
                "\uD83D\uDEA8", KanoonifyPremiumColors.AccentEmergency,
                onClick = viewModel::onEmergencyRights
            ),
            QuickAccessVD(
                Res.string.search_quick_lawyer_title,
                Res.string.search_quick_lawyer_subtitle,
                "\uD83D\uDC68\u200D\u2696\uFE0F", KanoonifyPremiumColors.AccentLawyer,
                onClick = viewModel::onConsultLawyer
            )
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

            AnimatedEntrance(durationMillis = 460, slidePx = 18f) {
                Column {
                    Text(
                        text = stringResource(Res.string.search_screen_title),
                        color = KanoonifyPremiumColors.TextHi,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.3).sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = stringResource(Res.string.search_screen_subtitle),
                        color = KanoonifyPremiumColors.TextLow,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(Modifier.height(Dimens.SpaceL))

            AnimatedEntrance(delayMillis = 80, slidePx = 22f) {
                PremiumSearchField(
                    query = state.query,
                    onQueryChange = viewModel::onQueryChange,
                    placeholder = stringResource(Res.string.search_field_placeholder),
                    onSubmit = viewModel::onSubmit,
                    clearLabel = stringResource(Res.string.search_field_clear)
                )
            }

            Spacer(Modifier.height(Dimens.SpaceXXL))

            AnimatedEntrance(delayMillis = 160) {
                SearchSectionTitle(
                    title = stringResource(Res.string.search_section_recent_title),
                    caption = stringResource(Res.string.search_section_recent_caption),
                    trailing = if (!state.recentIsEmpty) stringResource(Res.string.search_section_recent_clear) else null,
                    onTrailingClick = viewModel::onClearHistory
                )
            }
            Spacer(Modifier.height(Dimens.SpaceM))

            if (state.recentIsEmpty) {
                AnimatedEntrance(delayMillis = 200) {
                    EmptyLibraryState(
                        glyph = "\uD83D\uDD52",
                        title = stringResource(Res.string.search_recent_empty_title),
                        body = stringResource(Res.string.search_recent_empty_body)
                    )
                }
            } else {
                RecentColumn(items = state.recent, onClick = viewModel::onRecentClick)
            }

            Spacer(Modifier.height(Dimens.SpaceXXL))

            AnimatedEntrance(delayMillis = 280) {
                SearchSectionTitle(
                    title = stringResource(Res.string.search_section_popular_title),
                    caption = stringResource(Res.string.search_section_popular_caption)
                )
            }
            Spacer(Modifier.height(Dimens.SpaceM))
            PopularGrid(topics = popular, onClick = viewModel::onTopicClick)

            Spacer(Modifier.height(Dimens.SpaceXXL))

            AnimatedEntrance(delayMillis = 360) {
                SearchSectionTitle(
                    title = stringResource(Res.string.search_section_trending_title),
                    caption = stringResource(Res.string.search_section_trending_caption)
                )
            }
            Spacer(Modifier.height(Dimens.SpaceM))
            AnimatedEntrance(delayMillis = 420) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceS),
                    contentPadding = PaddingValues(end = Dimens.SpaceL)
                ) {
                    items(trending) { topic ->
                        val label = stringResource(topic.labelRes)
                        NeonTrendingChip(
                            label = label,
                            glyph = topic.glyph,
                            accent = topic.accent,
                            onClick = { viewModel.onTopicClick(label) }
                        )
                    }
                }
            }

            Spacer(Modifier.height(Dimens.SpaceXXL))

            AnimatedEntrance(delayMillis = 500) {
                SearchSectionTitle(
                    title = stringResource(Res.string.search_section_quick_title),
                    caption = stringResource(Res.string.search_section_quick_caption)
                )
            }
            Spacer(Modifier.height(Dimens.SpaceM))
            QuickAccessGrid(features = quick)

            Spacer(Modifier.height(120.dp))
        }

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
                selectedIndex = -1,
                onHomeClick = onHomeTabClick,
                onNewsClick = onNewsTabClick,
                onAskClick = { onAskClick(null) },
                onSavedClick = onSavedTabClick,
                onProfileClick = onProfileTabClick
            )
        }
    }
}

@Composable
private fun SearchSectionTitle(
    title: String,
    caption: String,
    trailing: String? = null,
    onTrailingClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
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
        if (trailing != null && onTrailingClick != null) {
            Text(
                text = trailing,
                color = KanoonifyPremiumColors.NeonBlue,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(start = Dimens.SpaceM)
                    .clickableNoIndication(onTrailingClick)
            )
        }
    }
}

@Composable
private fun RecentColumn(
    items: List<RecentSearchItem>,
    onClick: (RecentSearchItem) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceS)) {
        items.forEachIndexed { index, item ->
            AnimatedEntrance(
                delayMillis = 220L + index * 60L,
                durationMillis = 420,
                slidePx = 16f
            ) {
                SearchHistoryCard(
                    query = item.query,
                    timestamp = item.timestamp,
                    onClick = { onClick(item) }
                )
            }
        }
    }
}

@Composable
private fun PopularGrid(topics: List<TopicVD>, onClick: (String) -> Unit) {
    val rows = topics.chunked(2)
    Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceM)) {
        rows.forEachIndexed { rowIndex, row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceM)
            ) {
                row.forEachIndexed { colIndex, topic ->
                    val index = rowIndex * 2 + colIndex
                    val label = stringResource(topic.labelRes)
                    Box(modifier = Modifier.weight(1f)) {
                        AnimatedEntrance(
                            delayMillis = 320L + index * 70L,
                            durationMillis = 420,
                            slidePx = 24f
                        ) {
                            PremiumQuickAccessCard(
                                title = label,
                                subtitle = " ",
                                glyph = topic.glyph,
                                accent = topic.accent,
                                onClick = { onClick(label) },
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
private fun QuickAccessGrid(features: List<QuickAccessVD>) {
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
                            delayMillis = 540L + index * 80L,
                            durationMillis = 420,
                            slidePx = 28f
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
private fun Modifier.clickableNoIndication(onClick: () -> Unit): Modifier {
    val interaction = remember { MutableInteractionSource() }
    return this.clickable(
        interactionSource = interaction,
        indication = null,
        onClick = onClick
    )
}
