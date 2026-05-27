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
import kanoonify.composeapp.generated.resources.*
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

private data class CategoryItem(
    val titleRes: StringResource,
    val subtitleRes: StringResource,
    val initials: String,
    val accent: Color
)

private val categoryItems = listOf(
    CategoryItem(Res.string.category_traffic_rules_title, Res.string.category_traffic_rules_subtitle, "TR", Color(0xFF2962FF)),
    CategoryItem(Res.string.category_police_rights_title, Res.string.category_police_rights_subtitle, "PR", Color(0xFF00897B)),
    CategoryItem(Res.string.category_women_safety_title,  Res.string.category_women_safety_subtitle,  "WS", Color(0xFFAD1457)),
    CategoryItem(Res.string.category_public_safety_title, Res.string.category_public_safety_subtitle, "PS", Color(0xFFE65100))
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
                    title   = stringResource(Res.string.category_screen_title),
                    caption = stringResource(Res.string.category_screen_caption)
                )
                Spacer(Modifier.height(Dimens.SpaceM))
            }
            items(categoryItems) { item ->
                val title = stringResource(item.titleRes)
                AnimatedEntrance {
                    CategoryCard(item = item, title = title, onClick = { onCategoryClick(title) })
                }
            }
        }
    }
}

@Composable
private fun CategoryCard(item: CategoryItem, title: String, onClick: () -> Unit) {
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
                    text  = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(Dimens.SpaceXS))
                Text(
                    text  = stringResource(item.subtitleRes),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.width(Dimens.SpaceS))
            Text(
                text  = stringResource(Res.string.common_chevron_right),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
