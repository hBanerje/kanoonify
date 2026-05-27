package com.multiplatform.kanoonify.presentation.screens.viewmodel

import com.multiplatform.kanoonify.data.LawListProvider
import com.multiplatform.kanoonify.domain.model.deriveLawTag
import com.multiplatform.kanoonify.domain.model.deriveUserAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel for the law-detail screen.
 * All domain logic (tag derivation, user-action text) lives here, not in the Composable.
 */
class LawDetailViewModel(lawId: Int) {

    private val _state = MutableStateFlow(LawDetailState())
    val state: StateFlow<LawDetailState> = _state

    init {
        val law = LawListProvider.getLawById(lawId)
        if (law != null) {
            _state.update {
                it.copy(
                    law = law,
                    tag = deriveLawTag(law.punishment),
                    userAction = deriveUserAction(law),
                    isLoading = false
                )
            }
        }
    }
}

