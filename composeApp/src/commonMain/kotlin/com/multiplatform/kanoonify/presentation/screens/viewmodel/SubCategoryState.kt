package com.multiplatform.kanoonify.presentation.screens.viewmodel

import com.multiplatform.kanoonify.domain.model.SubCategory

data class SubCategoryState(
    val category: String = "",
    val subcategories: List<SubCategory> = emptyList()
)

