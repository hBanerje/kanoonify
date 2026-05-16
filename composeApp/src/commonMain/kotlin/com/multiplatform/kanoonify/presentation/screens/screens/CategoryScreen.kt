package com.multiplatform.kanoonify.presentation.screens.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.sin

private data class CategoryItem(
    val title: String,
    val subtitle: String,
    val gradient: Brush,
    val accentColor: Color
)

private val categoryItems = listOf(
    CategoryItem(
        title = "Traffic Rules",
        subtitle = "Driving, challans, speed limits",
        gradient = Brush.linearGradient(
            colors = listOf(Color(0xFF1441CC), Color(0xFF2962FF), Color(0xFF00B0FF))
        ),
        accentColor = Color(0xFF1441CC)
    ),
    CategoryItem(
        title = "Police Rights",
        subtitle = "Arrest, search, FIR, bail",
        gradient = Brush.linearGradient(
            colors = listOf(Color(0xFF1D7373), Color(0xFF00897B), Color(0xFF4DB6AC))
        ),
        accentColor = Color(0xFF1D7373)
    ),
    CategoryItem(
        title = "Women Safety",
        subtitle = "Harassment, stalking, DV",
        gradient = Brush.linearGradient(
            colors = listOf(Color(0xFFAD1457), Color(0xFFF06292), Color(0xFFF48FB1))
        ),
        accentColor = Color(0xFFAD1457)
    ),
    CategoryItem(
        title = "Public Safety",
        subtitle = "Noise, nuisance, consumer rights",
        gradient = Brush.linearGradient(
            colors = listOf(Color(0xFFE65100), Color(0xFFF4511E), Color(0xFFFF8A65))
        ),
        accentColor = Color(0xFFE65100)
    )
)

@Composable
fun CategoryScreen() {

    // Staggered entry triggers
    val visibleStates = remember { List(categoryItems.size) { mutableStateOf(false) } }

    LaunchedEffect(Unit) {
        categoryItems.indices.forEach { index ->
            delay(index * 120L)
            visibleStates[index].value = true
        }
    }

    // Continuous floating animation for depth
    val infiniteTransition = rememberInfiniteTransition()
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 24.dp)
            .statusBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Header with entrance animation
        val headerVisible = visibleStates.firstOrNull()?.value ?: false
        val headerAlpha by animateFloatAsState(
            targetValue = if (headerVisible) 1f else 0f,
            animationSpec = tween(500, easing = FastOutSlowInEasing)
        )
        val headerSlide by animateFloatAsState(
            targetValue = if (headerVisible) 0f else -40f,
            animationSpec = tween(500, easing = FastOutSlowInEasing)
        )

        Text(
            text = "Browse by Category",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .graphicsLayer {
                    alpha = headerAlpha
                    translationY = headerSlide
                }
        )

        // Category cards with staggered animation
        categoryItems.forEachIndexed { index, item ->
            val isVisible = visibleStates[index].value

            // Entry animations
            val entryAlpha by animateFloatAsState(
                targetValue = if (isVisible) 1f else 0f,
                animationSpec = tween(
                    durationMillis = 600,
                    easing = FastOutSlowInEasing
                )
            )
            val entryTranslationX by animateFloatAsState(
                targetValue = if (isVisible) 0f else 300f,
                animationSpec = spring(
                    dampingRatio = 0.7f,
                    stiffness = Spring.StiffnessLow
                )
            )
            val entryScale by animateFloatAsState(
                targetValue = if (isVisible) 1f else 0.6f,
                animationSpec = spring(
                    dampingRatio = 0.65f,
                    stiffness = Spring.StiffnessLow
                )
            )
            val entryRotation by animateFloatAsState(
                targetValue = if (isVisible) 0f else 15f,
                animationSpec = spring(
                    dampingRatio = 0.6f,
                    stiffness = Spring.StiffnessMediumLow
                )
            )

            // Subtle continuous floating per card (phase offset per index)
            val cardFloat = sin((floatOffset + index * 0.25f) * 2 * PI).toFloat()

            CategoryCard(
                item = item,
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        alpha = entryAlpha
                        translationX = entryTranslationX
                        scaleX = entryScale
                        scaleY = entryScale
                        rotationZ = entryRotation
                        // Subtle idle float
                        translationY = cardFloat * 3f
                    }
            )
        }
    }
}

@Composable
private fun CategoryCard(
    item: CategoryItem,
    modifier: Modifier = Modifier
) {
    // Press animation
    var isPressed by remember { mutableStateOf(false) }
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = 0.5f,
            stiffness = Spring.StiffnessMedium
        )
    )

    // Shine sweep animation
    val infiniteTransition = rememberInfiniteTransition()
    val shineSweep by infiniteTransition.animateFloat(
        initialValue = -0.5f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        modifier = modifier
            .height(100.dp)
            .graphicsLayer {
                scaleX = pressScale
                scaleY = pressScale
            }
            .clip(RoundedCornerShape(20.dp))
            .background(item.gradient)
            .drawBehind {
                // Animated shine overlay
                val shineWidth = size.width * 0.3f
                val shineX = shineSweep * (size.width + shineWidth) - shineWidth
                drawRoundRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.White.copy(alpha = 0.15f),
                            Color.Transparent
                        ),
                        startX = shineX,
                        endX = shineX + shineWidth
                    ),
                    cornerRadius = CornerRadius(20.dp.toPx())
                )
            }
            .clickable {
                isPressed = !isPressed
            }
            .padding(horizontal = 24.dp, vertical = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Column {
            Text(
                text = item.title,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.subtitle,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
        }
    }
}