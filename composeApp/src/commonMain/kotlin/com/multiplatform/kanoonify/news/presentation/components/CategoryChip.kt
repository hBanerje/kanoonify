package com.multiplatform.kanoonify.news.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.multiplatform.kanoonify.news.domain.model.NewsCategory
import com.multiplatform.kanoonify.presentation.theme.Dimens
import com.multiplatform.kanoonify.presentation.theme.KanoonifyPremiumColors

@Composable
fun CategoryChip(
    category: NewsCategory,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = accentFor(category)
    val bg by animateColorAsState(
        targetValue = if (selected) accent.copy(alpha = 0.22f) else Color.White.copy(alpha = 0.05f),
        label = "categoryBg"
    )
    val border by animateColorAsState(
        targetValue = if (selected) accent.copy(alpha = 0.65f) else KanoonifyPremiumColors.GlassStroke,
        label = "categoryBorder"
    )
    val fg by animateColorAsState(
        targetValue = if (selected) KanoonifyPremiumColors.TextHi else KanoonifyPremiumColors.TextMid,
        label = "categoryFg"
    )
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.94f else 1f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessMedium),
        label = "categoryScale"
    )
    val shape = RoundedCornerShape(Dimens.RadiusPill)

    Box(
        modifier = modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clip(shape)
            .background(bg)
            .border(width = 1.dp, color = border, shape = shape)
            .clickable(interactionSource = interaction, indication = null, onClick = onClick)
            .padding(horizontal = Dimens.SpaceL, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (selected) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(Dimens.RadiusPill))
                        .background(accent)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = glyphFor(category),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.width(8.dp))
            } else {
                Text(
                    text = glyphFor(category),
                    color = fg,
                    fontSize = 12.sp
                )
                Spacer(Modifier.width(6.dp))
            }
            Text(
                text = label,
                color = fg,
                fontSize = 12.sp,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                letterSpacing = 0.3.sp
            )
        }
    }
}

fun accentFor(category: NewsCategory): Color = when (category) {
    NewsCategory.Latest     -> KanoonifyPremiumColors.NeonBlue
    NewsCategory.Politics   -> KanoonifyPremiumColors.AlertOrange
    NewsCategory.Parliament -> KanoonifyPremiumColors.NeonIndigo
    NewsCategory.Corporate  -> KanoonifyPremiumColors.NeonCyan
    NewsCategory.Finance    -> KanoonifyPremiumColors.GoldMid
    NewsCategory.Technology -> KanoonifyPremiumColors.NeonViolet
    NewsCategory.Law        -> KanoonifyPremiumColors.AccentLaws
    NewsCategory.India      -> KanoonifyPremiumColors.AccentConstitution
    NewsCategory.World      -> KanoonifyPremiumColors.NeonBlue
    NewsCategory.Business   -> KanoonifyPremiumColors.AccentLawyer
    NewsCategory.Sports     -> KanoonifyPremiumColors.AccentEmergency
}

fun glyphFor(category: NewsCategory): String = when (category) {
    NewsCategory.Latest     -> "\u2728"
    NewsCategory.Politics   -> "\uD83C\uDFDB"
    NewsCategory.Parliament -> "\uD83C\uDFDB"
    NewsCategory.Corporate  -> "\uD83C\uDFE2"
    NewsCategory.Finance    -> "\uD83D\uDCB9"
    NewsCategory.Technology -> "\uD83D\uDCBB"
    NewsCategory.Law        -> "\u2696"
    NewsCategory.India      -> "\uD83C\uDDEE\uD83C\uDDF3"
    NewsCategory.World      -> "\uD83C\uDF0D"
    NewsCategory.Business   -> "\uD83D\uDCBC"
    NewsCategory.Sports     -> "\uD83C\uDFC6"
}
