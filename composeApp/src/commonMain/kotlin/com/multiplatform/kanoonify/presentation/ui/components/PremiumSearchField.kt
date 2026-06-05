package com.multiplatform.kanoonify.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.multiplatform.kanoonify.presentation.theme.Dimens
import com.multiplatform.kanoonify.presentation.theme.KanoonifyPremiumColors
import com.multiplatform.kanoonify.presentation.ui.modifiers.glassSurface
import com.multiplatform.kanoonify.presentation.ui.modifiers.neonGlow

@Composable
fun PremiumSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String,
    onSubmit: () -> Unit,
    clearLabel: String,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(Dimens.RadiusXXL)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .neonGlow(KanoonifyPremiumColors.NeonBlue, shape, radius = 18.dp, alpha = 0.45f)
            .glassSurface(
                shape = shape,
                fill = Color.White.copy(alpha = 0.06f),
                stroke = KanoonifyPremiumColors.GlassStrokeHi
            )
            .padding(horizontal = Dimens.SpaceM, vertical = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        listOf(KanoonifyPremiumColors.NeonBlue, KanoonifyPremiumColors.NeonViolet)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("\uD83D\uDD0D", color = Color.White, fontSize = 16.sp)
        }
        Spacer(Modifier.width(Dimens.SpaceM))
        Box(modifier = Modifier.weight(1f)) {
            if (query.isEmpty()) {
                Text(
                    text = placeholder,
                    color = KanoonifyPremiumColors.TextLow,
                    fontSize = 15.sp
                )
            }
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                cursorBrush = SolidColor(KanoonifyPremiumColors.NeonBlue),
                textStyle = TextStyle(
                    color = KanoonifyPremiumColors.TextHi,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSubmit() }),
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (query.isNotEmpty()) {
            Spacer(Modifier.width(Dimens.SpaceS))
            val interaction = remember { MutableInteractionSource() }
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.08f))
                    .clickable(
                        interactionSource = interaction,
                        indication = null,
                        onClick = { onQueryChange("") }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("\u2715", color = KanoonifyPremiumColors.TextMid, fontSize = 12.sp)
            }

            Text(
                text = clearLabel,
                color = Color.Transparent,
                fontSize = 1.sp,
                modifier = Modifier.size(1.dp)
            )
        }
    }
}
