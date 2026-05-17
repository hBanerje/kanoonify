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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.multiplatform.kanoonify.data.LawListProvider
import com.multiplatform.kanoonify.domain.model.LawItem
import com.multiplatform.kanoonify.presentation.theme.Dimens
import com.multiplatform.kanoonify.presentation.ui.components.AnimatedEntrance
import com.multiplatform.kanoonify.presentation.ui.components.AppCard
import com.multiplatform.kanoonify.presentation.ui.components.AskAiFab
import com.multiplatform.kanoonify.presentation.ui.components.CardSectionTitle
import com.multiplatform.kanoonify.presentation.ui.components.TagChip
import com.multiplatform.kanoonify.presentation.ui.components.deriveLawTag

@Composable
fun LawDetailScreen(
    lawId: Int,
    onAskAiClick: () -> Unit = {}
) {
    val law = remember(lawId) { LawListProvider.getLawById(lawId) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (law == null) {
            EmptyDetail()
        } else {
            DetailContent(law)
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
private fun DetailContent(law: LawItem) {
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
        // Title block
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
                        TagChip(tag = deriveLawTag(law.punishment))
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
                    title = "DESCRIPTION",
                    body  = law.description.ifBlank { "No description available." }
                )
            }
        }

        item {
            AnimatedEntrance(delayMillis = 160) {
                DetailSectionCard(
                    title = "PUNISHMENT",
                    body  = law.punishment.ifBlank { "Not specified." }
                )
            }
        }

        item {
            AnimatedEntrance(delayMillis = 240) {
                DetailSectionCard(
                    title = "WHAT YOU SHOULD DO",
                    body  = deriveUserAction(law)
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
            text  = "Law not found.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/** Deterministic helpful action derived from punishment / keywords. */
private fun deriveUserAction(law: LawItem): String {
    val tag = deriveLawTag(law.punishment)
    return when (tag) {
        com.multiplatform.kanoonify.presentation.ui.components.LawTag.JAIL ->
            "1. Stay calm. Do not resist or self-incriminate.\n" +
            "2. Ask the officer for ID and reason for action.\n" +
            "3. Contact a lawyer or legal aid cell immediately.\n" +
            "4. Request bail where the offence is bailable."
        com.multiplatform.kanoonify.presentation.ui.components.LawTag.FINE ->
            "1. Request a written challan with section reference.\n" +
            "2. Pay only via official e-Challan portals.\n" +
            "3. Keep the receipt safely.\n" +
            "4. Contest in court within the allowed window if unjust."
        com.multiplatform.kanoonify.presentation.ui.components.LawTag.RIGHT ->
            "1. Politely assert your right and ask for written grounds.\n" +
            "2. Document the incident (time, place, witnesses).\n" +
            "3. File an FIR or written complaint if denied.\n" +
            "4. Reach out to free legal aid services for support."
    }
}

