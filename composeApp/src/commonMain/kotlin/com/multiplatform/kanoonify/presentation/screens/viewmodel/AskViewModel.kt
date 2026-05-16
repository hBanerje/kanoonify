package com.multiplatform.kanoonify.presentation.screens.viewmodel

import com.multiplatform.kanoonify.data.local.LawDataSource
import com.multiplatform.kanoonify.data.remote.OpenAIService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.multiplatform.kanoonify.data.repository.LawRepository.findLaw
import com.multiplatform.kanoonify.domain.model.LawItem
import com.multiplatform.kanoonify.presentation.screens.components.ChatMessage

class AskViewModel {
    private val _state = MutableStateFlow(AskState())
    val state: StateFlow<AskState> = _state

    private val aiService = OpenAIService()

    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    var laws: List<LawItem> = emptyList()

    init {

        laws = LawDataSource.loadLaws()

    }

    fun onQueryChange(newQuery: String) {
        _state.update { it.copy(query = newQuery) }
    }

    fun onSubmit() {
        val userQuery = _state.value.query

        if (userQuery.isBlank()) return

        // Add user message
        _state.update {
            it.copy(
                query = "",
                isLoading = true,
                messages = it.messages + ChatMessage(userQuery, true)
            )
        }

        viewModelScope.launch {
            val result = findLaw(userQuery, laws)

            // Simulate AI thinking delay
            delay(1500L)

            _state.update {
                it.copy(
                    isLoading = false,
                    messages = it.messages + ChatMessage(result, false)
                )
            }
        }
    }
}