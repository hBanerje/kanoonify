package com.multiplatform.kanoonify.news.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.multiplatform.kanoonify.news.domain.util.RelativeTime
import com.multiplatform.kanoonify.news.presentation.components.NewsImage
import com.multiplatform.kanoonify.news.presentation.components.SaveButton
import com.multiplatform.kanoonify.news.presentation.components.accentFor
import com.multiplatform.kanoonify.news.presentation.components.glyphFor
import com.multiplatform.kanoonify.news.presentation.state.NewsUiEvent
import com.multiplatform.kanoonify.news.presentation.viewmodel.NewsViewModel
import com.multiplatform.kanoonify.presentation.theme.Dimens
import com.multiplatform.kanoonify.presentation.theme.KanoonifyPremiumColors
import com.multiplatform.kanoonify.presentation.ui.components.AnimatedEntrance
import com.multiplatform.kanoonify.presentation.ui.components.EmptyLibraryState
import com.multiplatform.kanoonify.presentation.ui.components.FloatingOrbBackground
import com.multiplatform.kanoonify.presentation.ui.modifiers.glassSurface
import com.multiplatform.kanoonify.presentation.ui.modifiers.neonGlow
import com.multiplatform.kanoonify.utils.SystemClock
import kanoonify.composeapp.generated.resources.Res
import kanoonify.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

/**
 * Full-article detail screen. Hero image, headline, source/meta, body,
 * primary CTA to open the original URL + save & share affordances.
 */
@Composable
fun NewsDetailScreen(
    viewModel: NewsViewModel,
    articleId: String,
    onBack: () -> Unit,
    onShareText: (text: String, title: String) -> Unit,
    onOpenExternal: (url: String) -> Unit
) {
    val state by viewModel.detail.collectAsState()

    LaunchedEffect(articleId) { viewModel.loadDetail(articleId) }
    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
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

        when {
            state.isLoading -> LoadingState()
            state.article == null -> UnavailableState(onBack = onBack)
            else -> Loaded(
                viewModel = viewModel,
                onBack = onBack
            )
        }
    }
}

@Composable
private fun Loaded(viewModel: NewsViewModel, onBack: () -> Unit) {
    val state by viewModel.detail.collectAsState()
    val article = state.article ?: return
    val accent = accentFor(article.category)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        /* ----- Hero ----- */
        Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.42f).heightDp(320)) {
            NewsImage(
                imageUrl = article.imageUrl,
                seed = article.id,
                accent = accent,
                shape = RoundedCornerShape(bottomStart = Dimens.RadiusHero, bottomEnd = Dimens.RadiusHero),
                modifier = Modifier.fillMaxSize()
            )
            // Top bar overlay
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(Dimens.SpaceM),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackBubble(onBack = onBack)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceS)) {
                    ShareBubble(onShare = { viewModel.onShare(article) })
                    SaveButton(saved = state.isSaved, onClick = { viewModel.onToggleSaved(article) })
                }
            }
            // Bottom category badge
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(Dimens.SpaceL)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(Dimens.RadiusPill))
                        .background(accent.copy(alpha = 0.85f))
                        .neonGlow(accent, RoundedCornerShape(Dimens.RadiusPill), radius = 14.dp, alpha = 0.6f)
                        .padding(horizontal = Dimens.SpaceM, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(glyphFor(article.category), color = Color.White, fontSize = 12.sp)
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = article.category.displayName.uppercase(),
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.6.sp
                        )
                    }
                }
            }
        }

        /* ----- Content ----- */
        Column(modifier = Modifier.padding(horizontal = Dimens.ScreenHorizontal)) {
            Spacer(Modifier.height(Dimens.SpaceL))

            AnimatedEntrance(durationMillis = 460, slidePx = 18f) {
                Text(
                    text = article.title,
                    color = KanoonifyPremiumColors.TextHi,
                    fontSize = 24.sp,
                    lineHeight = 30.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.3).sp
                )
            }

            Spacer(Modifier.height(Dimens.SpaceM))

            AnimatedEntrance(delayMillis = 100) {
                MetaRow(
                    source = article.source,
                    author = article.author,
                    publishedAtEpochMs = article.publishedAtEpochMs
                )
            }

            Spacer(Modifier.height(Dimens.SpaceL))

            AnimatedEntrance(delayMillis = 180) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .glassSurface(
                            shape = RoundedCornerShape(Dimens.RadiusXL),
                            fill = Color.White.copy(alpha = 0.04f),
                            stroke = KanoonifyPremiumColors.GlassStroke
                        )
                        .padding(Dimens.SpaceL)
                ) {
                    Column {
                        if (article.description.isNotBlank() && article.description != article.content) {
                            Text(
                                text = article.description,
                                color = KanoonifyPremiumColors.TextMid,
                                fontSize = 15.sp,
                                lineHeight = 22.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(Modifier.height(Dimens.SpaceM))
                        }
                        Text(
                            text = article.content.ifBlank { article.description },
                            color = KanoonifyPremiumColors.TextHi,
                            fontSize = 14.sp,
                            lineHeight = 24.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(Dimens.SpaceXL))

            if (article.articleUrl.isNotBlank()) {
                AnimatedEntrance(delayMillis = 260) {
                    OpenOriginalButton(
                        label = stringResource(Res.string.news_detail_open_original),
                        accent = accent,
                        onClick = { viewModel.onOpenOriginal(article) }
                    )
                }
            }

            Spacer(Modifier.height(Dimens.SpaceXXL))
        }
    }
}

/* ------------------------------ subpieces ---------------------------------- */

@Composable
private fun MetaRow(source: String, author: String, publishedAtEpochMs: Long) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (source.isNotBlank()) {
            Text(
                text = source,
                color = KanoonifyPremiumColors.TextHi,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.width(Dimens.SpaceS))
            Text("·", color = KanoonifyPremiumColors.TextLow)
            Spacer(Modifier.width(Dimens.SpaceS))
        }
        Text(
            text = RelativeTime.format(publishedAtEpochMs, SystemClock.currentTimeMillis()),
            color = KanoonifyPremiumColors.TextLow,
            fontSize = 12.sp
        )
        if (author.isNotBlank()) {
            Spacer(Modifier.width(Dimens.SpaceS))
            Text("·", color = KanoonifyPremiumColors.TextLow)
            Spacer(Modifier.width(Dimens.SpaceS))
            Text(
                text = stringResource(Res.string.news_detail_by, author),
                color = KanoonifyPremiumColors.TextLow,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun OpenOriginalButton(label: String, accent: Color, onClick: () -> Unit) {
    val interaction = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Dimens.RadiusPill))
            .background(
                Brush.linearGradient(listOf(accent, accent.copy(alpha = 0.6f)))
            )
            .neonGlow(accent, RoundedCornerShape(Dimens.RadiusPill), radius = 16.dp, alpha = 0.55f)
            .clickable(interactionSource = interaction, indication = null, onClick = onClick)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.3.sp
        )
    }
}

@Composable
private fun BackBubble(onBack: () -> Unit) {
    val interaction = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .padding(2.dp)
            .androidx_size(40)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.55f))
            .clickable(interactionSource = interaction, indication = null, onClick = onBack),
        contentAlignment = Alignment.Center
    ) {
        Text("\u2039", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ShareBubble(onShare: () -> Unit) {
    val interaction = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .androidx_size(44)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.40f))
            .clickable(interactionSource = interaction, indication = null, onClick = onShare),
        contentAlignment = Alignment.Center
    ) {
        Text("\u21AA", color = Color.White, fontSize = 18.sp)
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize().statusBarsPadding().padding(Dimens.ScreenHorizontal),
        contentAlignment = Alignment.Center
    ) {
        com.multiplatform.kanoonify.news.presentation.components.NewsCardSkeleton(
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun UnavailableState(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = Dimens.ScreenHorizontal, vertical = Dimens.SpaceL)
    ) {
        BackBubble(onBack = onBack)
        Spacer(Modifier.height(Dimens.SpaceXXL))
        EmptyLibraryState(
            glyph = "\u2753",
            title = stringResource(Res.string.news_detail_unavailable_title),
            body = stringResource(Res.string.news_detail_unavailable_body)
        )
    }
}

/* Small modifiers for size with Int dp values to keep call-sites compact. */
private fun Modifier.androidx_size(dp: Int): Modifier =
    this.size(dp.dp)

private fun Modifier.heightDp(dp: Int): Modifier =
    this.height(dp.dp)



