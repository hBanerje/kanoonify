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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.multiplatform.kanoonify.presentation.screens.viewmodel.AskTurn
import com.multiplatform.kanoonify.presentation.screens.viewmodel.AskViewModel
import com.multiplatform.kanoonify.presentation.theme.Dimens
import com.multiplatform.kanoonify.presentation.ui.components.AiAnswerCard
import com.multiplatform.kanoonify.presentation.ui.components.AnimatedEntrance
import com.multiplatform.kanoonify.presentation.ui.components.AppCard
import com.multiplatform.kanoonify.presentation.ui.components.SectionHeader

@Composable
fun AskScreen() {
    val viewModel = remember { AskViewModel() }
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(state.turns.size, state.isLoading) {
        val count = state.turns.size + if (state.isLoading) 1 else 0
        if (count > 0) listState.animateScrollToItem(count - 1)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Header
            Column(modifier = Modifier.padding(horizontal = Dimens.ScreenHorizontal, vertical = Dimens.SpaceL)) {
                SectionHeader(
                    title   = "Ask Kanoonify",
                    caption = "Describe your situation. Get rights, applicable law and next steps."
                )
            }

            // Conversation
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(
                    horizontal = Dimens.ScreenHorizontal,
                    vertical   = Dimens.SpaceS
                ),
                verticalArrangement = Arrangement.spacedBy(Dimens.SpaceM)
            ) {
                items(state.turns.size) { index ->
                    when (val turn = state.turns[index]) {
                        is AskTurn.User      -> AnimatedEntrance { UserBubble(turn.text) }
                        is AskTurn.Assistant -> AnimatedEntrance { AiAnswerCard(turn.answer) }
                    }
                }
                if (state.isLoading) {
                    item { AnimatedEntrance { ThinkingCard() } }
                }
            }

            // Input bar
            InputBar(
                value = state.query,
                onValueChange = viewModel::onQueryChange,
                onSubmit = viewModel::onSubmit,
                enabled = !state.isLoading
            )
        }
    }
}

@Composable
private fun UserBubble(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(Dimens.RadiusL)
                )
                .padding(horizontal = Dimens.SpaceL, vertical = Dimens.SpaceM)
        ) {
            Text(
                text  = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun ThinkingCard() {
    AppCard(modifier = Modifier.fillMaxWidth()) {
        Text(
            text  = "Thinking…",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun InputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSubmit: () -> Unit,
    enabled: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = Dimens.ScreenHorizontal,
                vertical   = Dimens.SpaceM
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    "Describe your situation…",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            modifier = Modifier.weight(1f),
            singleLine = true,
            shape = RoundedCornerShape(Dimens.RadiusL),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )
        Spacer(Modifier.width(Dimens.SpaceS))
        Button(
            onClick = onSubmit,
            enabled = enabled && value.isNotBlank(),
            shape = RoundedCornerShape(Dimens.RadiusL),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor   = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text  = "Send",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}