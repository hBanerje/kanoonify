package com.multiplatform.kanoonify.news.domain.model

enum class NewsCategory(val slug: String, val displayName: String) {
    Latest    ("latest",     "Latest"),
    Politics  ("politics",   "Politics"),
    Parliament("parliament", "Parliament"),
    Corporate ("corporate",  "Corporate"),
    Finance   ("finance",    "Finance"),
    Technology("technology", "Technology"),
    Law       ("law",        "Law"),
    India     ("india",      "India"),
    World     ("world",      "World"),
    Business  ("business",   "Business"),
    Sports    ("sports",     "Sports");

    companion object {
        fun fromSlug(slug: String): NewsCategory =
            entries.firstOrNull { it.slug.equals(slug, ignoreCase = true) } ?: Latest
    }
}
