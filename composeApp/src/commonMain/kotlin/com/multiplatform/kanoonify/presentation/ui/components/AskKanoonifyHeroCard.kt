package com.multiplatform.kanoonify.presentation.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.multiplatform.kanoonify.presentation.theme.Dimens
import com.multiplatform.kanoonify.presentation.theme.KanoonifyPremiumColors
import com.multiplatform.kanoonify.presentation.ui.modifiers.neonGlow

@Composable
fun AskKanoonifyHeroCard(
    title: String,
    subtitle: String,
    aiBadge: String,
    alwaysOnBadge: String,
    confidentialBadge: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(Dimens.RadiusHero)
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val transition = rememberInfiniteTransition(label = "askCard")

    val breathe by transition.animateFloat(
        initialValue = 0.995f, targetValue = 1.012f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathe"
    )
    val pressScale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessMediumLow),
        label = "askPress"
    )

    AnimatedGradientBorder(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                val s = breathe * pressScale
                scaleX = s; scaleY = s
            }

            .drawBehind {
                val haloRadius = size.minDimension * 0.85f
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            KanoonifyPremiumColors.NeonIndigo.copy(alpha = 0.55f),
                            KanoonifyPremiumColors.NeonViolet.copy(alpha = 0.20f),
                            Color.Transparent
                        ),
                        center = Offset(size.width / 2f, size.height / 2f),
                        radius = haloRadius
                    ),
                    center = Offset(size.width / 2f, size.height / 2f),
                    radius = haloRadius
                )
            }
            .neonGlow(
                color = KanoonifyPremiumColors.NeonIndigo,
                shape = shape,
                radius = 28.dp,
                alpha = 0.7f
            ),
        colors = listOf(
            KanoonifyPremiumColors.NeonBlue,
            KanoonifyPremiumColors.NeonIndigo,
            KanoonifyPremiumColors.NeonViolet,
            KanoonifyPremiumColors.NeonCyan
        ),
        cornerRadius = Dimens.RadiusHero,
        strokeWidth = 1.4.dp
    ) {
        Box(
            modifier = Modifier
                .clip(shape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF1B1F4E),
                            Color(0xFF14245C),
                            Color(0xFF0A1B4A)
                        )
                    )
                )

                .shimmerEffect(
                    highlightColor = Color.White.copy(alpha = 0.10f),
                    durationMillis = 3200
                )
                .clickable(
                    interactionSource = interaction,
                    indication = null,
                    onClick = onClick
                )
                .padding(Dimens.SpaceXL)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AiOrb()
                    Spacer(Modifier.width(Dimens.SpaceL))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = title,
                            color = KanoonifyPremiumColors.TextHi,
                            fontSize = 22.sp,
                            lineHeight = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(Dimens.SpaceXS))
                        Text(
                            text = subtitle,
                            color = KanoonifyPremiumColors.TextMid,
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                    Spacer(Modifier.width(Dimens.SpaceM))
                    ArrowAffordance()
                }

                Spacer(Modifier.height(Dimens.SpaceL))

                Row(horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceS)) {
                    MiniBadge(aiBadge)
                    MiniBadge(alwaysOnBadge)
                    MiniBadge(confidentialBadge)
                }
            }
        }
    }
}

@Composable
private fun AiOrb() {
    val t = rememberInfiniteTransition(label = "aiOrb")
    val pulse by t.animateFloat(
        initialValue = 0.88f, targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(1700, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "aiOrbPulse"
    )
    val haloAlpha by t.animateFloat(
        initialValue = 0.35f, targetValue = 0.75f,
        animationSpec = infiniteRepeatable(
            animation = tween(1700, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "aiOrbHalo"
    )
    Box(contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(78.dp)
                .graphicsLayer {
                    scaleX = pulse; scaleY = pulse; alpha = haloAlpha
                }
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            KanoonifyPremiumColors.NeonCyan.copy(alpha = 0.55f),
                            Color.Transparent
                        )
                    )
                )
        )
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        listOf(
                            KanoonifyPremiumColors.NeonBlue,
                            KanoonifyPremiumColors.NeonViolet
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "\u2728",
                color = Color.White,
                fontSize = 22.sp
            )
        }
    }
}

@Composable
private fun ArrowAffordance() {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.18f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "\u2192",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun MiniBadge(label: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(Dimens.RadiusPill))
            .background(Color.White.copy(alpha = 0.10f))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.85f),
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.6.sp
        )
    }
}
