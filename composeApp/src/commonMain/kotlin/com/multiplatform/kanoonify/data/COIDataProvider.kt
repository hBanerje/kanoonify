package com.multiplatform.kanoonify.data

import com.multiplatform.kanoonify.data.local.COIDataSource
import com.multiplatform.kanoonify.domain.model.Article

/**
 * Provides access to Constitution of India articles loaded from JSON.
 * Follows the same caching pattern as [LawListProvider].
 */
object COIDataProvider {

    private var cachedArticles: List<Article>? = null

    private fun getAll(): List<Article> {
        if (cachedArticles == null) {
            cachedArticles = COIDataSource.loadArticles()
        }
        return cachedArticles!!
    }

    fun allArticles(): List<Article> = getAll()

    fun getArticleById(id: Int): Article? = getAll().firstOrNull { it.id == id }
}

