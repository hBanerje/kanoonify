package com.multiplatform.kanoonify.presentation.screens.viewmodel

import com.multiplatform.kanoonify.data.COIDataProvider
import com.multiplatform.kanoonify.domain.model.Article
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class COIViewModel(
    private val dataProvider: COIDataProvider = COIDataProvider,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
) {

    private val _state = MutableStateFlow(COIState())
    val state: StateFlow<COIState> = _state.asStateFlow()

    private var explainJob: Job? = null

    init {
        loadArticles()
    }

    fun loadArticles() {
        _state.update { it.copy(isLoading = true) }
        val articles = try {
            dataProvider.allArticles()
        } catch (t: Throwable) {
            emptyList()
        }
        _state.update {
            it.copy(
                allArticles = articles,
                filteredArticles = applyFilter(articles, it.searchQuery),
                isLoading = false
            )
        }
    }

    fun updateSearchQuery(query: String) {
        _state.update {
            it.copy(
                searchQuery = query,
                filteredArticles = applyFilter(it.allArticles, query)
            )
        }
    }

    fun getArticleById(id: Int): Article? =
        _state.value.allArticles.firstOrNull { it.id == id }
            ?: dataProvider.getArticleById(id)

    fun requestExplanation(article: Article) {
        explainJob?.cancel()
        _state.update {
            it.copy(
                isExplaining = true,
                explanationText = null,
                explainingArticleId = article.id
            )
        }
        explainJob = scope.launch {
            val result = runCatching { explainArticle(article) }
                .getOrElse { "Sorry, the AI explanation is unavailable right now." }
            _state.update {

                if (it.explainingArticleId == article.id) {
                    it.copy(isExplaining = false, explanationText = result)
                } else it
            }
        }
    }

    fun clearExplanation() {
        explainJob?.cancel()
        _state.update {
            it.copy(isExplaining = false, explanationText = null, explainingArticleId = null)
        }
    }

    private suspend fun explainArticle(article: Article): String {
        delay(800L)
        return "This article means that: ${article.subtitle.ifBlank { article.title }} — " +
            "in simple terms, ${article.description.take(160)}..."
    }

    private fun applyFilter(source: List<Article>, query: String): List<Article> {
        val q = query.trim()
        if (q.isEmpty()) return source
        return source.filter { article ->
            article.title.contains(q, ignoreCase = true) ||
                article.subtitle.contains(q, ignoreCase = true) ||
                article.description.contains(q, ignoreCase = true) ||
                article.id.toString() == q
        }
    }
}
