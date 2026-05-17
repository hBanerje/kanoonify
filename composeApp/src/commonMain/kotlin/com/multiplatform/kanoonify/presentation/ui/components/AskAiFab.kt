package com.multiplatform.kanoonify.presentation.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import com.multiplatform.kanoonify.presentation.theme.Dimens

/**
 * Floating "Ask AI" action button with a subtle pulse ring.
 * Tap → navigates / expands into the Ask screen.
 */
@Composable
fun AskAiFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infinite = rememberInfiniteTransition()
    val pulseScale by infinite.animateFloat(
        initialValue = 1f,
        targetValue = 1.35f,
        animationSpec = infiniteRepeatable(animation = tween(1600), repeatMode = RepeatMode.Restart)
    )
    val pulseAlpha by infinite.animateFloat(
        initialValue = 0.35f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(animation = tween(1600), repeatMode = RepeatMode.Restart)
    )

    Box(modifier = modifier, contentAlignment = Alignment.Center) {

        // Pulse ring
        Box(
            modifier = Modifier
                .size(Dimens.FabSize)
                .graphicsLayer {
                    scaleX = pulseScale
                    scaleY = pulseScale
                    alpha = pulseAlpha
                }
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        )

        FloatingActionButton(
            onClick = onClick,
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = Dimens.ElevationFab
            ),
            modifier = Modifier.size(Dimens.FabSize)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "AI",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

