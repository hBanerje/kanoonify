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
 * High-attention emergency rights banner.
 *
 *  - Red→orange diagonal gradient with strong glow
 *  - Pulsing heartbeat ring around the "🚨" glyph
 *  - "Open" pill action on the right
 */
@Composable
fun EmergencyBanner(
    title: String,
    actionLabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val pressScale by animateFloatAsState(
        targetValue = if (pressed) 0.98f else 1f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessMediumLow),
        label = "emergencyPress"
    )

    val t = rememberInfiniteTransition(label = "emergency")
    val pulse by t.animateFloat(
        initialValue = 0.9f, targetValue = 1.18f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "emergencyPulse"
    )
    val pulseAlpha by t.animateFloat(
        initialValue = 0.5f, targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "emergencyPulseAlpha"
    )

    val shape = RoundedCornerShape(Dimens.RadiusXXL)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer { scaleX = pressScale; scaleY = pressScale }
            .neonGlow(color = KanoonifyPremiumColors.AlertRed, shape = shape, radius = 22.dp, alpha = 0.55f)
            .clip(shape)
            .background(
                Brush.linearGradient(
                    listOf(
                        KanoonifyPremiumColors.AlertRed,
                        KanoonifyPremiumColors.AlertOrange
                    )
                )
            )
            .clickable(
                interactionSource = interaction,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = Dimens.SpaceL, vertical = Dimens.SpaceL)
    ) {
        // Pulsing heartbeat glyph
        Box(contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .graphicsLayer { scaleX = pulse; scaleY = pulse; alpha = pulseAlpha }
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.45f))
            )
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "\uD83D\uDEA8", fontSize = 18.sp)
            }
        }

        Spacer(Modifier.width(Dimens.SpaceL))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(2.dp))
            HeartbeatWaveform()
        }

        Spacer(Modifier.width(Dimens.SpaceM))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(Dimens.RadiusPill))
                .background(Color.White)
                .padding(horizontal = Dimens.SpaceL, vertical = 8.dp)
        ) {
            Text(
                text = actionLabel,
                color = KanoonifyPremiumColors.AlertRed,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.4.sp
            )
        }
    }
}

/** Subtle scrolling heartbeat-style sparkline rendered with bars. */
@Composable
private fun HeartbeatWaveform() {
    val t = rememberInfiniteTransition(label = "waveform")
    val shift by t.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "waveformShift"
    )
    val heights = listOf(4, 6, 14, 10, 4, 18, 6, 4, 8, 12, 4, 6)
    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier
            .height(18.dp)
            .graphicsLayer { translationX = (1f - shift) * -20f; alpha = 0.65f }
    ) {
        heights.forEach { h ->
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(h.dp)
                    .background(Color.White.copy(alpha = 0.85f))
            )
        }
    }
}

