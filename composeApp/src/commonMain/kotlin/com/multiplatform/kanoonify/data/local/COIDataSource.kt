package com.multiplatform.kanoonify.data.local

import com.multiplatform.kanoonify.domain.model.Article
import kotlinx.serialization.json.Json

/**
 * Loads and parses Constitution of India articles from a bundled JSON file.
 *
 * The file is resolved through the platform [loadJsonFile] expect/actual,
 * so no platform-specific path is hardcoded here. The same [FILE_NAME]
 * is expected on both Android (assets/) and iOS (app bundle).
 */
object COIDataSource {

    private const val FILE_NAME = "coi_articles.json"

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    fun loadArticles(): List<Article> {
        val jsonString = try {
            loadJsonFile(FILE_NAME)
        } catch (t: Throwable) {
            return emptyList()
        }

        if (jsonString.isBlank()) return emptyList()

        return try {
            json.decodeFromString(jsonString)
        } catch (t: Throwable) {
            // Malformed JSON should never crash the app — surface empty list.
            emptyList()
        }
    }
}
