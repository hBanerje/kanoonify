package com.multiplatform.kanoonify.domain.model

data class Law(
    val id: Long = 0,
    val title: String,
    val category: String,
    val description: String,
    val punishment: String
)

