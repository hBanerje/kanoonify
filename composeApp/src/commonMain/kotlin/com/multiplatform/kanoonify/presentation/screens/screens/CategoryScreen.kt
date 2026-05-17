package com.multiplatform.kanoonify.presentation.screens.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.multiplatform.kanoonify.presentation.theme.Dimens
import com.multiplatform.kanoonify.presentation.ui.components.AnimatedEntrance
import com.multiplatform.kanoonify.presentation.ui.components.AppCard
import com.multiplatform.kanoonify.presentation.ui.components.MonogramIcon
import com.multiplatform.kanoonify.presentation.ui.components.SectionHeader

private data class CategoryItem(
    val title: String,
    val subtitle: String,
    val initials: String,
    val accent: Color
)

private val categoryItems = listOf(
    CategoryItem("Traffic Rules",  "Driving, challans & road safety", "TR", Color(0xFF2962FF)),
    CategoryItem("Police Rights",  "Arrest, search, FIR & bail",      "PR", Color(0xFF00897B)),
    CategoryItem("Women Safety",   "Harassment, stalking & DV",       "WS", Color(0xFFAD1457)),
    CategoryItem("Public Safety",  "Noise, nuisance & consumer law",  "PS", Color(0xFFE65100))
)

@Composable
fun CategoryScreen(onCategoryClick: (String) -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = Dimens.ScreenHorizontal),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpaceM),
            contentPadding = PaddingValues(
                top    = Dimens.SpaceXL,
                bottom = Dimens.SpaceXXL
            )
        ) {
            item {
                SectionHeader(
                    title   = "Browse by Category",
                    caption = "Explore laws by everyday situations"
                )
                Spacer(Modifier.height(Dimens.SpaceM))
            }
            items(categoryItems) { item ->
                AnimatedEntrance {
                    CategoryCard(item = item, onClick = { onCategoryClick(item.title) })
                }
            }
        }
    }
}

@Composable
private fun CategoryCard(item: CategoryItem, onClick: () -> Unit) {
    AppCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        cornerRadius = Dimens.RadiusL
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            MonogramIcon(text = item.initials, background = item.accent)
            Spacer(Modifier.width(Dimens.SpaceL))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text  = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(Dimens.SpaceXS))
                Text(
                    text  = item.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.width(Dimens.SpaceS))
            Text(
                text  = "›",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
