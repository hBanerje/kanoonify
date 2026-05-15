package com.multiplatform.kanoonify.presentation.screens.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChatBubble(message: ChatMessage) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {

        Column(
            horizontalAlignment = if (message.isUser) Alignment.End else Alignment.Start
        ) {

            // AI Persona
            if (!message.isUser) {
                Text(
                    text = "Kanoonify Assistant",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
                )
            }

            Box(
                modifier = Modifier
                    .background(
                        color = if (message.isUser)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(12.dp)
                    .widthIn(max = 280.dp)
            ) {
                if (message.text == "__typing__") {
                    TypingIndicator()
                } else {
                    if (message.isUser) {
                        Text(
                            text = message.text,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        FormattedText(message.text)
                    }
                }
            }
        }
    }
}

@Composable
fun TypingIndicator() {
    val dotCount = 3
    val infiniteTransition = rememberInfiniteTransition()

    val alphaValues = List(dotCount) { index ->
        infiniteTransition.animateFloat(
            initialValue = 0.2f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(600, delayMillis = index * 200),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Row(
        modifier = Modifier.padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        alphaValues.forEach { alpha ->
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(Color.Gray.copy(alpha = alpha.value))
            )
        }
    }
}

@Composable
fun FormattedText(text: String) {
    val parts = text.split("\n")

    Column {
        parts.forEach { line ->
            if (line.startsWith("**") && line.contains(":**")) {
                val title = line.substringAfter("**").substringBefore(":**")
                val content = line.substringAfter(":**").trim()

                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )

                Text(
                    text = content,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            } else {
                Text(text = line)
            }
        }
    }
}