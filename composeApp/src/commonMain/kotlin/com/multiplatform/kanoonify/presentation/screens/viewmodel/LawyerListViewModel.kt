package com.multiplatform.kanoonify.presentation.screens.viewmodel

import com.multiplatform.kanoonify.data.LawyerDataProvider
import com.multiplatform.kanoonify.domain.model.Lawyer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class LawyerListState(
    val searchQuery: String = "",
    val lawyers: List<Lawyer> = LawyerDataProvider.lawyers
) {
    val filtered: List<Lawyer>
        get() = if (searchQuery.isBlank()) lawyers
        else lawyers.filter { l ->
            l.name.contains(searchQuery, ignoreCase = true) ||
                    l.specialization.contains(searchQuery, ignoreCase = true) ||
                    l.location.contains(searchQuery, ignoreCase = true) ||
                    l.languages.any { it.contains(searchQuery, ignoreCase = true) }
        }
}

class LawyerListViewModel {
    private val _state = MutableStateFlow(LawyerListState())
    val state: StateFlow<LawyerListState> = _state

    fun updateSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }
}
