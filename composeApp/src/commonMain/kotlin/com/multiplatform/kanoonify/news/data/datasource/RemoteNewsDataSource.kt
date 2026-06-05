package com.multiplatform.kanoonify.news.data.datasource

import com.multiplatform.kanoonify.news.data.mapper.NewsApiMapper
import com.multiplatform.kanoonify.news.data.remote.NewsApiService
import com.multiplatform.kanoonify.news.domain.model.NewsArticle
import com.multiplatform.kanoonify.news.domain.model.NewsCategory
import com.multiplatform.kanoonify.utils.SystemClock

/**
 * Remote (HTTP) implementation of [NewsDataSource] backed by [NewsApiService].
 *
 * Network/parse errors are NOT swallowed here — they propagate up to the
 * repository which decides whether to fall back to cache/sample. That keeps
 * this class small, testable and free of policy.
 */
class RemoteNewsDataSource(
    private val api: NewsApiService
) : NewsDataSource {

    override suspend fun fetchLatestNews(): List<NewsArticle> =
        map(api.topHeadlines(category = null), NewsCategory.Latest)

    override suspend fun fetchCategoryNews(category: NewsCategory): List<NewsArticle> =
        map(api.topHeadlines(category = category.slug), category)

    override suspend fun searchNews(query: String): List<NewsArticle> =
        map(api.search(query = query), NewsCategory.Latest)

    /**
     * The remote API doesn't expose a per-id endpoint in the NewsAPI/GNews
     * envelope — articles are identified by their URL. The repository owns
     * the lookup-by-id concern (cache + saved), so this returns null.
     */
    override suspend fun fetchArticle(articleId: String): NewsArticle? = null

    private fun map(
        response: com.multiplatform.kanoonify.news.data.remote.NewsApiResponse,
        category: NewsCategory
    ): List<NewsArticle> {
        val now = SystemClock.currentTimeMillis()
        return response.articles.mapNotNull {
            NewsApiMapper.toDomain(it, category, now)
        }
    }
}

