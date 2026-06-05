package com.multiplatform.kanoonify.presentation.screens.viewmodel

import com.multiplatform.kanoonify.news.data.repository.NewsRepository
import com.multiplatform.kanoonify.news.domain.model.NewsArticle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Drives the personal Library screen.
 *
 *  - In-memory seed for the always-present sections (Laws, COI, AI chats,
 *    Lawyer notes) — these will move to persistence later.
 *  - **Live news bookmarks** are merged in via [newsRepository] so anything
 *    the user saves from the News module appears here automatically.
 *
 * The contract `addItem` / `removeItem` / `clearSection` lets a future
 * `SavedRepository` (Room / SQLDelight) slot in without UI changes.
 */
class SavedViewModel(
    private val newsRepository: NewsRepository? = null
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    // Seeded once; the News section is recomputed on every newsRepository emit.
    private val baseItems: List<SavedItem> = seed()

    private val _state = MutableStateFlow(SavedState(items = baseItems))
    val state: StateFlow<SavedState> = _state

    private var newsJob: Job? = null

    init {
        if (newsRepository != null) {
            newsJob = scope.launch {
                newsRepository.observeSaved().collect { savedNews ->
                    val newsItems = savedNews.map { it.toSavedItem() }
                    _state.update { current ->
                        // Replace any prior news-derived rows; keep the rest.
                        val nonNews = current.items.filterNot { it.id.startsWith(NEWS_PREFIX) }
                        current.copy(items = nonNews + newsItems)
                    }
                }
            }
        }
    }

    /* ------------------------------ intents -------------------------------- */

    fun onFilterChange(filter: SavedFilter) {
        _state.update { it.copy(filter = filter) }
    }

    fun onRemoveItem(id: String) {
        if (id.startsWith(NEWS_PREFIX)) {
            newsRepository?.removeSavedArticle(id.removePrefix(NEWS_PREFIX))
            // The Flow collector above will reconcile UI state.
            return
        }
        _state.update { s -> s.copy(items = s.items.filterNot { it.id == id }) }
    }

    fun onClearSection(type: SavedItemType) {
        if (type == SavedItemType.News) {
            newsRepository?.clearAllSaved()
            return
        }
        _state.update { s -> s.copy(items = s.items.filterNot { it.type == type }) }
    }

    fun onClearAll() {
        newsRepository?.clearAllSaved()
        _state.update { it.copy(items = emptyList()) }
    }

    /** Public for future repository writes / bookmarking. */
    fun addItem(item: SavedItem) {
        _state.update { it.copy(items = listOf(item) + it.items) }
    }

    fun dispose() {
        newsJob?.cancel()
        scope.coroutineContext[Job]?.cancel()
    }

    /* ------------------------------ seed ----------------------------------- */

    private fun seed(): List<SavedItem> = listOf(
        SavedItem(
            id = "law-1",
            type = SavedItemType.Law,
            title = "IPC Section 379 — Theft",
            subtitle = "Punishment up to 3 years & fine",
            savedAt = "Yesterday"
        ),
        SavedItem(
            id = "law-2",
            type = SavedItemType.Law,
            title = "MV Act §185 — Drunk driving",
            subtitle = "₹10,000 fine and/or 6 months jail",
            savedAt = "2 days ago"
        ),
        SavedItem(
            id = "coi-1",
            type = SavedItemType.ConstitutionArticle,
            title = "Article 21 — Right to Life",
            subtitle = "Protection of life and personal liberty",
            savedAt = "Last week"
        ),
        SavedItem(
            id = "coi-2",
            type = SavedItemType.ConstitutionArticle,
            title = "Article 19 — Freedom of Speech",
            subtitle = "Six fundamental freedoms",
            savedAt = "Last week"
        ),
        SavedItem(
            id = "ai-1",
            type = SavedItemType.AiConversation,
            title = "Police stopped my bike without reason",
            subtitle = "Rights during a police check · 3 days ago",
            savedAt = "3 days ago"
        ),
        SavedItem(
            id = "note-1",
            type = SavedItemType.LawyerNote,
            title = "Notes with Adv. Mehra",
            subtitle = "Property dispute consultation · last week",
            savedAt = "Last week"
        )
    )

    private fun NewsArticle.toSavedItem(): SavedItem = SavedItem(
        id       = "$NEWS_PREFIX$id",
        type     = SavedItemType.News,
        title    = title,
        subtitle = listOf(source, category.displayName).filter { it.isNotBlank() }.joinToString(" · "),
        savedAt  = ""
    )

    companion object {
        /** Prefix used to identify news-derived saved items vs in-memory seed. */
        const val NEWS_PREFIX = "news:"
    }
}

