package com.multiplatform.kanoonify.presentation.screens.viewmodel

import com.multiplatform.kanoonify.domain.model.Law

data class LawsState(
    val laws: List<Law> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
