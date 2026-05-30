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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
 * Generic settings row. Supports either:
 *  - navigation row (trailing chevron) when [toggleState] is null
 *  - toggle row (trailing Switch)      when [toggleState] is non-null
 *
 * Stateless. Caller owns toggle value via [toggleState] + [onToggle].
 */
@Composable
fun SettingsRow(
    glyph: String,
    title: String,
    subtitle: String,
    accent: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    toggleState: Boolean? = null,
    onToggle: ((Boolean) -> Unit)? = null,
    trailing: String? = null
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.985f else 1f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessMediumLow),
        label = "settingsRowScale"
    )
    val shape = RoundedCornerShape(Dimens.RadiusL)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .glassSurface(
                shape = shape,
                fill = Color.White.copy(alpha = 0.04f),
                stroke = KanoonifyPremiumColors.GlassStroke
            )
            .clickable(
                interactionSource = interaction,
                indication = null,
                onClick = {
                    if (toggleState != null && onToggle != null) onToggle(!toggleState)
                    else onClick()
                }
            )
            .padding(horizontal = Dimens.SpaceL, vertical = Dimens.SpaceM)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(Dimens.RadiusM))
                .background(accent.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center
        ) {
            Text(glyph, fontSize = 16.sp, color = accent)
        }
        Spacer(Modifier.width(Dimens.SpaceM))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = KanoonifyPremiumColors.TextHi,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.size(2.dp))
            Text(
                text = subtitle,
                color = KanoonifyPremiumColors.TextLow,
                fontSize = 11.sp,
                lineHeight = 14.sp
            )
        }
        Spacer(Modifier.width(Dimens.SpaceS))
        when {
            toggleState != null && onToggle != null -> {
                Switch(
                    checked = toggleState,
                    onCheckedChange = onToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = accent,
                        checkedBorderColor = Color.Transparent,
                        uncheckedThumbColor = KanoonifyPremiumColors.TextMid,
                        uncheckedTrackColor = Color.White.copy(alpha = 0.10f),
                        uncheckedBorderColor = KanoonifyPremiumColors.GlassStrokeHi
                    )
                )
            }
            trailing != null -> {
                Text(
                    text = trailing,
                    color = KanoonifyPremiumColors.TextMid,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.width(Dimens.SpaceS))
                Text(text = "\u203A", color = KanoonifyPremiumColors.TextLow, fontSize = 18.sp)
            }
            else -> {
                Text(text = "\u203A", color = KanoonifyPremiumColors.TextLow, fontSize = 20.sp)
            }
        }
    }
}

