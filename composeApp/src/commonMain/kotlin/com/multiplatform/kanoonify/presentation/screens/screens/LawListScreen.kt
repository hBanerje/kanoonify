package com.multiplatform.kanoonify.presentation.screens.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.multiplatform.kanoonify.data.LawListProvider
import com.multiplatform.kanoonify.domain.model.LawItem
import com.multiplatform.kanoonify.domain.model.SubCategory
import kotlinx.coroutines.delay

@Composable
fun LawListScreen(subCategory: SubCategory) {

    val laws = remember(subCategory) {
        LawListProvider.getLawsBySubCategory(subCategory)
    }

    val visibleStates = remember(laws.size) { List(laws.size) { mutableStateOf(false) } }
    LaunchedEffect(laws) {
        laws.indices.forEach { index ->
            delay(index * 60L)
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
            text = subCategory.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = "${laws.size} law${if (laws.size != 1) "s" else ""} found",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (laws.isEmpty()) {
            Text(
                text = "No laws found for this subcategory.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 32.dp)
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                itemsIndexed(laws) { index, law ->
                    val isVisible = if (index < visibleStates.size) visibleStates[index].value else true

                    val alpha by animateFloatAsState(
                        targetValue = if (isVisible) 1f else 0f,
                        animationSpec = tween(500, easing = FastOutSlowInEasing)
                    )
                    val slideY by animateFloatAsState(
                        targetValue = if (isVisible) 0f else 40f,
                        animationSpec = tween(500, easing = FastOutSlowInEasing)
                    )

                    LawCard(
                        law = law,
                        modifier = Modifier.graphicsLayer {
                            this.alpha = alpha
                            translationY = slideY
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun LawCard(
    law: LawItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = law.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = law.description,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Punishment: ${law.punishment}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}

