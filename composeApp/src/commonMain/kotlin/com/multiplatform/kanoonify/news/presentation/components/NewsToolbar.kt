package com.multiplatform.kanoonify.news.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.multiplatform.kanoonify.presentation.theme.Dimens
import com.multiplatform.kanoonify.presentation.theme.KanoonifyPremiumColors
import com.multiplatform.kanoonify.presentation.ui.modifiers.glassSurface

@Composable
fun NewsToolbar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    showBack: Boolean = false,
    onBack: () -> Unit = {},
    isOffline: Boolean = false,
    offlineLabel: String = "Offline",
    trailing: (@Composable () -> Unit)? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .glassSurface(
                shape = RoundedCornerShape(Dimens.RadiusXXL),
                fill = Color.White.copy(alpha = 0.06f),
                stroke = KanoonifyPremiumColors.GlassStrokeHi
            )
            .padding(horizontal = Dimens.SpaceM, vertical = 10.dp)
    ) {
        if (showBack) {
            val interaction = remember { MutableInteractionSource() }
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.08f))
                    .clickable(interactionSource = interaction, indication = null, onClick = onBack),
                contentAlignment = Alignment.Center
            ) {
                Text("\u2039", color = KanoonifyPremiumColors.TextHi, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(Dimens.SpaceM))
        }
        Box(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    color = KanoonifyPremiumColors.TextHi,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = (-0.2).sp
                )
                if (isOffline) {
                    Spacer(Modifier.width(Dimens.SpaceS))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(Dimens.RadiusPill))
                            .background(KanoonifyPremiumColors.AlertOrange.copy(alpha = 0.20f))
                            .padding(horizontal = Dimens.SpaceM, vertical = 3.dp)
                    ) {
                        Text(
                            text = offlineLabel,
                            color = KanoonifyPremiumColors.AlertOrange,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.6.sp
                        )
                    }
                }
            }
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    color = KanoonifyPremiumColors.TextLow,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
        if (trailing != null) {
            Spacer(Modifier.width(Dimens.SpaceS))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) { trailing() }
        }
    }
}
