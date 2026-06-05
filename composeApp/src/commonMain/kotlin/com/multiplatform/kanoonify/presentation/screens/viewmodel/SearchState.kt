package com.multiplatform.kanoonify.presentation.screens.viewmodel

enum class SearchEntityType { Law, ConstitutionArticle, Lawyer, News, AiHistory, All }

data class RecentSearchItem(
    val id: String,
    val query: String,
    val timestamp: String,
    val type: SearchEntityType = SearchEntityType.All
)

data class SearchResultItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val glyph: String,
    val type: SearchEntityType
)

data class SearchState(
    val query: String = "",
    val isSearching: Boolean = false,
    val recent: List<RecentSearchItem> = emptyList(),
    val results: List<SearchResultItem> = emptyList(),
    val activeFilter: SearchEntityType = SearchEntityType.All,
    val error: String? = null
) {
    val hasQuery: Boolean get() = query.isNotBlank()
    val showResults: Boolean get() = hasQuery
    val recentIsEmpty: Boolean get() = recent.isEmpty()
}

sealed interface SearchUiEvent {
    data class NavigateToAsk(val seedQuery: String) : SearchUiEvent
    data object NavigateToLaws : SearchUiEvent
    data object NavigateToCoi : SearchUiEvent
    data object NavigateToLawyers : SearchUiEvent
    data object NavigateToEmergency : SearchUiEvent
    data class OpenResult(val item: SearchResultItem) : SearchUiEvent
}
