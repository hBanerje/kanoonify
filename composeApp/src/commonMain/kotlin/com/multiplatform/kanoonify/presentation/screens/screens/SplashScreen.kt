package com.multiplatform.kanoonify.presentation.screens.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.multiplatform.kanoonify.presentation.screens.navigation.LandingRoute
import com.multiplatform.kanoonify.presentation.screens.navigation.SplashRoute
import com.multiplatform.kanoonify.presentation.screens.viewmodel.SplashViewModel
import com.multiplatform.kanoonify.presentation.theme.Dimens
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kanoonify.composeapp.generated.resources.*

/* ---------- Private design tokens for the splash (intentionally dark) ---------- */

private val SplashTopColor       = Color(0xFF071A2B)
private val SplashBottomColor    = Color(0xFF0B3C5D) // brand primary
private val SplashAccentColor    = Color(0xFFF2C94C) // brand accent
private val SplashProgressTrack  = Color(0x33FFFFFF)
private val SplashCaptionColor   = Color(0xCCFFFFFF)

private const val ScaleInDuration  = 700
private const val ScaleSettleDuration = 500
private const val ScreenFadeDuration  = 500
private const val ProgressFadeDuration = 600
private const val ExitDuration         = 320

private const val ScaleStart   = 0.8f
private const val ScalePeak    = 1.1f
private const val ScaleSettle  = 1.0f
private const val ScaleExit    = 0.92f

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = remember { SplashViewModel() }
) {
    val state by viewModel.state.collectAsState()

    val logoScale = remember { Animatable(ScaleStart) }
    val screenAlpha = remember { Animatable(0f) }
    val progressAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        logoScale.animateTo(
            targetValue = ScalePeak,
            animationSpec = tween(ScaleInDuration, easing = FastOutSlowInEasing)
        )
        logoScale.animateTo(
            targetValue = ScaleSettle,
            animationSpec = tween(ScaleSettleDuration, easing = FastOutSlowInEasing)
        )
    }

    // Full-screen fade-in
    LaunchedEffect(Unit) {
        screenAlpha.animateTo(1f, tween(ScreenFadeDuration, easing = FastOutSlowInEasing))
    }

    // Progress fades in slightly after the logo starts
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(300)
        progressAlpha.animateTo(1f, tween(ProgressFadeDuration, easing = FastOutSlowInEasing))
    }

    // (logo stays visible during the navigation transition fade).
    LaunchedEffect(state.navigateToLanding) {
        if (state.navigateToLanding) {
            logoScale.animateTo(
                targetValue = ScaleExit,
                animationSpec = tween(ExitDuration, easing = FastOutSlowInEasing)
            )
            viewModel.onNavigationHandled()
            navController.navigate(LandingRoute) {
                popUpTo<SplashRoute> { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(SplashTopColor, SplashBottomColor)
                )
            )
            .graphicsLayer { alpha = screenAlpha.value },
        contentAlignment = Alignment.Center
    ) {

        // Centered logo with scale sequence
        Image(
            painter = painterResource(Res.drawable.kanoonify_logo),
            contentDescription = stringResource(Res.string.splash_logo_content_description),
            modifier = Modifier
                .size(140.dp)
                .graphicsLayer {
                    scaleX = logoScale.value
                    scaleY = logoScale.value
                }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(
                    start  = Dimens.SpaceXL,
                    end    = Dimens.SpaceXL,
                    bottom = Dimens.SpaceXXL
                )
                .graphicsLayer { alpha = progressAlpha.value },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text  = stringResource(Res.string.splash_tagline),
                color = SplashCaptionColor,
                style = MaterialTheme.typography.bodyMedium
            )

            androidx.compose.foundation.layout.Spacer(Modifier.height(Dimens.SpaceM))

            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(Dimens.RadiusPill)),
                color      = SplashAccentColor,
                trackColor = SplashProgressTrack
            )
        }
    }
}
