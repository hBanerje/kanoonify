package com.multiplatform.kanoonify.data.local

import com.multiplatform.kanoonify.domain.model.Article
import kotlinx.serialization.json.Json

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

            emptyList()
        }
    }
}
