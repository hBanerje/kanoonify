package com.multiplatform.kanoonify.presentation.screens.viewmodel

import com.multiplatform.kanoonify.presentation.screens.components.ChatMessage

data class AskState(
    val query: String = "",
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false
)