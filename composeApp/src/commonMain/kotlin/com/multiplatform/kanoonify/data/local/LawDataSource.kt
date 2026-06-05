package com.multiplatform.kanoonify.data.local

import com.multiplatform.kanoonify.domain.model.LawItem
import kotlinx.serialization.json.Json

expect fun loadJsonFile(fileName: String): String

object LawDataSource {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    fun loadLaws(): List<LawItem> {
        val jsonString = loadJsonFile("laws.json")
        if (jsonString.isBlank()) return emptyList()
        return try {
            json.decodeFromString<List<LawItem>>(jsonString)
        } catch (_: Throwable) {
            emptyList()
        }
    }
}
