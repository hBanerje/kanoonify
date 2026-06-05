package com.multiplatform.kanoonify.presentation.screens.viewmodel

import com.multiplatform.kanoonify.domain.model.LawItem
import com.multiplatform.kanoonify.domain.model.LawTag

data class LawDetailState(
    val law: LawItem? = null,
    val tag: LawTag = LawTag.RIGHT,
    val userAction: String = "",
    val isLoading: Boolean = false
)
