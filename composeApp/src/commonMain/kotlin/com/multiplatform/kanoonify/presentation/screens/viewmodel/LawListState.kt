package com.multiplatform.kanoonify.presentation.screens.viewmodel

import com.multiplatform.kanoonify.domain.model.LawItem
import com.multiplatform.kanoonify.domain.model.LawTag

data class LawListState(
    val laws: List<LawItem> = emptyList(),
    val title: String = "",
    val isLoading: Boolean = false
)

