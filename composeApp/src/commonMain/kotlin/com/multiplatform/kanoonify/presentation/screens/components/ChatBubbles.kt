package com.multiplatform.kanoonify.presentation.screens.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kanoonify.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChatBubble(message: ChatMessage) {

    // Fade-in animation for AI responses
    val isAiResponse = !message.isUser && message.text != "__typing__"
    var appeared by remember { mutableStateOf(!isAiResponse) }
    LaunchedEffect(Unit) {
        appeared = true
    }
    val alpha by animateFloatAsState(
        targetValue = if (appeared) 1f else 0f,
        animationSpec = tween(durationMillis = if (isAiResponse) 600 else 0)
    )
    val slideUp by animateFloatAsState(
        targetValue = if (appeared) 0f else 20f,
        animationSpec = tween(durationMillis = if (isAiResponse) 600 else 0)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .graphicsLayer {
                this.alpha = alpha
                translationY = slideUp
            },
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {

        Column(
            horizontalAlignment = if (message.isUser) Alignment.End else Alignment.Start
        ) {

            // AI Persona
            if (!message.isUser) {
                Text(
                    text = stringResource(Res.string.chat_bubble_ai_persona),
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

    // Each dot bounces with delay
    val bounceValues = List(dotCount) { index ->
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 1200
                    0f at 0
                    -12f at 200 // bounce up
                    0f at 400   // back down
                    0f at 1200  // hold
                },
                repeatMode = RepeatMode.Restart,
                initialStartOffset = StartOffset(index * 150)
            )
        )
    }

    val alphaValues = List(dotCount) { index ->
        infiniteTransition.animateFloat(
            initialValue = 0.4f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 1200
                    0.4f at 0
                    1f at 200
                    0.4f at 400
                    0.4f at 1200
                },
                repeatMode = RepeatMode.Restart,
                initialStartOffset = StartOffset(index * 150)
            )
        )
    }

    Row(
        modifier = Modifier.padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        bounceValues.forEachIndexed { index, bounce ->
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .graphicsLayer {
                        translationY = bounce.value * 3f // amplify bounce
                    }
                    .clip(CircleShape)
                    .background(Color.Gray.copy(alpha = alphaValues[index].value))
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
