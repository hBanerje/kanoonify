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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.multiplatform.kanoonify.presentation.screens.viewmodel.SavedFilter
import com.multiplatform.kanoonify.presentation.screens.viewmodel.SavedItem
import com.multiplatform.kanoonify.presentation.screens.viewmodel.SavedItemType
import com.multiplatform.kanoonify.presentation.screens.viewmodel.SavedViewModel
import com.multiplatform.kanoonify.presentation.theme.Dimens
import com.multiplatform.kanoonify.presentation.theme.KanoonifyPremiumColors
import com.multiplatform.kanoonify.presentation.ui.components.AnimatedEntrance
import com.multiplatform.kanoonify.presentation.ui.components.EmptyLibraryState
import com.multiplatform.kanoonify.presentation.ui.components.FloatingBottomBar
import com.multiplatform.kanoonify.presentation.ui.components.FloatingOrbBackground
import com.multiplatform.kanoonify.presentation.ui.components.SavedItemCard
import com.multiplatform.kanoonify.presentation.ui.components.SegmentedFilterChip
import kanoonify.composeapp.generated.resources.Res
import kanoonify.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

/* -------------------------- view-data (UI-only) ---------------------------- */

private data class SectionSpec(
    val type: SavedItemType,
    val titleRes: org.jetbrains.compose.resources.StringResource,
    val captionRes: org.jetbrains.compose.resources.StringResource,
    val glyph: String,
    val accent: Color,
    val tagLabel: String
)

private val sectionSpecs: List<SectionSpec>
    @Composable
    get() = listOf(
        SectionSpec(
            type = SavedItemType.Law,
            titleRes = Res.string.saved_section_laws_title,
            captionRes = Res.string.saved_section_laws_caption,
            glyph = "\u2696",
            accent = KanoonifyPremiumColors.AccentLaws,
            tagLabel = stringResource(Res.string.saved_filter_laws).uppercase()
        ),
        SectionSpec(
            type = SavedItemType.ConstitutionArticle,
            titleRes = Res.string.saved_section_coi_title,
            captionRes = Res.string.saved_section_coi_caption,
            glyph = "\uD83D\uDCDC",
            accent = KanoonifyPremiumColors.AccentConstitution,
            tagLabel = stringResource(Res.string.saved_filter_coi).uppercase()
        ),
        SectionSpec(
            type = SavedItemType.AiConversation,
            titleRes = Res.string.saved_section_ai_title,
            captionRes = Res.string.saved_section_ai_caption,
            glyph = "\u2728",
            accent = KanoonifyPremiumColors.NeonViolet,
            tagLabel = stringResource(Res.string.saved_filter_ai).uppercase()
        ),
        SectionSpec(
            type = SavedItemType.News,
            titleRes = Res.string.saved_section_news_title,
            captionRes = Res.string.saved_section_news_caption,
            glyph = "\uD83D\uDCF0",
            accent = KanoonifyPremiumColors.NeonCyan,
            tagLabel = stringResource(Res.string.saved_filter_news).uppercase()
        ),
        SectionSpec(
            type = SavedItemType.LawyerNote,
            titleRes = Res.string.saved_section_notes_title,
            captionRes = Res.string.saved_section_notes_caption,
            glyph = "\uD83D\uDCDD",
            accent = KanoonifyPremiumColors.AccentLawyer,
            tagLabel = stringResource(Res.string.saved_filter_notes).uppercase()
        )
    )

/* --------------------------------- Saved ----------------------------------- */

@Composable
fun SavedScreen(
    viewModel: SavedViewModel,
    onItemClick: (SavedItem) -> Unit,
    onExploreClick: () -> Unit,
    onAskClick: () -> Unit,
    onHomeTabClick: () -> Unit,
    onSearchTabClick: () -> Unit,
    onProfileTabClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val specs = sectionSpecs
    val removeLabel = stringResource(Res.string.saved_item_remove)

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
                        text = stringResource(Res.string.saved_screen_title),
                        color = KanoonifyPremiumColors.TextHi,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.3).sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = stringResource(Res.string.saved_screen_subtitle),
                        color = KanoonifyPremiumColors.TextLow,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(Modifier.height(Dimens.SpaceL))

            /* ----- Filter row ----- */
            AnimatedEntrance(delayMillis = 80) {
                FilterRow(
                    active = state.filter,
                    onChange = viewModel::onFilterChange
                )
            }

            Spacer(Modifier.height(Dimens.SpaceXL))

            if (state.isCompletelyEmpty) {
                AnimatedEntrance(delayMillis = 160) {
                    EmptyLibraryState(
                        glyph = "\uD83D\uDCDA",
                        title = stringResource(Res.string.saved_empty_title),
                        body = stringResource(Res.string.saved_empty_body),
                        actionLabel = stringResource(Res.string.saved_empty_action),
                        onAction = onExploreClick
                    )
                }
            } else {
                var stagger = 160L
                specs.forEach { spec ->
                    if (!state.shouldShowSection(spec.type)) return@forEach
                    val items = state.visibleFor(spec.type)
                    AnimatedEntrance(delayMillis = stagger) {
                        SavedSection(
                            spec = spec,
                            items = items,
                            removeLabel = removeLabel,
                            onItemClick = onItemClick,
                            onRemove = viewModel::onRemoveItem,
                            onClearSection = { viewModel.onClearSection(spec.type) }
                        )
                    }
                    Spacer(Modifier.height(Dimens.SpaceXL))
                    stagger += 90L
                }
            }

            // Bottom safe area so content clears the floating bar.
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
                searchLabel = stringResource(Res.string.landing_bottom_nav_search),
                askLabel = stringResource(Res.string.landing_bottom_nav_ask),
                savedLabel = stringResource(Res.string.landing_bottom_nav_saved),
                profileLabel = stringResource(Res.string.landing_bottom_nav_profile),
                selectedIndex = 3,
                onHomeClick = onHomeTabClick,
                onSearchClick = onSearchTabClick,
                onAskClick = onAskClick,
                onSavedClick = { /* already here */ },
                onProfileClick = onProfileTabClick
            )
        }
    }
}

/* --------------------------- private composables --------------------------- */

@Composable
private fun FilterRow(active: SavedFilter, onChange: (SavedFilter) -> Unit) {
    data class FilterChipVD(val filter: SavedFilter, val label: String, val accent: Color)
    val chips = listOf(
        FilterChipVD(SavedFilter.All,   stringResource(Res.string.saved_filter_all),   KanoonifyPremiumColors.NeonBlue),
        FilterChipVD(SavedFilter.Laws,  stringResource(Res.string.saved_filter_laws),  KanoonifyPremiumColors.AccentLaws),
        FilterChipVD(SavedFilter.Coi,   stringResource(Res.string.saved_filter_coi),   KanoonifyPremiumColors.AccentConstitution),
        FilterChipVD(SavedFilter.Ai,    stringResource(Res.string.saved_filter_ai),    KanoonifyPremiumColors.NeonViolet),
        FilterChipVD(SavedFilter.News,  stringResource(Res.string.saved_filter_news),  KanoonifyPremiumColors.NeonCyan),
        FilterChipVD(SavedFilter.Notes, stringResource(Res.string.saved_filter_notes), KanoonifyPremiumColors.AccentLawyer)
    )
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceS),
        contentPadding = PaddingValues(end = Dimens.SpaceL)
    ) {
        items(chips) { chip ->
            SegmentedFilterChip(
                label = chip.label,
                selected = chip.filter == active,
                accent = chip.accent,
                onClick = { onChange(chip.filter) }
            )
        }
    }
}

@Composable
private fun SavedSection(
    spec: SectionSpec,
    items: List<SavedItem>,
    removeLabel: String,
    onItemClick: (SavedItem) -> Unit,
    onRemove: (String) -> Unit,
    onClearSection: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(spec.titleRes),
                        color = KanoonifyPremiumColors.TextHi,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(0.dp))
                    if (items.isNotEmpty()) {
                        Text(
                            text = "  · ${
                                if (items.size == 1)
                                    stringResource(Res.string.saved_count_item, items.size)
                                else
                                    stringResource(Res.string.saved_count_items, items.size)
                            }",
                            color = KanoonifyPremiumColors.TextLow,
                            fontSize = 12.sp
                        )
                    }
                }
                Spacer(Modifier.height(2.dp))
                Text(
                    text = stringResource(spec.captionRes),
                    color = KanoonifyPremiumColors.TextLow,
                    fontSize = 12.sp
                )
            }
            if (items.isNotEmpty()) {
                val interaction = remember { MutableInteractionSource() }
                Text(
                    text = stringResource(Res.string.saved_section_clear),
                    color = spec.accent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(start = Dimens.SpaceM)
                        .clickable(
                            interactionSource = interaction,
                            indication = null,
                            onClick = onClearSection
                        )
                )
            }
        }
        Spacer(Modifier.height(Dimens.SpaceM))

        if (items.isEmpty()) {
            EmptyLibraryState(
                glyph = spec.glyph,
                title = stringResource(Res.string.saved_empty_title),
                body = stringResource(Res.string.saved_empty_body)
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceS)) {
                items.forEachIndexed { index, item ->
                    AnimatedEntrance(
                        delayMillis = index * 50L,
                        durationMillis = 380,
                        slidePx = 16f
                    ) {
                        SavedItemCard(
                            title = item.title,
                            subtitle = item.subtitle,
                            glyph = spec.glyph,
                            accent = spec.accent,
                            tagLabel = spec.tagLabel,
                            removeLabel = removeLabel,
                            onClick = { onItemClick(item) },
                            onRemove = { onRemove(item.id) }
                        )
                    }
                }
            }
        }
    }
}




