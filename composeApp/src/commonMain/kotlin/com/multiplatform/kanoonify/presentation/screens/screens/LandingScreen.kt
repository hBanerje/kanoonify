package com.multiplatform.kanoonify.presentation.screens.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.multiplatform.kanoonify.presentation.screens.components.KanoonifyLogo
import com.multiplatform.kanoonify.presentation.screens.navigation.AskRoute
import com.multiplatform.kanoonify.presentation.screens.navigation.CategoriesRoute

@Composable
fun LandingScreen(navController: NavController) {

    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 2f,
        animationSpec = tween(600)
    )

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(600)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .graphicsLayer {
                this.alpha = alpha
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        KanoonifyLogo()

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Know your rights instantly",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = { navController.navigate(AskRoute) }) {
            Text("Ask a Question")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = { navController.navigate(CategoriesRoute) }) {
            Text("Browse Laws")
        }
    }
}