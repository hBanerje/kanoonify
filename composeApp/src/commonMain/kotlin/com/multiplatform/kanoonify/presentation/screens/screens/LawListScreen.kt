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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.multiplatform.kanoonify.domain.model.LawItem
import com.multiplatform.kanoonify.domain.model.deriveLawTag
import com.multiplatform.kanoonify.presentation.screens.viewmodel.LawListViewModel
import com.multiplatform.kanoonify.presentation.theme.Dimens
import com.multiplatform.kanoonify.presentation.ui.components.AnimatedEntrance
import com.multiplatform.kanoonify.presentation.ui.components.AppCard
import com.multiplatform.kanoonify.presentation.ui.components.AskAiFab
import com.multiplatform.kanoonify.presentation.ui.components.SectionHeader
import com.multiplatform.kanoonify.presentation.ui.components.TagChip

@Composable
fun LawListScreen(
    viewModel: LawListViewModel,
    onLawClick: (LawItem) -> Unit = {},
    onAskAiClick: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

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
                bottom = Dimens.SpaceXXL + Dimens.FabSize
            )
        ) {
            item {
                SectionHeader(
                    title   = state.title,
                    caption = "${state.laws.size} law${if (state.laws.size != 1) "s" else ""} found"
                )
                Spacer(Modifier.height(Dimens.SpaceM))
            }

            if (state.laws.isEmpty()) {
                item {
                    AppCard(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text  = "No laws found for this topic.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(state.laws, key = { it.id }) { law ->
                    AnimatedEntrance {
                        LawRowCard(law, onClick = { onLawClick(law) })
                    }
                }
            }
        }

        AskAiFab(
            onClick = onAskAiClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(Dimens.SpaceXL)
        )
    }
}

@Composable
private fun LawRowCard(law: LawItem, onClick: () -> Unit) {
    AppCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text  = law.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(Dimens.SpaceXS))
                Text(
                    text  = law.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
                Spacer(Modifier.height(Dimens.SpaceS))
                TagChip(tag = deriveLawTag(law.punishment))
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
