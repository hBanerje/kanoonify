package com.multiplatform.kanoonify.presentation.screens.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/**
 * Drives the personal Library screen. Currently backed by an in-memory list so
 * UI is fully exercisable; the contract is intentionally
 * `addItem` / `removeItem` / `clearSection` so a future
 * `SavedRepository` (Room or SQLDelight) can be plugged in unchanged.
 *
 * The seed data is replaced 1:1 in [seed] — wire a repository constructor arg
 * once persistence lands.
 */
class SavedViewModel {

    private val _state = MutableStateFlow(SavedState(items = seed()))
    val state: StateFlow<SavedState> = _state

    /* ------------------------------ intents -------------------------------- */

    fun onFilterChange(filter: SavedFilter) {
        _state.update { it.copy(filter = filter) }
    }

    fun onRemoveItem(id: String) {
        _state.update { s -> s.copy(items = s.items.filterNot { it.id == id }) }
    }

    fun onClearSection(type: SavedItemType) {
        _state.update { s -> s.copy(items = s.items.filterNot { it.type == type }) }
    }

    fun onClearAll() {
        _state.update { it.copy(items = emptyList()) }
    }

    /** Public for future repository writes / bookmarking. */
    fun addItem(item: SavedItem) {
        _state.update { it.copy(items = listOf(item) + it.items) }
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
            id = "news-1",
            type = SavedItemType.News,
            title = "SC ruling on right to privacy expanded",
            subtitle = "Bar & Bench · 2 days ago",
            savedAt = "2 days ago"
        ),
        SavedItem(
            id = "note-1",
            type = SavedItemType.LawyerNote,
            title = "Notes with Adv. Mehra",
            subtitle = "Property dispute consultation · last week",
            savedAt = "Last week"
        )
    )
}

