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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.multiplatform.kanoonify.presentation.theme.Dimens

@Composable
fun PremiumFeatureCard(
    title: String,
    subtitle: String,
    glyph: String,
    accent: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        animationSpec = spring(
            dampingRatio = 0.55f,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "premiumFeatureScale"
    )

    val shape = RoundedCornerShape(Dimens.RadiusL)
    val surface = MaterialTheme.colorScheme.surface
    val surfaceGradient = Brush.linearGradient(
        colors = listOf(
            accent.copy(alpha = 0.16f),
            surface,
            accent.copy(alpha = 0.06f)
        )
    )
    val iconGradient = Brush.linearGradient(
        colors = listOf(
            accent.copy(alpha = 0.85f),
            accent.copy(alpha = 0.55f)
        )
    )

    Box(
        modifier = modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }

            .shadow(
                elevation = 10.dp,
                shape = shape,
                ambientColor = accent.copy(alpha = 0.5f),
                spotColor = accent.copy(alpha = 0.5f)
            )
            .clip(shape)
            .background(surfaceGradient)
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.55f),
                shape = shape
            )
            .clickable(
                interactionSource = interaction,
                indication = null,
                onClick = onClick
            )
            .padding(Dimens.SpaceL)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape(Dimens.RadiusM),
                            ambientColor = accent,
                            spotColor = accent
                        )
                        .clip(RoundedCornerShape(Dimens.RadiusM))
                        .background(iconGradient),
                    contentAlignment = Alignment.Center
                ) {

                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .offsetTopStart()
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.35f))
                    )
                    Text(
                        text = glyph,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }

                Text(
                    text = "\u2197",
                    style = MaterialTheme.typography.labelLarge,
                    color = accent.copy(alpha = 0.85f)
                )
            }
            Spacer(Modifier.height(Dimens.SpaceM))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(Dimens.SpaceXS))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun Modifier.offsetTopStart(): Modifier =
    this.then(Modifier.graphicsLayer { translationX = -10f; translationY = -10f })
