package com.multiplatform.kanoonify.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.remember
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

@Composable
fun ProfileHeader(
    name: String,
    memberSince: String,
    membershipLabel: String,
    isPremium: Boolean,
    editLabel: String,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(Dimens.RadiusHero)
    val accent = if (isPremium) KanoonifyPremiumColors.GoldMid else KanoonifyPremiumColors.NeonBlue

    Column(
        modifier = modifier
            .fillMaxWidth()
            .neonGlow(accent, shape, radius = 18.dp, alpha = 0.4f)
            .glassSurface(
                shape = shape,
                fill = Color.White.copy(alpha = 0.06f),
                stroke = accent.copy(alpha = 0.35f)
            )
            .padding(Dimens.SpaceL)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Box(
                modifier = Modifier
                    .size(72.dp)
                    .neonGlow(accent, CircleShape, radius = 12.dp, alpha = 0.55f)
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
                    text = name.take(1).uppercase(),
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.width(Dimens.SpaceL))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    color = KanoonifyPremiumColors.TextHi,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.2).sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = memberSince,
                    color = KanoonifyPremiumColors.TextLow,
                    fontSize = 12.sp
                )
                Spacer(Modifier.height(Dimens.SpaceS))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(Dimens.RadiusPill))
                            .background(accent.copy(alpha = 0.18f))
                            .border(
                                width = 1.dp,
                                color = accent.copy(alpha = 0.45f),
                                shape = RoundedCornerShape(Dimens.RadiusPill)
                            )
                            .padding(horizontal = Dimens.SpaceM, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (isPremium) "\u2728" else "\u25CB",
                                color = accent,
                                fontSize = 11.sp
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = membershipLabel,
                                color = accent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.4.sp
                            )
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(Dimens.SpaceL))
        val interaction = remember { MutableInteractionSource() }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Dimens.RadiusPill))
                .background(Color.White.copy(alpha = 0.06f))
                .border(
                    width = 1.dp,
                    color = KanoonifyPremiumColors.GlassStrokeHi,
                    shape = RoundedCornerShape(Dimens.RadiusPill)
                )
                .clickable(
                    interactionSource = interaction,
                    indication = null,
                    onClick = onEditClick
                )
                .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = editLabel,
                color = KanoonifyPremiumColors.TextHi,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.3.sp
            )
        }
    }
}
