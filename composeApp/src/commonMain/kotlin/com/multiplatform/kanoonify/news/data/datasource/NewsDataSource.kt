package com.multiplatform.kanoonify.news.data.datasource

import com.multiplatform.kanoonify.news.domain.model.NewsArticle
import com.multiplatform.kanoonify.news.domain.model.NewsCategory

/**
 * Provider abstraction for news.
 *
 * The repository orchestrates *which* source to consult and *when* (cache,
 * remote, sample, fallback) — this interface only describes "where can we
 * get articles from?". Future providers (RSS, Hacker News, custom backend)
 * only need to implement these four methods.
 */
interface NewsDataSource {

    /** Latest top-headlines, country-agnostic. */
    suspend fun fetchLatestNews(): List<NewsArticle>

    /** Headlines filtered to a single category. */
    suspend fun fetchCategoryNews(category: NewsCategory): List<NewsArticle>

    /** Free-text search across the source's index. */
    suspend fun searchNews(query: String): List<NewsArticle>

    /** Fetch a single article by id. Returns null if not present. */
    suspend fun fetchArticle(articleId: String): NewsArticle?
}

