package com.multiplatform.kanoonify.presentation.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.multiplatform.kanoonify.presentation.ui.modifiers.glassSurface
import com.multiplatform.kanoonify.presentation.ui.modifiers.neonGlow

@Composable
fun FloatingBottomBar(
    homeLabel: String,
    newsLabel: String,
    askLabel: String,
    savedLabel: String,
    profileLabel: String,
    selectedIndex: Int,
    onHomeClick: () -> Unit,
    onNewsClick: () -> Unit,
    onAskClick: () -> Unit,
    onSavedClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(horizontal = Dimens.SpaceL)
                .fillMaxWidth()
                .glassSurface(
                    shape = RoundedCornerShape(Dimens.RadiusPill),
                    fill = Color(0xFF0A1226).copy(alpha = 0.78f),
                    stroke = KanoonifyPremiumColors.GlassStrokeHi
                )
                .padding(horizontal = Dimens.SpaceS, vertical = 10.dp)
        ) {
            BarTab(homeLabel, "\uD83C\uDFE0", selected = selectedIndex == 0, onClick = onHomeClick)
            BarTab(newsLabel, "\uD83D\uDCF0", selected = selectedIndex == 1, onClick = onNewsClick)
            Spacer(Modifier.size(64.dp))
            BarTab(savedLabel, "\uD83D\uDCD1", selected = selectedIndex == 3, onClick = onSavedClick)
            BarTab(profileLabel, "\uD83D\uDC64", selected = selectedIndex == 4, onClick = onProfileClick)
        }

        CentreAskButton(label = askLabel, onClick = onAskClick)
    }
}

@Composable
private fun BarTab(
    label: String,
    glyph: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val interaction = remember { MutableInteractionSource() }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(Dimens.RadiusL))
            .clickable(
                interactionSource = interaction,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = glyph,
            fontSize = 18.sp,
            color = if (selected) KanoonifyPremiumColors.NeonBlue
            else KanoonifyPremiumColors.TextLow
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
            color = if (selected) KanoonifyPremiumColors.TextHi
            else KanoonifyPremiumColors.TextLow,
            letterSpacing = 0.3.sp
        )
    }
}

@Composable
private fun CentreAskButton(label: String, onClick: () -> Unit) {
    val interaction = remember { MutableInteractionSource() }
    val t = rememberInfiniteTransition(label = "askFab")
    val pulse by t.animateFloat(
        initialValue = 0.95f, targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "askFabPulse"
    )
    val haloAlpha by t.animateFloat(
        initialValue = 0.35f, targetValue = 0.75f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "askFabHalo"
    )

    Box(contentAlignment = Alignment.Center) {

        Box(
            modifier = Modifier
                .size(72.dp)
                .graphicsLayer {
                    scaleX = pulse; scaleY = pulse; alpha = haloAlpha
                }
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(
                            KanoonifyPremiumColors.NeonViolet.copy(alpha = 0.65f),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .size(60.dp)
                .neonGlow(
                    color = KanoonifyPremiumColors.NeonViolet,
                    shape = CircleShape,
                    radius = 18.dp,
                    alpha = 0.7f
                )
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        listOf(
                            KanoonifyPremiumColors.NeonBlue,
                            KanoonifyPremiumColors.NeonViolet
                        )
                    )
                )
                .clickable(
                    interactionSource = interaction,
                    indication = null,
                    onClick = onClick
                )
        ) {
            Text(text = "\u2728", color = Color.White, fontSize = 18.sp)
            Spacer(Modifier.height(2.dp))
            Text(
                text = label,
                color = Color.White,
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.4.sp
            )
        }
    }
}
