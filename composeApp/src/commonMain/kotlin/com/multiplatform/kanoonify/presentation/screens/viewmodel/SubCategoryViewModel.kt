package com.multiplatform.kanoonify.presentation.screens.viewmodel

import com.multiplatform.kanoonify.data.CategoryDataProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel for the subcategory screen.
 * Keeps CategoryDataProvider access out of the Composable.
 */
class SubCategoryViewModel(category: String) {

    private val _state = MutableStateFlow(SubCategoryState())
    val state: StateFlow<SubCategoryState> = _state

    init {
        val subs = CategoryDataProvider.getSubcategories(category)
        _state.update { it.copy(category = category, subcategories = subs) }
    }
}

