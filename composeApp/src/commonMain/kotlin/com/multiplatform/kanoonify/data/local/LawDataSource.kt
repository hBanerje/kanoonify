package com.multiplatform.kanoonify.data.local

import com.multiplatform.kanoonify.domain.model.LawItem
import kotlinx.serialization.json.Json

expect fun loadJsonFile(fileName: String): String

object LawDataSource {

    fun loadLaws(): List<LawItem> {
        val jsonString = loadJsonFile("laws.json")

        return Json.decodeFromString<List<LawItem>>(jsonString)
    }
}