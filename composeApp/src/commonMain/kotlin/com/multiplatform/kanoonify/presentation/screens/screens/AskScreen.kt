package com.multiplatform.kanoonify.presentation.screens.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.multiplatform.kanoonify.presentation.screens.components.ChatBubble
import com.multiplatform.kanoonify.presentation.screens.components.ChatMessage
import com.multiplatform.kanoonify.presentation.screens.viewmodel.AskViewModel

@Composable
fun AskScreen() {

    val viewModel = remember { AskViewModel() }
    val state by viewModel.state.collectAsState()

    val listState = rememberLazyListState()

    // auto-scroll when new messages arrive or loading state changes
    LaunchedEffect(state.messages.size, state.isLoading) {
        val totalItems =
            state.messages.size + if (state.isLoading) 1 else 0

        if (totalItems > 0) {
            listState.animateScrollToItem(totalItems - 1)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        contentWindowInsets = WindowInsets(0)
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {

            // CHAT LIST
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                itemsIndexed(state.messages) { _, message ->
                    ChatBubble(message)
                }

                if (state.isLoading) {
                    item {
                        ChatBubble(
                            ChatMessage("__typing__", false)
                        )
                    }
                }
            }

            // INPUT AREA
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                OutlinedTextField(
                    value = state.query,
                    onValueChange = { viewModel.onQueryChange(it) },
                    placeholder = { Text("Ask about your situation...") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { viewModel.onSubmit() },
                    enabled = !state.isLoading
                ) {
                    Text("Send")
                }
            }
        }
    }
}