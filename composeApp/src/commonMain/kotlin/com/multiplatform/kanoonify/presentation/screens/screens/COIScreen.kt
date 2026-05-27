package com.multiplatform.kanoonify.presentation.screens.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.multiplatform.kanoonify.domain.model.Article
import com.multiplatform.kanoonify.presentation.screens.viewmodel.COIViewModel
import com.multiplatform.kanoonify.presentation.theme.Dimens
import com.multiplatform.kanoonify.presentation.ui.components.AnimatedEntrance
import com.multiplatform.kanoonify.presentation.ui.components.AppCard
import com.multiplatform.kanoonify.presentation.ui.components.SectionHeader

@Composable
fun COIScreen(
    viewModel: COIViewModel,
    onArticleClick: (Article) -> Unit
) {
    val state by viewModel.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = Dimens.ScreenHorizontal)
        ) {
            Spacer(Modifier.height(Dimens.SpaceL))

            SectionHeader(
                title = "Constitution of India",
                caption = "${state.allArticles.size} articles"
            )

            Spacer(Modifier.height(Dimens.SpaceM))

            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = viewModel::updateSearchQuery,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search articles…") },
                shape = RoundedCornerShape(Dimens.RadiusL),
                singleLine = true
            )

            Spacer(Modifier.height(Dimens.SpaceM))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(Dimens.SpaceM),
                contentPadding = PaddingValues(
                    top = Dimens.SpaceS,
                    bottom = Dimens.SpaceXXL
                )
            ) {
                items(state.filteredArticles, key = { it.id }) { article ->
                    // PERF NOTE: AnimatedEntrance runs on every item composition.
                    // For the current ~17 articles this is fine, but for larger
                    // datasets (e.g. >100) consider:
                    //   - animating only first-page items (track via index),
                    //   - or replacing per-item AnimatedEntrance with a lighter
                    //     LazyColumn-level fade-in on initial layout.
                    AnimatedEntrance {
                        ArticleRow(article = article, onClick = { onArticleClick(article) })
                    }
                }
            }
        }
    }
}

@Composable
private fun ArticleRow(article: Article, onClick: () -> Unit) {
    AppCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column {
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (article.subtitle.isNotBlank()) {
                Spacer(Modifier.height(Dimens.SpaceXS))
                Text(
                    text = article.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
