package com.multiplatform.kanoonify.presentation.screens.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.multiplatform.kanoonify.presentation.screens.viewmodel.COIViewModel
import com.multiplatform.kanoonify.presentation.theme.Dimens
import com.multiplatform.kanoonify.presentation.ui.components.AppCard
import kanoonify.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun COIDetailScreen(
    articleId: Int,
    viewModel: COIViewModel
) {
    val state by viewModel.state.collectAsState()
    val article = remember(articleId, state.allArticles) { viewModel.getArticleById(articleId) }

    // Clear any stale explanation when entering / leaving this article.
    DisposableEffect(articleId) {
        if (state.explainingArticleId != null && state.explainingArticleId != articleId) {
            viewModel.clearExplanation()
        }
        onDispose { viewModel.clearExplanation() }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (article == null) {
            Text(
                text = stringResource(Res.string.coi_detail_not_found),
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            return@Box
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = Dimens.ScreenHorizontal)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(Dimens.SpaceL))

            Text(
                text = article.title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            if (article.subtitle.isNotBlank()) {
                Spacer(Modifier.height(Dimens.SpaceXS))
                Text(
                    text = article.subtitle,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(Modifier.height(Dimens.SpaceL))

            AppCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = article.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(Modifier.height(Dimens.SpaceL))

            val isExplainingThis =
                state.isExplaining && state.explainingArticleId == articleId
            val explanationForThis =
                state.explanationText?.takeIf { state.explainingArticleId == articleId }

            AppCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = { if (!isExplainingThis) viewModel.requestExplanation(article) }
            ) {
                if (isExplainingThis) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(Dimens.SpaceL),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = stringResource(Res.string.coi_detail_explain_with_ai),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            explanationForThis?.let {
                Spacer(Modifier.height(Dimens.SpaceM))
                AppCard(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Text(
                            text = stringResource(Res.string.coi_detail_ai_explanation),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(Dimens.SpaceXS))
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(Modifier.height(Dimens.SpaceXXL))
        }
    }
}
