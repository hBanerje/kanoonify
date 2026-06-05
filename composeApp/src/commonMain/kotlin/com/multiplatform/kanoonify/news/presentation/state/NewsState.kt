package com.multiplatform.kanoonify.news.presentation.state

import com.multiplatform.kanoonify.news.domain.model.NewsArticle
import com.multiplatform.kanoonify.news.domain.model.NewsCategory

/** Top-level news feed UI state. */
data class NewsFeedState(
    val articles: List<NewsArticle> = emptyList(),
    val category: NewsCategory = NewsCategory.Latest,
    val savedIds: Set<String> = emptySet(),
    val phase: LoadPhase = LoadPhase.Loading,
    val isRefreshing: Boolean = false,
    val isOffline: Boolean = false,
    val errorMessage: String? = null
) {
    val isEmpty: Boolean get() = articles.isEmpty()

    fun isSaved(id: String): Boolean = id in savedIds
}

/** UI phase of any list-loading screen. */
enum class LoadPhase { Loading, Ready, Error, Empty }

/** Detail screen UI state. */
data class NewsDetailState(
    val article: NewsArticle? = null,
    val isLoading: Boolean = true,
    val isSaved: Boolean = false,
    val errorMessage: String? = null
)

/** Search UI state. */
data class NewsSearchState(
    val query: String = "",
    val results: List<NewsArticle> = emptyList(),
    val recent: List<String> = emptyList(),
    val savedIds: Set<String> = emptySet(),
    val isSearching: Boolean = false,
    val isOffline: Boolean = false,
    val phase: LoadPhase = LoadPhase.Ready,
    val errorMessage: String? = null
) {
    val hasQuery: Boolean get() = query.trim().isNotEmpty()
    fun isSaved(id: String): Boolean = id in savedIds
}

/** One-shot side effects emitted from the ViewModel. */
sealed interface NewsUiEvent {
    data class OpenDetail(val articleId: String) : NewsUiEvent
    data class OpenExternal(val url: String) : NewsUiEvent
    data class Share(val text: String, val title: String) : NewsUiEvent
    data class Toast(val message: String) : NewsUiEvent
}

