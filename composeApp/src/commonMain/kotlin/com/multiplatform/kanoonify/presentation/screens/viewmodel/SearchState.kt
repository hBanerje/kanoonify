package com.multiplatform.kanoonify.presentation.screens.viewmodel

/**
 * Future-ready taxonomy for searchable entities. Allows the same VM/State to
 * back Laws, Constitution Articles, Lawyers, News and AI History without
 * structural changes.
 */
enum class SearchEntityType { Law, ConstitutionArticle, Lawyer, News, AiHistory, All }

/** A single past query saved in history. */
data class RecentSearchItem(
    val id: String,
    val query: String,
    val timestamp: String,
    val type: SearchEntityType = SearchEntityType.All
)

/** A unified search result — populated from any data source in the future. */
data class SearchResultItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val glyph: String,
    val type: SearchEntityType
)

/** UI state for the Search hub. */
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

/** One-shot UI events emitted by the ViewModel (navigation, snackbar, etc.). */
sealed interface SearchUiEvent {
    data class NavigateToAsk(val seedQuery: String) : SearchUiEvent
    data object NavigateToLaws : SearchUiEvent
    data object NavigateToCoi : SearchUiEvent
    data object NavigateToLawyers : SearchUiEvent
    data object NavigateToEmergency : SearchUiEvent
    data class OpenResult(val item: SearchResultItem) : SearchUiEvent
}

