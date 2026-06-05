package com.multiplatform.kanoonify.news.data.datasource

import com.multiplatform.kanoonify.news.domain.model.NewsArticle
import com.multiplatform.kanoonify.news.domain.model.NewsCategory

interface NewsDataSource {

    suspend fun fetchLatestNews(): List<NewsArticle>

    suspend fun fetchCategoryNews(category: NewsCategory): List<NewsArticle>

    suspend fun searchNews(query: String): List<NewsArticle>

    suspend fun fetchArticle(articleId: String): NewsArticle?
}
