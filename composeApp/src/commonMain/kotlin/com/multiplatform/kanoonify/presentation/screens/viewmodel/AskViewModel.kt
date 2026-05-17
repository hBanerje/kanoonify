package com.multiplatform.kanoonify.presentation.screens.viewmodel

import com.multiplatform.kanoonify.data.local.LawDataSource
import com.multiplatform.kanoonify.data.repository.LawRepository
import com.multiplatform.kanoonify.domain.model.LawItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AskViewModel {
    private val _state = MutableStateFlow(AskState())
    val state: StateFlow<AskState> = _state

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val laws: List<LawItem> = LawDataSource.loadLaws()

    fun onQueryChange(newQuery: String) {
        _state.update { it.copy(query = newQuery) }
    }

    fun onSubmit() {
        val userQuery = _state.value.query.trim()
        if (userQuery.isBlank()) return

        _state.update {
            it.copy(
                query = "",
                isLoading = true,
                turns = it.turns + AskTurn.User(userQuery)
            )
        }

        scope.launch {
            val answer = LawRepository.findAnswer(userQuery, laws)
            delay(900L) // brief "thinking" pause
            _state.update {
                it.copy(
                    isLoading = false,
                    turns = it.turns + AskTurn.Assistant(answer)
                )
            }
        }
    }
}

