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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.multiplatform.kanoonify.domain.model.ChatAuthor
import com.multiplatform.kanoonify.domain.model.ChatMessage
import com.multiplatform.kanoonify.domain.model.Lawyer
import com.multiplatform.kanoonify.presentation.screens.viewmodel.LawyerChatViewModel
import com.multiplatform.kanoonify.presentation.theme.Dimens
import com.multiplatform.kanoonify.presentation.ui.components.AnimatedEntrance
import com.multiplatform.kanoonify.presentation.ui.components.MonogramIcon

@Composable
fun LawyerChatScreen(viewModel: LawyerChatViewModel) {
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(state.messages.size, state.isLawyerTyping) {
        val count = state.messages.size + if (state.isLawyerTyping) 1 else 0
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
            ChatHeader(lawyer = state.lawyer)

            HorizontalDivider(
                thickness = Dimens.DividerThickness,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(
                    horizontal = Dimens.ScreenHorizontal,
                    vertical = Dimens.SpaceM
                ),
                verticalArrangement = Arrangement.spacedBy(Dimens.SpaceS)
            ) {
                items(state.messages, key = { it.id }) { message ->
                    AnimatedEntrance {
                        MessageBubble(message)
                    }
                }
                if (state.isLawyerTyping) {
                    item {
                        AnimatedEntrance { TypingBubble() }
                    }
                }
            }

            ChatInputBar(
                value = state.draft,
                onValueChange = viewModel::onDraftChange,
                onSend = viewModel::onSend
            )
        }
    }
}

@Composable
private fun ChatHeader(lawyer: Lawyer?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = Dimens.ScreenHorizontal,
                vertical = Dimens.SpaceL
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (lawyer != null) {
            MonogramIcon(
                text = lawyer.name
                    .removePrefix("Adv.")
                    .trim()
                    .split(" ")
                    .mapNotNull { it.firstOrNull()?.toString() }
                    .take(2)
                    .joinToString(""),
                background = MaterialTheme.colorScheme.primary,
                size = 44.dp
            )
            Spacer(Modifier.width(Dimens.SpaceM))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = lawyer.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(Dimens.SpaceXS))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(
                                if (lawyer.isOnline) Color(0xFF2E7D32)
                                else MaterialTheme.colorScheme.outline
                            )
                    )
                    Spacer(Modifier.width(Dimens.SpaceXS))
                    Text(
                        text = if (lawyer.isOnline) "Online · ${lawyer.specialization}"
                        else "Offline · ${lawyer.specialization}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            Text(
                text = "Lawyer",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun MessageBubble(message: ChatMessage) {
    val isUser = message.author == ChatAuthor.User
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Column(
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start,
            modifier = Modifier.fillMaxWidth(0.82f)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = if (isUser) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(
                            topStart = Dimens.RadiusL,
                            topEnd = Dimens.RadiusL,
                            bottomStart = if (isUser) Dimens.RadiusL else Dimens.RadiusS,
                            bottomEnd = if (isUser) Dimens.RadiusS else Dimens.RadiusL
                        )
                    )
                    .padding(
                        horizontal = Dimens.SpaceL,
                        vertical = Dimens.SpaceM
                    )
            ) {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isUser) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.height(Dimens.SpaceXS))
            Text(
                text = message.timestamp,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun TypingBubble() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(Dimens.RadiusL)
                )
                .padding(horizontal = Dimens.SpaceL, vertical = Dimens.SpaceM)
        ) {
            Text(
                text = "typing…",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ChatInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = Dimens.ScreenHorizontal,
                vertical = Dimens.SpaceM
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    "Type a message…",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(Dimens.RadiusL),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )
        Spacer(Modifier.width(Dimens.SpaceS))
        Button(
            onClick = onSend,
            enabled = value.isNotBlank(),
            shape = RoundedCornerShape(Dimens.RadiusL),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = "Send",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

