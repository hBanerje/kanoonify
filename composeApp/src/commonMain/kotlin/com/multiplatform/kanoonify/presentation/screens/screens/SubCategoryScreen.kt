package com.multiplatform.kanoonify.presentation.screens.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.multiplatform.kanoonify.data.CategoryDataProvider
import com.multiplatform.kanoonify.domain.model.SubCategory
import kotlinx.coroutines.delay

@Composable
fun SubCategoryScreen(
    category: String,
    onSubCategoryClick: (SubCategory) -> Unit
) {
    val subcategories = remember(category) {
        CategoryDataProvider.getSubcategories(category)
    }

    // Staggered entry
    val visibleStates = remember { List(subcategories.size) { mutableStateOf(false) } }
    LaunchedEffect(Unit) {
        subcategories.indices.forEach { index ->
            delay(index * 80L)
            visibleStates[index].value = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(
            text = category,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            itemsIndexed(subcategories) { index, subCategory ->
                val isVisible = if (index < visibleStates.size) visibleStates[index].value else true

                val alpha by animateFloatAsState(
                    targetValue = if (isVisible) 1f else 0f,
                    animationSpec = tween(400, easing = FastOutSlowInEasing)
                )
                val slideX by animateFloatAsState(
                    targetValue = if (isVisible) 0f else 200f,
                    animationSpec = spring(
                        dampingRatio = 0.75f,
                        stiffness = Spring.StiffnessLow
                    )
                )

                SubCategoryCard(
                    subCategory = subCategory,
                    onClick = { onSubCategoryClick(subCategory) },
                    modifier = Modifier.graphicsLayer {
                        this.alpha = alpha
                        translationX = slideX
                    }
                )
            }
        }
    }
}

@Composable
private fun SubCategoryCard(
    subCategory: SubCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 18.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = subCategory.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "→",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

