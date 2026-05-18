package com.multiplatform.kanoonify.presentation.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import kotlinx.coroutines.delay

/**
 * Reveals text character-by-character to mimic a typing effect.
 */
@Composable
fun TypingText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    color: Color = Color.Unspecified,
    charDelayMillis: Long = 12L
) {
    var displayed by remember(text) { mutableStateOf("") }

    LaunchedEffect(text) {
        displayed = ""
        text.forEachIndexed { index, _ ->
            displayed = text.take(index + 1)
            delay(charDelayMillis)
        }
    }

    Text(
        text = displayed,
        modifier = modifier,
        style = style,
        color = color
    )
}

