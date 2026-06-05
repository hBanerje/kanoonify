package com.multiplatform.kanoonify.news.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.multiplatform.kanoonify.db.CachedArticle
import com.multiplatform.kanoonify.db.KanoonifyDatabase
import com.multiplatform.kanoonify.db.SavedArticle
import com.multiplatform.kanoonify.news.domain.model.NewsArticle
import com.multiplatform.kanoonify.news.domain.model.NewsCategory
import com.multiplatform.kanoonify.utils.SystemClock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * SQLDelight-backed news cache. Provides:
 *  - read-through cache for category feeds (`writeCategory` + `readCategory`)
 *  - durable bookmark store (`saveBookmark` / `removeBookmark` / [observeSaved])
 *  - recent-search history for the in-app search screen.
 *
 * Pure infrastructure — no policy lives here. The repository decides when
 * to read vs refresh vs fall back.
 */
class NewsCache(private val db: KanoonifyDatabase) {

    private val news    = db.newsQueries
    private val cacheTtlMs: Long = 30 * 60 * 1_000L  // 30 min

    /* ----------------------------- cache ----------------------------------- */

    fun writeCategory(category: NewsCategory, articles: List<NewsArticle>) {
        val now = SystemClock.currentTimeMillis()
        db.transaction {
            news.clearCachedCategory(category.slug)
            articles.forEach { a ->
                news.upsertCachedArticle(
                    id          = a.id,
                    title       = a.title,
                    description = a.description,
                    content     = a.content,
                    imageUrl    = a.imageUrl,
                    source      = a.source,
                    author      = a.author,
                    publishedAt = a.publishedAtEpochMs,
                    category    = a.category.slug,
                    articleUrl  = a.articleUrl,
                    cachedAt    = now
                )
            }
        }
    }

    fun readCategory(category: NewsCategory): List<NewsArticle> =
        news.selectCachedByCategory(category.slug).executeAsList().map { it.toDomain() }

    fun readAll(): List<NewsArticle> =
        news.selectAllCached().executeAsList().map { it.toDomain() }

    fun readById(id: String): NewsArticle? =
        news.selectCachedById(id).executeAsOneOrNull()?.toDomain()

    fun searchCached(query: String): List<NewsArticle> =
        news.searchCached(query).executeAsList().map { it.toDomain() }

    /** True when we have *any* cached data for this category newer than TTL. */
    fun isFresh(category: NewsCategory): Boolean {
        val maxCachedAt = news.cachedFreshness(category.slug).executeAsOneOrNull()?.MAX ?: return false
        return SystemClock.currentTimeMillis() - maxCachedAt < cacheTtlMs
    }

    fun clearAllCached() = news.clearAllCached()

    /* ---------------------------- bookmarks -------------------------------- */

    fun saveBookmark(article: NewsArticle) {
        news.insertSavedArticle(
            id          = article.id,
            title       = article.title,
            description = article.description,
            content     = article.content,
            imageUrl    = article.imageUrl,
            source      = article.source,
            author      = article.author,
            publishedAt = article.publishedAtEpochMs,
            category    = article.category.slug,
            articleUrl  = article.articleUrl,
            savedAt     = SystemClock.currentTimeMillis()
        )
    }

    fun removeBookmark(id: String) = news.deleteSavedArticle(id)

    fun clearAllSaved() = news.clearAllSaved()

    fun isSaved(id: String): Boolean =
        news.isArticleSaved(id).executeAsOneOrNull() == true

    fun observeSaved(): Flow<List<NewsArticle>> =
        news.selectAllSaved()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { rows -> rows.map { it.toDomainSaved() } }

    fun observeSavedIds(): Flow<Set<String>> =
        news.selectSavedIds()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { it.toSet() }

    /* ------------------------ recent searches ------------------------------ */

    fun upsertRecentSearch(query: String) {
        news.upsertRecentSearch(query, SystemClock.currentTimeMillis())
    }

    fun deleteRecentSearch(query: String) = news.deleteRecentSearch(query)

    fun clearRecentSearches() = news.clearRecentSearches()

    fun observeRecentSearches(): Flow<List<String>> =
        news.selectRecentSearches()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { rows -> rows.map { it.query } }
}

/* -------------------------- private mappers -------------------------------- */

private fun CachedArticle.toDomain(): NewsArticle = NewsArticle(
    id = id,
    title = title,
    description = description,
    content = content,
    imageUrl = imageUrl,
    source = source,
    author = author,
    publishedAtEpochMs = publishedAt,
    category = NewsCategory.fromSlug(category),
    articleUrl = articleUrl
)

private fun SavedArticle.toDomainSaved(): NewsArticle = NewsArticle(
    id = id,
    title = title,
    description = description,
    content = content,
    imageUrl = imageUrl,
    source = source,
    author = author,
    publishedAtEpochMs = publishedAt,
    category = NewsCategory.fromSlug(category),
    articleUrl = articleUrl
)


