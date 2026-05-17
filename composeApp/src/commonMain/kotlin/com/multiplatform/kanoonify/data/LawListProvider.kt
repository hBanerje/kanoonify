package com.multiplatform.kanoonify.data

import com.multiplatform.kanoonify.data.local.LawDataSource
import com.multiplatform.kanoonify.domain.model.LawItem
import com.multiplatform.kanoonify.domain.model.SubCategory

object LawListProvider {

    private var cachedLaws: List<LawItem>? = null

    private fun getAllLaws(): List<LawItem> {
        if (cachedLaws == null) {
            cachedLaws = LawDataSource.loadLaws()
        }
        return cachedLaws!!
    }

    fun getLawsBySubCategory(subCategory: SubCategory): List<LawItem> {
        return getLawsBySubCategory(getAllLaws(), subCategory.keywords)
    }

    fun getLawsBySubCategory(
        laws: List<LawItem>,
        subCategoryKeywords: List<String>
    ): List<LawItem> {
        return laws.filter { law ->
            law.keywords.any { lawKeyword ->
                subCategoryKeywords.any { subKeyword ->
                    lawKeyword.contains(subKeyword, ignoreCase = true) ||
                        subKeyword.contains(lawKeyword, ignoreCase = true)
                }
            }
        }
    }

    fun getLawsBySubCategoryFromAll(subCategoryKeywords: List<String>): List<LawItem> {
        return getLawsBySubCategory(getAllLaws(), subCategoryKeywords)
    }

    fun getLawsByCategory(category: String): List<LawItem> {
        val allLaws = getAllLaws()
        return allLaws.filter { it.category.equals(category, ignoreCase = true) }
    }
}

