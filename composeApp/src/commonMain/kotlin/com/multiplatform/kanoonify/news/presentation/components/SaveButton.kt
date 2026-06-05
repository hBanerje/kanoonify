package com.multiplatform.kanoonify.news.presentation.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.multiplatform.kanoonify.presentation.theme.KanoonifyPremiumColors
import com.multiplatform.kanoonify.presentation.ui.modifiers.neonGlow

@Composable
fun SaveButton(
    saved: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    diameter: Dp = 44.dp,
    accent: Color = KanoonifyPremiumColors.GoldMid
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.88f else if (saved) 1.05f else 1f,
        animationSpec = spring(dampingRatio = 0.45f, stiffness = Spring.StiffnessMedium),
        label = "saveButtonScale"
    )

    val bg = if (saved) accent.copy(alpha = 0.22f) else Color.White.copy(alpha = 0.10f)
    val glyph = if (saved) "\u2605" else "\u2606"

    Box(
        modifier = modifier
            .size(diameter)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .neonGlow(
                color = if (saved) accent else Color.Transparent,
                shape = CircleShape,
                radius = if (saved) 14.dp else 0.dp,
                alpha = 0.6f
            )
            .clip(CircleShape)
            .background(bg)
            .clickable(interactionSource = interaction, indication = null, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = glyph,
            color = if (saved) accent else Color.White,
            fontSize = 18.sp
        )
    }
}
