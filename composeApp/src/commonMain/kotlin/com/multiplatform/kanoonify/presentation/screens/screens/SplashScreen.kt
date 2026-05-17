package com.multiplatform.kanoonify.presentation.screens.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.multiplatform.kanoonify.presentation.screens.components.KanoonifyLogo
import com.multiplatform.kanoonify.presentation.screens.navigation.LandingRoute
import com.multiplatform.kanoonify.presentation.screens.navigation.SplashRoute
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {

    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate(LandingRoute) {
            popUpTo<SplashRoute> { inclusive = true }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        KanoonifyLogo()
    }
}