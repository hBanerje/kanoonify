package com.multiplatform.kanoonify.presentation.screens.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kanoonify.composeapp.generated.resources.*

@Composable
fun KanoonifyLogo() {

    val infiniteTransition = rememberInfiniteTransition()

    val rotation by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(Res.drawable.kanoonify_logo),
            contentDescription = stringResource(Res.string.splash_logo_content_description),
            modifier = Modifier
                .size(140.dp)
                .rotate(rotation)
        )
    }
}
