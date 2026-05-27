package com.multiplatform.kanoonify.presentation.screens.viewmodel

import com.multiplatform.kanoonify.domain.model.Article

data class COIState(
    val allArticles: List<Article> = emptyList(),
    val searchQuery: String = "",
    val filteredArticles: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val isExplaining: Boolean = false,
    val explanationText: String? = null,
    val explainingArticleId: Int? = null
)
