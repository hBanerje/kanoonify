package com.multiplatform.kanoonify.news.domain.model

data class NewsArticle(
    val id: String,
    val title: String,
    val description: String,
    val content: String,
    val imageUrl: String,
    val source: String,
    val author: String,
    val publishedAtEpochMs: Long,
    val category: NewsCategory,
    val articleUrl: String
)
