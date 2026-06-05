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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.multiplatform.kanoonify.domain.model.Lawyer
import com.multiplatform.kanoonify.presentation.screens.viewmodel.LawyerListViewModel
import com.multiplatform.kanoonify.presentation.theme.Dimens
import com.multiplatform.kanoonify.presentation.ui.components.AnimatedEntrance
import com.multiplatform.kanoonify.presentation.ui.components.AppCard
import com.multiplatform.kanoonify.presentation.ui.components.MonogramIcon
import com.multiplatform.kanoonify.presentation.ui.components.SectionHeader
import kanoonify.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun LawyerListScreen(
    viewModel: LawyerListViewModel,
    onLawyerClick: (Lawyer) -> Unit
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
                title = stringResource(Res.string.lawyer_list_title),
                caption = stringResource(Res.string.lawyer_list_caption, state.lawyers.size)
            )

            Spacer(Modifier.height(Dimens.SpaceM))

            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = viewModel::updateSearchQuery,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(Res.string.lawyer_list_search_placeholder)) },
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
                items(state.filtered, key = { it.id }) { lawyer ->
                    AnimatedEntrance {
                        LawyerRow(lawyer = lawyer, onClick = { onLawyerClick(lawyer) })
                    }
                }
            }
        }
    }
}

@Composable
private fun LawyerRow(lawyer: Lawyer, onClick: () -> Unit) {
    AppCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            MonogramIcon(
                text = lawyer.name
                    .removePrefix("Adv.")
                    .trim()
                    .split(" ")
                    .mapNotNull { it.firstOrNull()?.toString() }
                    .take(2)
                    .joinToString(""),
                background = monogramColor(lawyer.id),
                size = 48.dp
            )
            Spacer(Modifier.width(Dimens.SpaceL))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = lawyer.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    if (lawyer.isOnline) {
                        OnlineBadge()
                    }
                }
                Spacer(Modifier.height(Dimens.SpaceXS))
                Text(
                    text = lawyer.specialization,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(Dimens.SpaceXS))
                Text(
                    text = stringResource(
                        Res.string.lawyer_list_row_summary,
                        lawyer.experienceYears,
                        lawyer.rating.toString(),
                        lawyer.location
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(Dimens.SpaceXS))
                Text(
                    text = stringResource(Res.string.lawyer_list_row_fee, lawyer.feePerSession),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun OnlineBadge() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(Color(0xFF2E7D32))
        )
        Spacer(Modifier.width(Dimens.SpaceXS))
        Text(
            text = stringResource(Res.string.lawyer_status_online),
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF2E7D32)
        )
    }
}

private fun String.toColorSeed(): Int {
    var h = 0
    for (c in this) h = 31 * h + c.code
    return h
}

private fun monogramColor(id: String): Color {
    val palette = listOf(
        Color(0xFF1E88E5),
        Color(0xFF6A1B9A),
        Color(0xFF00897B),
        Color(0xFFD81B60),
        Color(0xFFEF6C00),
        Color(0xFF5E35B1),
        Color(0xFF43A047),
        Color(0xFFC62828)
    )
    val idx = (id.toColorSeed().let { if (it < 0) -it else it }) % palette.size
    return palette[idx]
}
