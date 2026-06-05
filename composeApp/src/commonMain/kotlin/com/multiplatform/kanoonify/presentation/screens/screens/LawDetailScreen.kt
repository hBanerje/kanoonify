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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.multiplatform.kanoonify.domain.model.LawItem
import com.multiplatform.kanoonify.presentation.screens.viewmodel.LawDetailViewModel
import com.multiplatform.kanoonify.presentation.theme.Dimens
import com.multiplatform.kanoonify.presentation.ui.components.AnimatedEntrance
import com.multiplatform.kanoonify.presentation.ui.components.AppCard
import com.multiplatform.kanoonify.presentation.ui.components.AskAiFab
import com.multiplatform.kanoonify.presentation.ui.components.CardSectionTitle
import com.multiplatform.kanoonify.presentation.ui.components.TagChip
import kanoonify.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun LawDetailScreen(
    viewModel: LawDetailViewModel,
    onAskAiClick: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val law = state.law
        if (law == null) {
            EmptyDetail()
        } else {
            DetailContent(law, state.userAction, state.tag)
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
private fun DetailContent(
    law: LawItem,
    userAction: String,
    tag: com.multiplatform.kanoonify.domain.model.LawTag
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
            AnimatedEntrance {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text  = law.title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(Modifier.height(Dimens.SpaceS))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TagChip(tag = tag)
                        if (law.category.isNotBlank()) {
                            Spacer(Modifier.width(Dimens.SpaceS))
                            Text(
                                text  = law.category,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(Dimens.SpaceS))
        }

        item {
            AnimatedEntrance(delayMillis = 80) {
                DetailSectionCard(
                    title = stringResource(Res.string.law_detail_section_description),
                    body  = law.description.ifBlank { stringResource(Res.string.law_detail_no_description) }
                )
            }
        }

        item {
            AnimatedEntrance(delayMillis = 160) {
                DetailSectionCard(
                    title = stringResource(Res.string.law_detail_section_punishment),
                    body  = law.punishment.ifBlank { stringResource(Res.string.law_detail_no_punishment) }
                )
            }
        }

        item {
            AnimatedEntrance(delayMillis = 240) {
                DetailSectionCard(
                    title = stringResource(Res.string.law_detail_section_what_to_do),
                    body  = userAction
                )
            }
        }
    }
}

@Composable
private fun DetailSectionCard(title: String, body: String) {
    AppCard(modifier = Modifier.fillMaxWidth()) {
        Column {
            CardSectionTitle(title = title)
            Spacer(Modifier.height(Dimens.SpaceS))
            Text(
                text  = body,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun EmptyDetail() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(Dimens.ScreenHorizontal),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text  = stringResource(Res.string.law_detail_not_found),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
