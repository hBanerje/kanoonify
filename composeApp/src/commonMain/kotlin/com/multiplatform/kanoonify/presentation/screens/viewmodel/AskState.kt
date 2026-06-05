package com.multiplatform.kanoonify.presentation.screens.viewmodel

import com.multiplatform.kanoonify.domain.model.AskAnswer

sealed class AskTurn {
    data class User(val text: String) : AskTurn()
    data class Assistant(val answer: AskAnswer) : AskTurn()
}

data class AskState(
    val query: String = "",
    val turns: List<AskTurn> = emptyList(),
    val isLoading: Boolean = false
)
