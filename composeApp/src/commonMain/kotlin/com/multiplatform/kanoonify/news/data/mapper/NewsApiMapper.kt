package com.multiplatform.kanoonify.news.data.mapper

import com.multiplatform.kanoonify.news.data.remote.NewsApiArticleDto
import com.multiplatform.kanoonify.news.domain.model.NewsArticle
import com.multiplatform.kanoonify.news.domain.model.NewsCategory

/**
 * DTO ↔ Domain conversions. Centralised so wire-format quirks (null fields,
 * field aliases like `urlToImage` vs `image`) never bleed into the rest of
 * the codebase.
 */
object NewsApiMapper {

    /**
     * Convert a wire DTO into the domain model. Drops articles missing both
     * a title and a body — those are unsalvageable for display.
     */
    fun toDomain(
        dto: NewsApiArticleDto,
        category: NewsCategory,
        fallbackNowEpochMs: Long
    ): NewsArticle? {
        val title = dto.title?.trim().orEmpty()
        if (title.isBlank()) return null

        val description = dto.description?.trim().orEmpty()
        val content     = dto.content?.trim().orEmpty().ifBlank { description }
        if (description.isBlank() && content.isBlank()) return null

        val image = (dto.image ?: dto.urlToImage).orEmpty()
        val src   = dto.source?.name.orEmpty().ifBlank { "Unknown" }
        val url   = dto.url.orEmpty()
        val publishedAt = Iso8601.parseEpochMs(dto.publishedAt) ?: fallbackNowEpochMs

        val id = url.ifBlank { "${src}-${title.hashCode()}" }

        return NewsArticle(
            id = id,
            title = title,
            description = description,
            content = content,
            imageUrl = image,
            source = src,
            author = dto.author?.trim().orEmpty(),
            publishedAtEpochMs = publishedAt,
            category = category,
            articleUrl = url
        )
    }
}

/**
 * Minimal ISO-8601 parser sufficient for `YYYY-MM-DDTHH:MM:SSZ` shapes
 * produced by NewsAPI/GNews. KMP-pure (no java.time).
 */
private object Iso8601 {
    fun parseEpochMs(value: String?): Long? {
        if (value.isNullOrBlank()) return null
        // Expected: 2026-05-30T11:22:33Z  (optional fractional seconds tolerated)
        return try {
            val s = value.trim().removeSuffix("Z")
            val tIdx = s.indexOf('T').takeIf { it >= 0 } ?: return null
            val (date, time) = s.substring(0, tIdx) to s.substring(tIdx + 1).substringBefore('+').substringBefore('-')
            val (y, mo, d) = date.split('-').map { it.toInt() }
            val timeParts = time.split(':')
            val h  = timeParts.getOrNull(0)?.toInt() ?: 0
            val mi = timeParts.getOrNull(1)?.toInt() ?: 0
            val se = timeParts.getOrNull(2)?.substringBefore('.')?.toInt() ?: 0
            daysFromCivil(y, mo, d) * 86_400_000L +
                h  * 3_600_000L +
                mi * 60_000L +
                se * 1_000L
        } catch (_: Throwable) {
            null
        }
    }

    /** Howard Hinnant's date algorithm — civil date → days since 1970-01-01. */
    private fun daysFromCivil(y: Int, m: Int, d: Int): Long {
        val yy = if (m <= 2) y - 1 else y
        val era = (if (yy >= 0) yy else yy - 399) / 400
        val yoe = (yy - era * 400).toLong()
        val doy = (153L * (if (m > 2) m - 3 else m + 9) + 2) / 5 + (d - 1)
        val doe = yoe * 365 + yoe / 4 - yoe / 100 + doy
        return era * 146097L + doe - 719468L
    }
}

