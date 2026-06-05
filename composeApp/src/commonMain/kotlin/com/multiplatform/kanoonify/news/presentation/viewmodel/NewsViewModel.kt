package com.multiplatform.kanoonify.news.presentation.viewmodel

import com.multiplatform.kanoonify.news.data.repository.FeedResult
import com.multiplatform.kanoonify.news.data.repository.NewsRepository
import com.multiplatform.kanoonify.news.data.repository.SearchResult
import com.multiplatform.kanoonify.news.domain.model.NewsArticle
import com.multiplatform.kanoonify.news.domain.model.NewsCategory
import com.multiplatform.kanoonify.news.presentation.state.LoadPhase
import com.multiplatform.kanoonify.news.presentation.state.NewsDetailState
import com.multiplatform.kanoonify.news.presentation.state.NewsFeedState
import com.multiplatform.kanoonify.news.presentation.state.NewsSearchState
import com.multiplatform.kanoonify.news.presentation.state.NewsUiEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewsViewModel(private val repository: NewsRepository) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _feed = MutableStateFlow(NewsFeedState())
    val feed: StateFlow<NewsFeedState> = _feed

    private val _detail = MutableStateFlow(NewsDetailState())
    val detail: StateFlow<NewsDetailState> = _detail

    private val _search = MutableStateFlow(NewsSearchState())
    val search: StateFlow<NewsSearchState> = _search

    private val _events = MutableSharedFlow<NewsUiEvent>(
        extraBufferCapacity = 8,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<NewsUiEvent> = _events.asSharedFlow()

    private var feedJob: Job? = null
    private var searchJob: Job? = null

    init {

        scope.launch {
            repository.observeSavedIds().collect { ids ->
                _feed.update   { it.copy(savedIds = ids) }
                _search.update { it.copy(savedIds = ids) }
                _detail.update { d -> d.copy(isSaved = d.article?.id in ids) }
            }
        }
        scope.launch {
            repository.observeRecentSearches().collect { recents ->
                _search.update { it.copy(recent = recents) }
            }
        }
        loadFeed(NewsCategory.Latest, forceRefresh = false)
    }

    fun onCategorySelected(category: NewsCategory) {
        if (category == _feed.value.category && _feed.value.articles.isNotEmpty()) return
        loadFeed(category, forceRefresh = false)
    }

    fun onRefresh() {
        loadFeed(_feed.value.category, forceRefresh = true)
    }

    fun onRetry() {
        loadFeed(_feed.value.category, forceRefresh = true)
    }

    private fun loadFeed(category: NewsCategory, forceRefresh: Boolean) {
        feedJob?.cancel()
        feedJob = scope.launch {
            _feed.update {
                it.copy(
                    category = category,
                    phase = if (it.articles.isEmpty() || it.category != category) LoadPhase.Loading
                            else it.phase,
                    isRefreshing = !it.isEmpty && forceRefresh,
                    errorMessage = null
                )
            }
            val result = repository.loadFeed(category, forceRefresh)
            _feed.update {
                when (result) {
                    is FeedResult.Success -> it.copy(
                        articles = result.articles,
                        phase = if (result.articles.isEmpty()) LoadPhase.Empty else LoadPhase.Ready,
                        isRefreshing = false,
                        isOffline = result.isStale,
                        errorMessage = null
                    )
                }
            }
        }
    }

    fun onArticleClick(article: NewsArticle) {
        _detail.update { NewsDetailState(article = article, isLoading = false, isSaved = repository.isSaved(article.id)) }
        scope.launch { _events.emit(NewsUiEvent.OpenDetail(article.id)) }
    }

    fun loadDetail(articleId: String) {
        scope.launch {
            _detail.update { it.copy(isLoading = it.article?.id != articleId) }
            val article = repository.fetchArticle(articleId)
            _detail.update {
                it.copy(
                    article = article,
                    isLoading = false,
                    isSaved = article?.let { a -> repository.isSaved(a.id) } ?: false,
                    errorMessage = if (article == null) "Article unavailable" else null
                )
            }
        }
    }

    fun onToggleSaved(article: NewsArticle) {
        scope.launch {
            if (repository.isSaved(article.id)) {
                repository.removeSavedArticle(article.id)
                _events.emit(NewsUiEvent.Toast("Removed from Library"))
            } else {
                repository.saveArticle(article)
                _events.emit(NewsUiEvent.Toast("Saved to Library"))
            }
        }
    }

    fun onShare(article: NewsArticle) {
        val text = buildString {
            append(article.title)
            if (article.articleUrl.isNotBlank()) {
                append("\n\n")
                append(article.articleUrl)
            }
            append("\n\nShared via Kanoonify")
        }
        scope.launch { _events.emit(NewsUiEvent.Share(text = text, title = article.title)) }
    }

    fun onOpenOriginal(article: NewsArticle) {
        if (article.articleUrl.isBlank()) return
        scope.launch { _events.emit(NewsUiEvent.OpenExternal(article.articleUrl)) }
    }

    fun onSearchQueryChange(value: String) {
        _search.update { it.copy(query = value) }
        searchJob?.cancel()
        if (value.isBlank()) {
            _search.update { it.copy(results = emptyList(), phase = LoadPhase.Ready, isSearching = false) }
            return
        }

        searchJob = scope.launch {
            delay(300L)
            doSearch(value)
        }
    }

    fun onSearchSubmit() {
        val q = _search.value.query.trim()
        if (q.isBlank()) return
        repository.recordSearch(q)
        searchJob?.cancel()
        searchJob = scope.launch { doSearch(q) }
    }

    fun onRecentSearchClick(query: String) {
        _search.update { it.copy(query = query) }
        searchJob?.cancel()
        searchJob = scope.launch { doSearch(query) }
    }

    fun onDeleteRecentSearch(query: String) {
        repository.deleteRecentSearch(query)
    }

    fun onClearRecentSearches() {
        repository.clearRecentSearches()
    }

    private suspend fun doSearch(query: String) {
        _search.update { it.copy(isSearching = true, errorMessage = null) }
        val result = repository.search(query)
        _search.update {
            when (result) {
                is SearchResult.Success -> it.copy(
                    results = result.articles,
                    isSearching = false,
                    isOffline = result.fromCache && result.offlineReason != null,
                    phase = if (result.articles.isEmpty()) LoadPhase.Empty else LoadPhase.Ready,
                    errorMessage = null
                )
            }
        }
    }

    fun dispose() {
        scope.coroutineContext[Job]?.cancel()
    }
}
