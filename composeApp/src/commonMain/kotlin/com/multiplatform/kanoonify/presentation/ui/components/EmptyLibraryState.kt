package com.multiplatform.kanoonify.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.multiplatform.kanoonify.presentation.theme.Dimens
import com.multiplatform.kanoonify.presentation.theme.KanoonifyPremiumColors
import com.multiplatform.kanoonify.presentation.ui.modifiers.glassSurface

@Composable
fun EmptyLibraryState(
    glyph: String,
    title: String,
    body: String,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(Dimens.RadiusXXL)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .glassSurface(
                shape = shape,
                fill = Color.White.copy(alpha = 0.04f),
                stroke = KanoonifyPremiumColors.GlassStroke
            )
            .padding(horizontal = Dimens.SpaceXL, vertical = Dimens.SpaceXXL)
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(
                            KanoonifyPremiumColors.NeonIndigo.copy(alpha = 0.45f),
                            Color.Transparent
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(glyph, fontSize = 30.sp)
        }
        Spacer(Modifier.height(Dimens.SpaceL))
        Text(
            text = title,
            color = KanoonifyPremiumColors.TextHi,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = body,
            color = KanoonifyPremiumColors.TextLow,
            fontSize = 13.sp,
            lineHeight = 18.sp,
            textAlign = TextAlign.Center
        )
        if (actionLabel != null && onAction != null) {
            Spacer(Modifier.height(Dimens.SpaceL))
            val interaction = remember { MutableInteractionSource() }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(Dimens.RadiusPill))
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
                        onClick = onAction
                    )
                    .padding(horizontal = Dimens.SpaceXL, vertical = 10.dp)
            ) {
                Text(
                    text = actionLabel,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.3.sp
                )
            }
        }
    }
}
