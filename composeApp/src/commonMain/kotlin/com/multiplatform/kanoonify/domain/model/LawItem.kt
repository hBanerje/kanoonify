package com.multiplatform.kanoonify.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LawItem(
    val id: Int = 0,
    val title: String,
    val category: String = "",
    val description: String = "",
    val punishment: String = "",
    val keywords: List<String> = emptyList()
)