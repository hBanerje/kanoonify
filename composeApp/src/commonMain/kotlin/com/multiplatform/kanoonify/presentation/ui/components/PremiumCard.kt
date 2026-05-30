package com.multiplatform.kanoonify.presentation.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.multiplatform.kanoonify.presentation.theme.Dimens
import com.multiplatform.kanoonify.presentation.theme.KanoonifyPremiumColors
import com.multiplatform.kanoonify.presentation.ui.modifiers.neonGlow

/**
 * Premium upsell card. Gold gradient, "Coming Soon" pill, benefit list, CTA button.
 * Stateless.
 */
@Composable
fun PremiumCard(
    title: String,
    subtitle: String,
    benefits: List<String>,
    comingSoonLabel: String,
    ctaLabel: String,
    onCtaClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gold = KanoonifyPremiumColors.GoldMid
    val shape = RoundedCornerShape(Dimens.RadiusHero)
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessMediumLow),
        label = "premiumCardScale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .neonGlow(gold, shape, radius = 20.dp, alpha = 0.55f)
            .clip(shape)
            .background(
                Brush.linearGradient(
                    listOf(
                        KanoonifyPremiumColors.GoldDeep.copy(alpha = 0.32f),
                        KanoonifyPremiumColors.GoldMid.copy(alpha = 0.18f),
                        KanoonifyPremiumColors.BgMid
                    )
                )
            )
            .border(width = 1.dp, color = gold.copy(alpha = 0.55f), shape = shape)
            .padding(Dimens.SpaceL)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .neonGlow(gold, RoundedCornerShape(Dimens.RadiusL), radius = 10.dp, alpha = 0.6f)
                        .clip(RoundedCornerShape(Dimens.RadiusL))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    KanoonifyPremiumColors.GoldLight,
                                    KanoonifyPremiumColors.GoldDeep
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("\uD83D\uDC51", fontSize = 20.sp)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(Dimens.RadiusPill))
                        .background(gold.copy(alpha = 0.18f))
                        .border(
                            width = 1.dp,
                            color = gold.copy(alpha = 0.45f),
                            shape = RoundedCornerShape(Dimens.RadiusPill)
                        )
                        .padding(horizontal = Dimens.SpaceM, vertical = 4.dp)
                ) {
                    Text(
                        text = comingSoonLabel,
                        color = KanoonifyPremiumColors.GoldLight,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.6.sp
                    )
                }
            }
            Spacer(Modifier.height(Dimens.SpaceL))
            Text(
                text = title,
                color = KanoonifyPremiumColors.TextHi,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.3).sp
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = subtitle,
                color = KanoonifyPremiumColors.TextMid,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
            Spacer(Modifier.height(Dimens.SpaceL))
            benefits.forEach { benefit ->
                BenefitRow(text = benefit, accent = gold)
                Spacer(Modifier.height(Dimens.SpaceS))
            }
            Spacer(Modifier.height(Dimens.SpaceS))
            // CTA
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(Dimens.RadiusPill))
                    .background(
                        Brush.linearGradient(
                            listOf(KanoonifyPremiumColors.GoldLight, KanoonifyPremiumColors.GoldDeep)
                        )
                    )
                    .clickable(
                        interactionSource = interaction,
                        indication = null,
                        onClick = onCtaClick
                    )
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = ctaLabel,
                    color = Color(0xFF231703),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.3.sp
                )
            }
        }
    }
}

@Composable
private fun BenefitRow(text: String, accent: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(accent.copy(alpha = 0.22f)),
            contentAlignment = Alignment.Center
        ) {
            Text("\u2713", color = accent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(Dimens.SpaceM))
        Text(
            text = text,
            color = KanoonifyPremiumColors.TextHi,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}


