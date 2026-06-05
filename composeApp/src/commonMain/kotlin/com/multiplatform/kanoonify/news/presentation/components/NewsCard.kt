package com.multiplatform.kanoonify.news.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.multiplatform.kanoonify.news.domain.model.NewsArticle
import com.multiplatform.kanoonify.news.domain.util.RelativeTime
import com.multiplatform.kanoonify.presentation.theme.Dimens
import com.multiplatform.kanoonify.presentation.theme.KanoonifyPremiumColors
import com.multiplatform.kanoonify.presentation.ui.modifiers.glassSurface
import com.multiplatform.kanoonify.presentation.ui.modifiers.neonGlow
import com.multiplatform.kanoonify.utils.SystemClock

/**
 * Full-bleed Inshorts-style news card. Designed for use inside a
 * VerticalPager — fills the parent box and uses the externally-driven
 * [pagerFraction] (0f = current, ±1f = neighbours) to drive a parallax
 * image translation and a scale/alpha breathing effect on the body.
 *
 *  - Pure UI; no business logic.
 *  - All actions are dispatched via the callback parameters.
 */
@Composable
fun NewsCard(
    article: NewsArticle,
    saved: Boolean,
    pagerFraction: Float,
    labels: NewsCardLabels,
    onSave: () -> Unit,
    onShare: () -> Unit,
    onReadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = accentFor(article.category)
    val parallax = pagerFraction.coerceIn(-1f, 1f)
    val bodyAlpha = (1f - kotlin.math.abs(parallax) * 0.5f).coerceIn(0.3f, 1f)
    val bodyScale = 0.94f + (1f - kotlin.math.abs(parallax)) * 0.06f

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.ScreenHorizontal, vertical = Dimens.SpaceM)
    ) {
        /* ---- Hero image (top half, with parallax) ---- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.46f)
        ) {
            NewsImage(
                imageUrl = article.imageUrl,
                seed = article.id,
                accent = accent,
                shape = RoundedCornerShape(Dimens.RadiusHero),
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        // Parallax: shift image opposite to page direction.
                        translationY = -parallax * size.height * 0.18f
                        scaleX = 1.08f
                        scaleY = 1.08f
                    },
                overlay = {
                    // Top-row: category badge + source pill
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Dimens.SpaceM),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        CategoryBadge(label = labels.categoryLabel, glyph = glyphFor(article.category), accent = accent)
                        if (article.source.isNotBlank()) {
                            SourcePill(text = article.source)
                        }
                    }
                }
            )
        }

        Spacer(Modifier.height(Dimens.SpaceL))

        /* ---- Body (scales/fades during paging) ---- */
        Column(
            modifier = Modifier
                .weight(1f)
                .graphicsLayer {
                    alpha = bodyAlpha
                    scaleX = bodyScale
                    scaleY = bodyScale
                }
        ) {
            Text(
                text = article.title,
                color = KanoonifyPremiumColors.TextHi,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.3).sp
            )
            Spacer(Modifier.height(Dimens.SpaceM))
            Text(
                text = article.description.ifBlank { article.content },
                color = KanoonifyPremiumColors.TextMid,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                maxLines = 8
            )

            Spacer(Modifier.weight(1f))

            /* ---- Meta row ---- */
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = RelativeTime.format(
                        publishedAtEpochMs = article.publishedAtEpochMs,
                        nowEpochMs = SystemClock.currentTimeMillis()
                    ),
                    color = KanoonifyPremiumColors.TextLow,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                if (article.author.isNotBlank()) {
                    Spacer(Modifier.width(Dimens.SpaceS))
                    Text(text = "·", color = KanoonifyPremiumColors.TextLow, fontSize = 12.sp)
                    Spacer(Modifier.width(Dimens.SpaceS))
                    Text(
                        text = article.author,
                        color = KanoonifyPremiumColors.TextLow,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(Modifier.height(Dimens.SpaceL))

            /* ---- Actions row ---- */
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceM),
                modifier = Modifier.fillMaxWidth()
            ) {
                ReadMorePill(label = labels.readMoreLabel, accent = accent, onClick = onReadMore, modifier = Modifier.weight(1f))
                IconCircleButton(glyph = "\u21AA", onClick = onShare, contentDescription = labels.shareLabel)
                SaveButton(saved = saved, onClick = onSave)
            }
        }
    }
}

/** UI text bundle so the screen owns localisation (component stays pure). */
data class NewsCardLabels(
    val categoryLabel: String,
    val readMoreLabel: String,
    val shareLabel: String,
    val saveLabel: String
)

/* ----------------------------- sub-pieces ---------------------------------- */

@Composable
private fun CategoryBadge(label: String, glyph: String, accent: Color) {
    val shape = RoundedCornerShape(Dimens.RadiusPill)
    Box(
        modifier = Modifier
            .clip(shape)
            .background(accent.copy(alpha = 0.85f))
            .neonGlow(accent, shape, radius = 12.dp, alpha = 0.6f)
            .padding(horizontal = Dimens.SpaceM, vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(glyph, color = Color.White, fontSize = 11.sp)
            Spacer(Modifier.width(6.dp))
            Text(
                text = label.uppercase(),
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.6.sp
            )
        }
    }
}

@Composable
private fun SourcePill(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(Dimens.RadiusPill))
            .background(Color.Black.copy(alpha = 0.45f))
            .border(width = 1.dp, color = Color.White.copy(alpha = 0.18f), shape = RoundedCornerShape(Dimens.RadiusPill))
            .padding(horizontal = Dimens.SpaceM, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.4.sp
        )
    }
}

@Composable
private fun ReadMorePill(label: String, accent: Color, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val interaction = remember { MutableInteractionSource() }
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(Dimens.RadiusPill))
            .background(
                Brush.linearGradient(
                    listOf(accent, accent.copy(alpha = 0.6f))
                )
            )
            .neonGlow(accent, RoundedCornerShape(Dimens.RadiusPill), radius = 14.dp, alpha = 0.55f)
            .clickable(interactionSource = interaction, indication = null, onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.3.sp
        )
    }
}

@Composable
private fun IconCircleButton(glyph: String, onClick: () -> Unit, contentDescription: String) {
    val interaction = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.10f))
            .border(width = 1.dp, color = KanoonifyPremiumColors.GlassStrokeHi, shape = CircleShape)
            .clickable(interactionSource = interaction, indication = null, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(glyph, color = Color.White, fontSize = 18.sp)
        // a11y label (visually hidden)
        Text(
            text = contentDescription,
            color = Color.Transparent,
            fontSize = 1.sp,
            modifier = Modifier.size(1.dp)
        )
    }
}

/* --------------------- shimmer skeleton variant ---------------------------- */

/** Loading-state skeleton with the same vertical rhythm as [NewsCard]. */
@Composable
fun NewsCardSkeleton(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "newsCardSkeleton")
    val pulse by transition.animateFloat(
        initialValue = 0.5f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "skeletonPulse"
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.ScreenHorizontal, vertical = Dimens.SpaceM)
    ) {
        SkeletonBlock(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.46f),
            shape = RoundedCornerShape(Dimens.RadiusHero),
            alpha = pulse * 0.4f
        )
        Spacer(Modifier.height(Dimens.SpaceL))
        SkeletonBlock(
            modifier = Modifier.fillMaxWidth().height(24.dp),
            shape = RoundedCornerShape(Dimens.RadiusS),
            alpha = pulse * 0.35f
        )
        Spacer(Modifier.height(Dimens.SpaceS))
        SkeletonBlock(
            modifier = Modifier.fillMaxWidth(0.7f).height(24.dp),
            shape = RoundedCornerShape(Dimens.RadiusS),
            alpha = pulse * 0.35f
        )
        Spacer(Modifier.height(Dimens.SpaceL))
        SkeletonBlock(
            modifier = Modifier.fillMaxWidth().height(14.dp),
            shape = RoundedCornerShape(Dimens.RadiusS),
            alpha = pulse * 0.25f
        )
        Spacer(Modifier.height(Dimens.SpaceS))
        SkeletonBlock(
            modifier = Modifier.fillMaxWidth().height(14.dp),
            shape = RoundedCornerShape(Dimens.RadiusS),
            alpha = pulse * 0.25f
        )
        Spacer(Modifier.height(Dimens.SpaceS))
        SkeletonBlock(
            modifier = Modifier.fillMaxWidth(0.8f).height(14.dp),
            shape = RoundedCornerShape(Dimens.RadiusS),
            alpha = pulse * 0.25f
        )
    }
}

@Composable
private fun SkeletonBlock(
    modifier: Modifier,
    shape: RoundedCornerShape,
    alpha: Float
) {
    Box(
        modifier = modifier
            .glassSurface(
                shape = shape,
                fill = Color.White.copy(alpha = alpha),
                stroke = KanoonifyPremiumColors.GlassStroke
            )
    )
}

