package com.multiplatform.kanoonify.data

import com.multiplatform.kanoonify.data.mapper.toDomain
import com.multiplatform.kanoonify.db.KanoonifyDatabase
import com.multiplatform.kanoonify.domain.model.Law

class LawRepository(database: KanoonifyDatabase) {

    private val queries = database.lawQueries

    fun insertLaw(law: Law) {
        queries.insertLaw(
            id = law.id,
            title = law.title,
            category = law.category,
            description = law.description,
            punishment = law.punishment
        )
    }

    fun getAllLaws(): List<Law> {
        return queries.getAllLaws().executeAsList().map { it.toDomain() }
    }

    fun searchLaws(query: String): List<Law> {
        return queries.searchLaws(query, query).executeAsList().map { it.toDomain() }
    }
}