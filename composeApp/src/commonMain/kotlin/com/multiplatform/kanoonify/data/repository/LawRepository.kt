package com.multiplatform.kanoonify.data.repository

import com.multiplatform.kanoonify.domain.model.LawItem

object LawRepository {

    fun findLaw(query: String, laws: List<LawItem>): String {
        val match = laws.find {
            it.keywords.any { keyword ->
                query.contains(keyword, ignoreCase = true)
            }
        }

        return match?.let {
            """
        📋 ${it.title}
        📂 Category: ${it.category}
        📝 ${it.description}
        ⚖️ Punishment: ${it.punishment}
            """.trimIndent()
        } ?: "No relevant law found for your query. Try describing your situation differently."
    }
}