package com.multiplatform.kanoonify.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.multiplatform.kanoonify.presentation.theme.Dimens
import com.multiplatform.kanoonify.presentation.theme.KanoonifyPremiumColors
import com.multiplatform.kanoonify.presentation.ui.modifiers.glassSurface
import com.multiplatform.kanoonify.presentation.ui.modifiers.neonGlow

/** Single statistic tile — big value + label + tinted glyph. */
@Composable
fun StatsCard(
    label: String,
    value: String,
    glyph: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(Dimens.RadiusXL)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .neonGlow(accent, shape, radius = 10.dp, alpha = 0.30f)
            .glassSurface(
                shape = shape,
                fill = Color.White.copy(alpha = 0.05f),
                stroke = accent.copy(alpha = 0.25f)
            )
            .padding(Dimens.SpaceL)
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(Dimens.RadiusM))
                .background(
                    Brush.linearGradient(
                        listOf(accent.copy(alpha = 0.85f), accent.copy(alpha = 0.45f))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(glyph, color = Color.White, fontSize = 14.sp)
        }
        Spacer(Modifier.height(Dimens.SpaceM))
        Text(
            text = value,
            color = KanoonifyPremiumColors.TextHi,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.5).sp
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = label,
            color = KanoonifyPremiumColors.TextLow,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.4.sp
        )
    }
}

