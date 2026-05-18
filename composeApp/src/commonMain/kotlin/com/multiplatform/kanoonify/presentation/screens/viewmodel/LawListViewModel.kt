package com.multiplatform.kanoonify.presentation.screens.viewmodel

import com.multiplatform.kanoonify.data.LawListProvider
import com.multiplatform.kanoonify.domain.model.SubCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel for the law-list screen.
 * Keeps data-access out of the Composable.
 */
class LawListViewModel(subCategory: SubCategory) {

    private val _state = MutableStateFlow(LawListState())
    val state: StateFlow<LawListState> = _state

    init {
        val laws = LawListProvider.getLawsBySubCategory(subCategory)
        _state.update {
            it.copy(
                laws = laws,
                title = subCategory.title,
                isLoading = false
            )
        }
    }
}

