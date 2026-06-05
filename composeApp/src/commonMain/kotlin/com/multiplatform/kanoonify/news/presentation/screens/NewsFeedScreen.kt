package com.multiplatform.kanoonify.news.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.multiplatform.kanoonify.news.domain.model.NewsCategory
import com.multiplatform.kanoonify.news.presentation.components.CategoryChip
import com.multiplatform.kanoonify.news.presentation.components.NewsCard
import com.multiplatform.kanoonify.news.presentation.components.NewsCardLabels
import com.multiplatform.kanoonify.news.presentation.components.NewsCardSkeleton
import com.multiplatform.kanoonify.news.presentation.components.NewsToolbar
import com.multiplatform.kanoonify.news.presentation.state.LoadPhase
import com.multiplatform.kanoonify.news.presentation.state.NewsUiEvent
import com.multiplatform.kanoonify.news.presentation.viewmodel.NewsViewModel
import com.multiplatform.kanoonify.presentation.theme.Dimens
import com.multiplatform.kanoonify.presentation.theme.KanoonifyPremiumColors
import com.multiplatform.kanoonify.presentation.ui.components.EmptyLibraryState
import com.multiplatform.kanoonify.presentation.ui.components.FloatingBottomBar
import com.multiplatform.kanoonify.presentation.ui.components.FloatingOrbBackground
import kanoonify.composeapp.generated.resources.Res
import kanoonify.composeapp.generated.resources.*
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/* -------------------------- view-data (UI-only) ---------------------------- */

private data class CategoryVD(val category: NewsCategory, val labelRes: StringResource)

private val allCategories: List<CategoryVD> = listOf(
    CategoryVD(NewsCategory.Latest,     Res.string.news_category_latest),
    CategoryVD(NewsCategory.Politics,   Res.string.news_category_politics),
    CategoryVD(NewsCategory.Parliament, Res.string.news_category_parliament),
    CategoryVD(NewsCategory.Corporate,  Res.string.news_category_corporate),
    CategoryVD(NewsCategory.Finance,    Res.string.news_category_finance),
    CategoryVD(NewsCategory.Technology, Res.string.news_category_technology),
    CategoryVD(NewsCategory.Law,        Res.string.news_category_law),
    CategoryVD(NewsCategory.India,      Res.string.news_category_india),
    CategoryVD(NewsCategory.World,      Res.string.news_category_world),
    CategoryVD(NewsCategory.Business,   Res.string.news_category_business),
    CategoryVD(NewsCategory.Sports,     Res.string.news_category_sports)
)

/* -------------------------------- Feed ------------------------------------- */

/**
 * Inshorts-style vertical news feed.
 *
 *  - Sticky toolbar + category strip.
 *  - VerticalPager with parallax + scale animations per page.
 *  - Pull-down gesture (vertical drag at top) → triggers refresh.
 *  - Listens to [NewsViewModel.events] for navigation / share / external URLs.
 */
@Composable
fun NewsFeedScreen(
    viewModel: NewsViewModel,
    onArticleClick: (articleId: String) -> Unit,
    onShareText: (text: String, title: String) -> Unit,
    onOpenExternal: (url: String) -> Unit,
    onSearchClick: () -> Unit,
    onAskClick: () -> Unit,
    onHomeTabClick: () -> Unit,
    onSavedTabClick: () -> Unit,
    onProfileTabClick: () -> Unit
) {
    val state by viewModel.feed.collectAsState()

    // One-shot side effects
    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is NewsUiEvent.OpenDetail   -> onArticleClick(event.articleId)
                is NewsUiEvent.OpenExternal -> onOpenExternal(event.url)
                is NewsUiEvent.Share        -> onShareText(event.text, event.title)
                is NewsUiEvent.Toast        -> Unit /* future snackbar host */
            }
        }
    }

    val labels = NewsCardLabels(
        categoryLabel = stringResource(labelResFor(state.category)),
        readMoreLabel = stringResource(Res.string.news_action_read_more),
        shareLabel    = stringResource(Res.string.news_action_share),
        saveLabel     = stringResource(Res.string.news_action_save)
    )

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

            /* ----- Toolbar ----- */
            NewsToolbar(
                title = stringResource(Res.string.news_screen_title),
                subtitle = stringResource(Res.string.news_screen_subtitle),
                isOffline = state.isOffline,
                offlineLabel = stringResource(Res.string.news_offline_label),
                trailing = {
                    SearchAffordance(onClick = onSearchClick)
                }
            )

            Spacer(Modifier.height(Dimens.SpaceM))

            /* ----- Category strip ----- */
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceS),
                contentPadding = PaddingValues(vertical = 2.dp)
            ) {
                items(allCategories) { vd ->
                    val label = stringResource(vd.labelRes)
                    CategoryChip(
                        category = vd.category,
                        label = label,
                        selected = state.category == vd.category,
                        onClick = { viewModel.onCategorySelected(vd.category) }
                    )
                }
            }

            Spacer(Modifier.height(Dimens.SpaceM))

            /* ----- Body ----- */
            Box(modifier = Modifier.weight(1f)) {
                when (state.phase) {
                    LoadPhase.Loading -> NewsCardSkeleton(modifier = Modifier.fillMaxSize())
                    LoadPhase.Empty -> EmptyState(
                        onRetry = viewModel::onRetry,
                        retryLabel = stringResource(Res.string.news_action_retry)
                    )
                    LoadPhase.Error -> ErrorState(
                        message = state.errorMessage ?: stringResource(Res.string.news_error_body),
                        retryLabel = stringResource(Res.string.news_action_retry),
                        onRetry = viewModel::onRetry
                    )
                    LoadPhase.Ready -> {
                        FeedPager(
                            articles = state.articles,
                            savedIds = state.savedIds,
                            labels = labels,
                            isRefreshing = state.isRefreshing,
                            onRefresh = viewModel::onRefresh,
                            onArticleClick = { article -> viewModel.onArticleClick(article) },
                            onShare = { article -> viewModel.onShare(article) },
                            onSave = { article -> viewModel.onToggleSaved(article) }
                        )
                    }
                }
            }

            // Bottom safe area for floating bottom bar
            Spacer(Modifier.height(120.dp))
        }

        /* ----- Refresh indicator (top) ----- */
        if (state.isRefreshing) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .padding(top = 70.dp)
            ) {
                RefreshingPill(label = stringResource(Res.string.news_refreshing))
            }
        }

        /* ----- Bottom navigation ----- */
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
                selectedIndex = 1,
                onHomeClick = onHomeTabClick,
                onNewsClick = { /* already here */ },
                onAskClick = onAskClick,
                onSavedClick = onSavedTabClick,
                onProfileClick = onProfileTabClick
            )
        }
    }
}

@Composable
private fun FeedPager(
    articles: List<com.multiplatform.kanoonify.news.domain.model.NewsArticle>,
    savedIds: Set<String>,
    labels: NewsCardLabels,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onArticleClick: (com.multiplatform.kanoonify.news.domain.model.NewsArticle) -> Unit,
    onShare: (com.multiplatform.kanoonify.news.domain.model.NewsArticle) -> Unit,
    onSave: (com.multiplatform.kanoonify.news.domain.model.NewsArticle) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { articles.size })

    // Reset to the first item whenever the dataset is meaningfully replaced
    // (e.g. category changed) — avoids landing on a stale index.
    LaunchedEffect(articles.firstOrNull()?.id) {
        if (articles.isNotEmpty() && pagerState.currentPage > articles.lastIndex) {
            pagerState.scrollToPage(0)
        }
    }

    val pullToRefreshThreshold = 140f
    val dragAccumulator = remember { androidx.compose.runtime.mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(articles.firstOrNull()?.id) {
                detectVerticalDragGestures(
                    onDragStart = { dragAccumulator.value = 0f },
                    onDragEnd = {
                        if (!isRefreshing && dragAccumulator.value > pullToRefreshThreshold &&
                            pagerState.currentPage == 0
                        ) {
                            onRefresh()
                        }
                        dragAccumulator.value = 0f
                    },
                    onDragCancel = { dragAccumulator.value = 0f },
                    onVerticalDrag = { _, dy ->
                        if (pagerState.currentPage == 0 && dy > 0f) {
                            dragAccumulator.value += dy
                        } else {
                            dragAccumulator.value = 0f
                        }
                    }
                )
            }
    ) {
        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { pageIndex ->
            val article = articles[pageIndex]
            // pagerFraction in [-1f, 1f]
            val fraction by remember(pagerState) {
                derivedStateOf {
                    (pageIndex - pagerState.currentPage) - pagerState.currentPageOffsetFraction
                }
            }
            NewsCard(
                article = article,
                saved = article.id in savedIds,
                pagerFraction = fraction,
                labels = labels,
                onSave = { onSave(article) },
                onShare = { onShare(article) },
                onReadMore = { onArticleClick(article) },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

/* ----------------------------- helpers ------------------------------------- */

@Composable
private fun SearchAffordance(onClick: () -> Unit) {
    val interaction = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .padding(end = 4.dp)
            .clickable(interactionSource = interaction, indication = null, onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 4.dp)
    ) {
        Text("\uD83D\uDD0D", fontSize = 18.sp, color = KanoonifyPremiumColors.TextHi)
    }
}

@Composable
private fun EmptyState(onRetry: () -> Unit, retryLabel: String) {
    Box(modifier = Modifier.fillMaxSize().padding(top = Dimens.SpaceXL)) {
        EmptyLibraryState(
            glyph = "\uD83D\uDCF0",
            title = stringResource(Res.string.news_empty_title),
            body = stringResource(Res.string.news_empty_body),
            actionLabel = retryLabel,
            onAction = onRetry
        )
    }
}

@Composable
private fun ErrorState(message: String, retryLabel: String, onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().padding(top = Dimens.SpaceXL)) {
        EmptyLibraryState(
            glyph = "\u26A0",
            title = stringResource(Res.string.news_error_title),
            body = message,
            actionLabel = retryLabel,
            onAction = onRetry
        )
    }
}

@Composable
private fun RefreshingPill(label: String) {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .padding(horizontal = Dimens.SpaceL)
            .background(
                color = KanoonifyPremiumColors.NeonBlue.copy(alpha = 0.18f),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(Dimens.RadiusPill)
            )
            .padding(horizontal = Dimens.SpaceL, vertical = 6.dp)
    ) {
        Text(
            text = label,
            color = KanoonifyPremiumColors.TextHi,
            fontSize = 11.sp
        )
    }
}

private fun labelResFor(category: NewsCategory): StringResource = when (category) {
    NewsCategory.Latest     -> Res.string.news_category_latest
    NewsCategory.Politics   -> Res.string.news_category_politics
    NewsCategory.Parliament -> Res.string.news_category_parliament
    NewsCategory.Corporate  -> Res.string.news_category_corporate
    NewsCategory.Finance    -> Res.string.news_category_finance
    NewsCategory.Technology -> Res.string.news_category_technology
    NewsCategory.Law        -> Res.string.news_category_law
    NewsCategory.India      -> Res.string.news_category_india
    NewsCategory.World      -> Res.string.news_category_world
    NewsCategory.Business   -> Res.string.news_category_business
    NewsCategory.Sports     -> Res.string.news_category_sports
}






