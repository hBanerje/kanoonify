package com.multiplatform.kanoonify.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val id: Int,
    val title: String,
    val subtitle: String = "",
    val description: String = ""
)

