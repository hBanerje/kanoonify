package com.multiplatform.kanoonify.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.multiplatform.kanoonify.presentation.theme.Dimens

@Composable
fun MonogramIcon(
    text: String,
    background: Color,
    foreground: Color = Color.White,
    modifier: Modifier = Modifier,
    size: Dp = Dimens.CategoryIconSize,
    cornerRadius: Dp = Dimens.RadiusM
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(cornerRadius))
            .background(background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text.take(2).uppercase(),
            color = foreground,
            style = MaterialTheme.typography.titleSmall
        )
    }
}
