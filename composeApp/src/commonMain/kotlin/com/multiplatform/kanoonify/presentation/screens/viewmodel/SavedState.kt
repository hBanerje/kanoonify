package com.multiplatform.kanoonify.presentation.screens.viewmodel

/**
 * Categories of saved items in the personal Library. Future persistence
 * (Room / SQLDelight) maps each entity to one of these.
 */
enum class SavedItemType { Law, ConstitutionArticle, AiConversation, News, LawyerNote }

/**
 * UI-facing saved record. Persistence layer maps domain entities → this DTO.
 *  - [titleKey]/[subtitleKey] keep the seed data localisable; once real
 *    persistence lands these become plain `title`/`subtitle` strings.
 */
data class SavedItem(
    val id: String,
    val type: SavedItemType,
    val title: String,
    val subtitle: String,
    val savedAt: String
)

enum class SavedFilter { All, Laws, Coi, Ai, News, Notes }

data class SavedState(
    val items: List<SavedItem> = emptyList(),
    val filter: SavedFilter = SavedFilter.All,
    val isLoading: Boolean = false
) {
    val laws  get() = items.filter { it.type == SavedItemType.Law }
    val coi   get() = items.filter { it.type == SavedItemType.ConstitutionArticle }
    val ai    get() = items.filter { it.type == SavedItemType.AiConversation }
    val news  get() = items.filter { it.type == SavedItemType.News }
    val notes get() = items.filter { it.type == SavedItemType.LawyerNote }

    fun visibleFor(type: SavedItemType): List<SavedItem> = when (filter) {
        SavedFilter.All   -> items.filter { it.type == type }
        SavedFilter.Laws  -> if (type == SavedItemType.Law) laws else emptyList()
        SavedFilter.Coi   -> if (type == SavedItemType.ConstitutionArticle) coi else emptyList()
        SavedFilter.Ai    -> if (type == SavedItemType.AiConversation) ai else emptyList()
        SavedFilter.News  -> if (type == SavedItemType.News) news else emptyList()
        SavedFilter.Notes -> if (type == SavedItemType.LawyerNote) notes else emptyList()
    }

    fun shouldShowSection(type: SavedItemType): Boolean = when (filter) {
        SavedFilter.All   -> true
        SavedFilter.Laws  -> type == SavedItemType.Law
        SavedFilter.Coi   -> type == SavedItemType.ConstitutionArticle
        SavedFilter.Ai    -> type == SavedItemType.AiConversation
        SavedFilter.News  -> type == SavedItemType.News
        SavedFilter.Notes -> type == SavedItemType.LawyerNote
    }

    val isCompletelyEmpty: Boolean get() = items.isEmpty()
}

