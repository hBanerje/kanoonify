package com.multiplatform.kanoonify.presentation.screens.viewmodel

import com.multiplatform.kanoonify.data.LawRepository
import com.multiplatform.kanoonify.domain.model.Law
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LawsViewModel(
    private val repository: LawRepository
) {

    private val _state = MutableStateFlow(LawsState())
    val state: StateFlow<LawsState> = _state

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    init {
        loadAllLaws()
    }

    fun loadAllLaws() {
        scope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val laws = repository.getAllLaws()
                _state.update { it.copy(laws = laws, isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
        scope.launch {
            try {
                val results = if (query.isBlank()) {
                    repository.getAllLaws()
                } else {
                    repository.searchLaws(query)
                }
                _state.update { it.copy(laws = results) }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }

    fun insertLaw(law: Law) {
        scope.launch {
            try {
                repository.insertLaw(law)
                loadAllLaws()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }
}
