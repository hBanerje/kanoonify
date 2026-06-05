package com.multiplatform.kanoonify.news.domain.util

import kotlin.math.max

/**
 * Lightweight "X minutes/hours/days ago" formatter. KMP-friendly (no
 * platform date libs) and stable for use inside Composables.
 *
 *  - Returns "Just now" for <60s diffs.
 *  - Falls back to "long ago" for >365d to avoid noisy output.
 */
object RelativeTime {

    private const val MINUTE_MS = 60_000L
    private const val HOUR_MS   = 60L * MINUTE_MS
    private const val DAY_MS    = 24L * HOUR_MS
    private const val WEEK_MS   = 7L  * DAY_MS
    private const val YEAR_MS   = 365L * DAY_MS

    fun format(publishedAtEpochMs: Long, nowEpochMs: Long): String {
        val diff = max(0L, nowEpochMs - publishedAtEpochMs)
        return when {
            diff < MINUTE_MS -> "Just now"
            diff < HOUR_MS   -> "${diff / MINUTE_MS}m ago"
            diff < DAY_MS    -> "${diff / HOUR_MS}h ago"
            diff < WEEK_MS   -> "${diff / DAY_MS}d ago"
            diff < YEAR_MS   -> "${diff / WEEK_MS}w ago"
            else             -> "long ago"
        }
    }
}

