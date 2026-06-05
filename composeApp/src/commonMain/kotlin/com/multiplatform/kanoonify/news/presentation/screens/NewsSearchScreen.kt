package com.multiplatform.kanoonify.news.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.multiplatform.kanoonify.news.domain.model.NewsArticle
import com.multiplatform.kanoonify.news.domain.util.RelativeTime
import com.multiplatform.kanoonify.news.presentation.components.NewsImage
import com.multiplatform.kanoonify.news.presentation.components.NewsToolbar
import com.multiplatform.kanoonify.news.presentation.components.SaveButton
import com.multiplatform.kanoonify.news.presentation.components.accentFor
import com.multiplatform.kanoonify.news.presentation.state.LoadPhase
import com.multiplatform.kanoonify.news.presentation.state.NewsUiEvent
import com.multiplatform.kanoonify.news.presentation.viewmodel.NewsViewModel
import com.multiplatform.kanoonify.presentation.theme.Dimens
import com.multiplatform.kanoonify.presentation.theme.KanoonifyPremiumColors
import com.multiplatform.kanoonify.presentation.ui.components.AnimatedEntrance
import com.multiplatform.kanoonify.presentation.ui.components.EmptyLibraryState
import com.multiplatform.kanoonify.presentation.ui.components.FloatingOrbBackground
import com.multiplatform.kanoonify.presentation.ui.components.PremiumSearchField
import com.multiplatform.kanoonify.presentation.ui.modifiers.glassSurface
import com.multiplatform.kanoonify.utils.SystemClock
import kanoonify.composeapp.generated.resources.Res
import kanoonify.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun NewsSearchScreen(
    viewModel: NewsViewModel,
    onBack: () -> Unit,
    onArticleClick: (articleId: String) -> Unit,
    onShareText: (text: String, title: String) -> Unit,
    onOpenExternal: (url: String) -> Unit
) {
    val state by viewModel.search.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is NewsUiEvent.OpenDetail   -> onArticleClick(event.articleId)
                is NewsUiEvent.Share        -> onShareText(event.text, event.title)
                is NewsUiEvent.OpenExternal -> onOpenExternal(event.url)
                else -> Unit
            }
        }
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
                .padding(horizontal = Dimens.ScreenHorizontal)
        ) {
            Spacer(Modifier.height(Dimens.SpaceM))

            NewsToolbar(
                title = stringResource(Res.string.news_search_title),
                showBack = true,
                onBack = onBack,
                isOffline = state.isOffline,
                offlineLabel = stringResource(Res.string.news_offline_label)
            )

            Spacer(Modifier.height(Dimens.SpaceL))

            PremiumSearchField(
                query = state.query,
                onQueryChange = viewModel::onSearchQueryChange,
                placeholder = stringResource(Res.string.news_search_placeholder),
                onSubmit = viewModel::onSearchSubmit,
                clearLabel = "Clear"
            )

            Spacer(Modifier.height(Dimens.SpaceL))

            Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                when {
                    state.hasQuery && state.phase == LoadPhase.Empty && !state.isSearching -> {
                        EmptyLibraryState(
                            glyph = "\uD83D\uDD0D",
                            title = stringResource(Res.string.news_search_empty_title),
                            body = stringResource(Res.string.news_search_empty_body)
                        )
                    }
                    state.hasQuery -> {
                        ResultsList(
                            results = state.results,
                            savedIds = state.savedIds,
                            onClick = viewModel::onArticleClick,
                            onSave = viewModel::onToggleSaved
                        )
                    }
                    state.recent.isNotEmpty() -> {
                        RecentSearchesSection(
                            recent = state.recent,
                            onClick = viewModel::onRecentSearchClick,
                            onDelete = viewModel::onDeleteRecentSearch,
                            onClearAll = viewModel::onClearRecentSearches
                        )
                    }
                    else -> {
                        EmptyLibraryState(
                            glyph = "\u2728",
                            title = stringResource(Res.string.news_search_idle_title),
                            body = stringResource(Res.string.news_search_idle_body)
                        )
                    }
                }
                Spacer(Modifier.height(40.dp))
            }
        }
    }
}

@Composable
private fun ResultsList(
    results: List<NewsArticle>,
    savedIds: Set<String>,
    onClick: (NewsArticle) -> Unit,
    onSave: (NewsArticle) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceM)) {
        results.forEachIndexed { index, article ->
            AnimatedEntrance(
                delayMillis = index * 40L,
                durationMillis = 360,
                slidePx = 14f
            ) {
                ResultRow(
                    article = article,
                    saved = article.id in savedIds,
                    onClick = { onClick(article) },
                    onSave = { onSave(article) }
                )
            }
        }
    }
}

@Composable
private fun ResultRow(
    article: NewsArticle,
    saved: Boolean,
    onClick: () -> Unit,
    onSave: () -> Unit
) {
    val interaction = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(Dimens.RadiusXL)
    val accent = accentFor(article.category)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .glassSurface(
                shape = shape,
                fill = Color.White.copy(alpha = 0.05f),
                stroke = accent.copy(alpha = 0.25f)
            )
            .clickable(interactionSource = interaction, indication = null, onClick = onClick)
            .padding(Dimens.SpaceM)
    ) {
        NewsImage(
            imageUrl = article.imageUrl,
            seed = article.id,
            accent = accent,
            shape = RoundedCornerShape(Dimens.RadiusM),
            modifier = Modifier.size(72.dp)
        )
        Spacer(Modifier.width(Dimens.SpaceM))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = article.title,
                color = KanoonifyPremiumColors.TextHi,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2
            )
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = article.source.ifBlank { article.category.displayName },
                    color = accent,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.3.sp
                )
                Spacer(Modifier.width(6.dp))
                Text("·", color = KanoonifyPremiumColors.TextLow, fontSize = 11.sp)
                Spacer(Modifier.width(6.dp))
                Text(
                    text = RelativeTime.format(article.publishedAtEpochMs, SystemClock.currentTimeMillis()),
                    color = KanoonifyPremiumColors.TextLow,
                    fontSize = 11.sp
                )
            }
        }
        Spacer(Modifier.width(Dimens.SpaceS))
        SaveButton(saved = saved, onClick = onSave, diameter = 36.dp)
    }
}

@Composable
private fun RecentSearchesSection(
    recent: List<String>,
    onClick: (String) -> Unit,
    onDelete: (String) -> Unit,
    onClearAll: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(Res.string.news_search_recent_title),
                color = KanoonifyPremiumColors.TextHi,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            val interaction = remember { MutableInteractionSource() }
            Text(
                text = stringResource(Res.string.news_search_recent_clear),
                color = KanoonifyPremiumColors.NeonBlue,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable(
                    interactionSource = interaction,
                    indication = null,
                    onClick = onClearAll
                )
            )
        }
        Spacer(Modifier.height(Dimens.SpaceM))
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.SpaceS)) {
            recent.forEachIndexed { index, query ->
                AnimatedEntrance(delayMillis = index * 40L) {
                    RecentRow(
                        query = query,
                        onClick = { onClick(query) },
                        onDelete = { onDelete(query) }
                    )
                }
            }
        }
    }
}

@Composable
private fun RecentRow(query: String, onClick: () -> Unit, onDelete: () -> Unit) {
    val shape = RoundedCornerShape(Dimens.RadiusL)
    val interaction = remember { MutableInteractionSource() }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .glassSurface(
                shape = shape,
                fill = Color.White.copy(alpha = 0.04f),
                stroke = KanoonifyPremiumColors.GlassStroke
            )
            .clickable(interactionSource = interaction, indication = null, onClick = onClick)
            .padding(horizontal = Dimens.SpaceL, vertical = Dimens.SpaceM)
    ) {
        Text("\uD83D\uDD52", fontSize = 14.sp)
        Spacer(Modifier.width(Dimens.SpaceM))
        Text(
            text = query,
            color = KanoonifyPremiumColors.TextHi,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(Dimens.SpaceS))
        val deleteInteraction = remember { MutableInteractionSource() }
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.08f))
                .clickable(interactionSource = deleteInteraction, indication = null, onClick = onDelete),
            contentAlignment = Alignment.Center
        ) {
            Text("\u2715", color = KanoonifyPremiumColors.TextMid, fontSize = 12.sp)
        }
    }
}
