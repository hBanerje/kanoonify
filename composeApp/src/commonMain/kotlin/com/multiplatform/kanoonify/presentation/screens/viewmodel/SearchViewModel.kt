package com.multiplatform.kanoonify.presentation.screens.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _state = MutableStateFlow(
        SearchState(
            recent = seedHistory()
        )
    )
    val state: StateFlow<SearchState> = _state

    private val _events = MutableSharedFlow<SearchUiEvent>(
        extraBufferCapacity = 8,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<SearchUiEvent> = _events.asSharedFlow()

    fun onQueryChange(value: String) {
        _state.update { it.copy(query = value) }
    }

    fun onSubmit() {
        val q = _state.value.query.trim()
        if (q.isBlank()) return
        addToHistory(q)
        scope.launch { _events.emit(SearchUiEvent.NavigateToAsk(q)) }
    }

    fun onTopicClick(label: String) {
        _state.update { it.copy(query = label) }
        addToHistory(label)
        scope.launch { _events.emit(SearchUiEvent.NavigateToAsk(label)) }
    }

    fun onRecentClick(item: RecentSearchItem) {
        _state.update { it.copy(query = item.query) }
        scope.launch { _events.emit(SearchUiEvent.NavigateToAsk(item.query)) }
    }

    fun onRemoveRecent(id: String) {
        _state.update { s -> s.copy(recent = s.recent.filterNot { it.id == id }) }
    }

    fun onClearHistory() {
        _state.update { it.copy(recent = emptyList()) }
    }

    fun onFilterChange(type: SearchEntityType) {
        _state.update { it.copy(activeFilter = type) }
    }

    fun onBrowseLaws()       { emit(SearchUiEvent.NavigateToLaws) }
    fun onOpenConstitution() { emit(SearchUiEvent.NavigateToCoi) }
    fun onConsultLawyer()    { emit(SearchUiEvent.NavigateToLawyers) }
    fun onEmergencyRights()  { emit(SearchUiEvent.NavigateToEmergency) }

    private fun emit(event: SearchUiEvent) {
        scope.launch { _events.emit(event) }
    }

    private fun addToHistory(query: String) {
        _state.update { s ->
            val without = s.recent.filterNot { it.query.equals(query, ignoreCase = true) }
            val updated = listOf(
                RecentSearchItem(
                    id = nextId(),
                    query = query,
                    timestamp = "Just now"
                )
            ) + without
            s.copy(recent = updated.take(10))
        }
    }

    private fun seedHistory(): List<RecentSearchItem> = listOf(
        RecentSearchItem(nextId(), "Police stopped my bike", "2 hours ago"),
        RecentSearchItem(nextId(), "Can police check my phone?", "Yesterday"),
        RecentSearchItem(nextId(), "Noise complaint in my area", "3 days ago")
    )

    private var idCounter = 0L
    private fun nextId(): String = "sh-${++idCounter}"
}
