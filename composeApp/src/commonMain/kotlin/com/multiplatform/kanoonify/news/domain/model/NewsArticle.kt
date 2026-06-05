package com.multiplatform.kanoonify.news.domain.model

/**
 * Canonical news article. Pure domain — independent of remote DTOs, cache
 * rows and UI representations. All mapping happens in the data layer.
 *
 *  - [publishedAtEpochMs] is timezone-neutral.
 *  - [imageUrl] / [articleUrl] / [author] may be empty when upstream omits
 *    them; consumers must handle the empty-string case gracefully.
 */
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

