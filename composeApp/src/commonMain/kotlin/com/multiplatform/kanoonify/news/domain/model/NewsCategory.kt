package com.multiplatform.kanoonify.news.domain.model

/**
 * Canonical News categories supported by Kanoonify.
 *
 *  - [slug] is what we send to the upstream API (and persist in cache).
 *  - [displayName] is what we show in the UI (string resource lookup is done
 *    in the presentation layer; the domain model stays platform-neutral).
 */
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

