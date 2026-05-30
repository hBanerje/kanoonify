package com.multiplatform.kanoonify.presentation.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.multiplatform.kanoonify.presentation.theme.Dimens
import com.multiplatform.kanoonify.presentation.theme.KanoonifyPremiumColors
import com.multiplatform.kanoonify.presentation.ui.modifiers.glassSurface

/**
 * Recent-search row: translucent glass strip with clock glyph + timestamp.
 * Stateless. The caller supplies the text — no business logic in UI.
 */
@Composable
fun SearchHistoryCard(
    query: String,
    timestamp: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.98f else 1f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessMediumLow),
        label = "historyScale"
    )
    val shape = RoundedCornerShape(Dimens.RadiusL)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .glassSurface(shape = shape)
            .clickable(
                interactionSource = interaction,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = Dimens.SpaceL, vertical = Dimens.SpaceM)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(KanoonifyPremiumColors.NeonBlue.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "\uD83D\uDD52", fontSize = 14.sp)
        }
        Spacer(Modifier.width(Dimens.SpaceM))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = query,
                color = KanoonifyPremiumColors.TextHi,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 18.sp
            )
            Spacer(Modifier.size(2.dp))
            Text(
                text = timestamp,
                color = KanoonifyPremiumColors.TextMuted,
                fontSize = 11.sp,
                letterSpacing = 0.3.sp
            )
        }
        Spacer(Modifier.width(Dimens.SpaceM))
        Text(
            text = "\u203A",
            color = Color.White.copy(alpha = 0.55f),
            fontSize = 22.sp,
            fontWeight = FontWeight.Light
        )
    }
}

