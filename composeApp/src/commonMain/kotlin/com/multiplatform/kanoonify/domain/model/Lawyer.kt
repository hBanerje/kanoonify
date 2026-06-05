package com.multiplatform.kanoonify.domain.model

data class Lawyer(
    val id: String,
    val name: String,
    val specialization: String,
    val experienceYears: Int,
    val rating: Float,
    val location: String,
    val languages: List<String>,
    val feePerSession: Int,
    val isOnline: Boolean,
    val bio: String
)
