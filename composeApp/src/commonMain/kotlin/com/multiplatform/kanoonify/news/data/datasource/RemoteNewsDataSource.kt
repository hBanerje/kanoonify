package com.multiplatform.kanoonify.news.data.datasource

import com.multiplatform.kanoonify.news.data.mapper.NewsApiMapper
import com.multiplatform.kanoonify.news.data.remote.NewsApiResponse
import com.multiplatform.kanoonify.news.data.remote.NewsApiService
import com.multiplatform.kanoonify.news.domain.model.NewsArticle
import com.multiplatform.kanoonify.news.domain.model.NewsCategory
import com.multiplatform.kanoonify.utils.SystemClock

class RemoteNewsDataSource(
    private val api: NewsApiService
) : NewsDataSource {

    override suspend fun fetchLatestNews(): List<NewsArticle> =
        map(api.topHeadlines(category = null), NewsCategory.Latest)

    override suspend fun fetchCategoryNews(category: NewsCategory): List<NewsArticle> {
        val response = when (val resolved = category.toNewsApi()) {
            is NewsApiQuery.TopHeadlines -> api.topHeadlines(
                category = resolved.category,
                country  = resolved.country
            )
            is NewsApiQuery.Everything   -> api.everything(query = resolved.query)
        }
        return map(response, category)
    }

    override suspend fun searchNews(query: String): List<NewsArticle> =
        map(api.search(query = query), NewsCategory.Latest)

    override suspend fun fetchArticle(articleId: String): NewsArticle? = null

    private fun map(
        response: NewsApiResponse,
        category: NewsCategory
    ): List<NewsArticle> {
        if (response.status.equals("error", ignoreCase = true)) {
            throw IllegalStateException(response.message ?: "NewsAPI error")
        }
        val now = SystemClock.currentTimeMillis()
        return response.articles.mapNotNull {
            NewsApiMapper.toDomain(it, category, now)
        }
    }

    private sealed interface NewsApiQuery {
        data class TopHeadlines(val category: String?, val country: String?) : NewsApiQuery
        data class Everything(val query: String) : NewsApiQuery
    }

    private fun NewsCategory.toNewsApi(): NewsApiQuery = when (this) {
        NewsCategory.Latest     -> NewsApiQuery.TopHeadlines(category = null,           country = "in")
        NewsCategory.India      -> NewsApiQuery.TopHeadlines(category = null,           country = "in")
        NewsCategory.World      -> NewsApiQuery.TopHeadlines(category = null,           country = null)
        NewsCategory.Business   -> NewsApiQuery.TopHeadlines(category = "business",     country = "in")
        NewsCategory.Finance    -> NewsApiQuery.TopHeadlines(category = "business",     country = "in")
        NewsCategory.Technology -> NewsApiQuery.TopHeadlines(category = "technology",   country = "in")
        NewsCategory.Sports     -> NewsApiQuery.TopHeadlines(category = "sports",       country = "in")
        NewsCategory.Politics   -> NewsApiQuery.Everything(query = "India AND (politics OR election OR government)")
        NewsCategory.Parliament -> NewsApiQuery.Everything(query = "India AND (parliament OR \"Lok Sabha\" OR \"Rajya Sabha\")")
        NewsCategory.Corporate  -> NewsApiQuery.Everything(query = "India AND (corporate OR \"listed company\" OR earnings)")
        NewsCategory.Law        -> NewsApiQuery.Everything(query = "India AND (law OR \"Supreme Court\" OR \"High Court\" OR judgment)")
    }
}
