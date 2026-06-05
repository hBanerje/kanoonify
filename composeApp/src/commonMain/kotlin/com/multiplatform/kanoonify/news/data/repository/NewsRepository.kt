package com.multiplatform.kanoonify.news.data.repository

import com.multiplatform.kanoonify.news.data.datasource.NewsDataSource
import com.multiplatform.kanoonify.news.data.local.NewsCache
import com.multiplatform.kanoonify.news.domain.model.NewsArticle
import com.multiplatform.kanoonify.news.domain.model.NewsCategory
import kotlinx.coroutines.flow.Flow

/**
 * Orchestrates [primary], [fallback] and [cache] sources behind a single
 * coherent API for the news feature.
 *
 *  Strategy:
 *   1. If [forceRefresh] is false and cache is fresh → serve from cache.
 *   2. Try [primary] (remote). On success → persist to cache & return.
 *   3. On failure → return cache contents (even if stale).
 *   4. If cache empty too → return [fallback] (sample) so the UI is never blank.
 *
 *  Bookmarks/Saved/Recent-search are routed straight to [cache] (the durable
 *  store).
 */
class NewsRepository(
    private val primary: NewsDataSource,
    private val fallback: NewsDataSource,
    private val cache: NewsCache
) {

    /* --------------------------- feed reads -------------------------------- */

    suspend fun loadFeed(
        category: NewsCategory,
        forceRefresh: Boolean = false
    ): FeedResult {
        if (!forceRefresh && cache.isFresh(category)) {
            val cached = cache.readCategory(category)
            if (cached.isNotEmpty()) return FeedResult.Success(cached, fromCache = true, isStale = false)
        }
        return try {
            val fresh = if (category == NewsCategory.Latest) primary.fetchLatestNews()
                        else primary.fetchCategoryNews(category)
            if (fresh.isNotEmpty()) {
                cache.writeCategory(category, fresh)
                FeedResult.Success(fresh, fromCache = false, isStale = false)
            } else {
                serveFallback(category)
            }
        } catch (e: Throwable) {
            // Network unavailable / parse error → serve cache + signal offline.
            val cached = cache.readCategory(category)
            if (cached.isNotEmpty()) {
                FeedResult.Success(cached, fromCache = true, isStale = true, offlineReason = e.message)
            } else {
                serveFallback(category, offlineReason = e.message)
            }
        }
    }

    suspend fun search(query: String): SearchResult {
        val q = query.trim()
        if (q.isBlank()) return SearchResult.Success(emptyList(), fromCache = false)
        return try {
            val remote = primary.searchNews(q)
            if (remote.isNotEmpty()) SearchResult.Success(remote, fromCache = false)
            else SearchResult.Success(cache.searchCached(q), fromCache = true)
        } catch (e: Throwable) {
            val cached = cache.searchCached(q)
            if (cached.isNotEmpty()) SearchResult.Success(cached, fromCache = true, offlineReason = e.message)
            else SearchResult.Success(fallback.searchNews(q), fromCache = true, offlineReason = e.message)
        }
    }

    /** Resolve a single article by id from saved → cache → remote. */
    suspend fun fetchArticle(id: String): NewsArticle? =
        cache.readById(id)
            ?: try { primary.fetchArticle(id) } catch (_: Throwable) { null }
            ?: fallback.fetchArticle(id)

    /* --------------------------- bookmarks --------------------------------- */

    fun observeSaved(): Flow<List<NewsArticle>> = cache.observeSaved()
    fun observeSavedIds(): Flow<Set<String>> = cache.observeSavedIds()

    fun saveArticle(article: NewsArticle)   = cache.saveBookmark(article)
    fun removeSavedArticle(id: String)      = cache.removeBookmark(id)
    fun clearAllSaved()                     = cache.clearAllSaved()
    fun isSaved(id: String): Boolean        = cache.isSaved(id)

    /* ------------------------ recent searches ------------------------------ */

    fun observeRecentSearches(): Flow<List<String>> = cache.observeRecentSearches()
    fun recordSearch(query: String)                 = cache.upsertRecentSearch(query.trim())
    fun deleteRecentSearch(query: String)           = cache.deleteRecentSearch(query)
    fun clearRecentSearches()                       = cache.clearRecentSearches()

    /* --------------------------- internals --------------------------------- */

    private suspend fun serveFallback(
        category: NewsCategory,
        offlineReason: String? = null
    ): FeedResult {
        val items = if (category == NewsCategory.Latest) fallback.fetchLatestNews()
                    else fallback.fetchCategoryNews(category)
        return FeedResult.Success(
            articles = items,
            fromCache = true,
            isStale = offlineReason != null,
            offlineReason = offlineReason
        )
    }
}

sealed interface FeedResult {
    data class Success(
        val articles: List<NewsArticle>,
        val fromCache: Boolean,
        val isStale: Boolean,
        val offlineReason: String? = null
    ) : FeedResult
}

sealed interface SearchResult {
    data class Success(
        val articles: List<NewsArticle>,
        val fromCache: Boolean,
        val offlineReason: String? = null
    ) : SearchResult
}

