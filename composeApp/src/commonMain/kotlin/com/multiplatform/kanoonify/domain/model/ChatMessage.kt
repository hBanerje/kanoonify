package com.multiplatform.kanoonify.domain.model
enum class ChatAuthor { User, Lawyer }
data class ChatMessage(
    val id: String,
    val author: ChatAuthor,
    val text: String,
    val timestamp: String
)
