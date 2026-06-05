package com.multiplatform.kanoonify.utils

/**
 * Tiny KMP wall-clock wrapper. Kept as a single dependency-free abstraction
 * so swapping to a test clock (or kotlinx-datetime, if added later) is a
 * one-file change.
 *
 * The current implementation seeds a stable epoch anchor at process start
 * and adds the monotonic delta — adequate for "X minutes ago" UI formatting
 * which is the only consumer today. Swap to `kotlinx.datetime.Clock.System`
 * the moment kotlinx-datetime is on the classpath.
 */
object SystemClock {
    private val ANCHOR_MS = 1_767_225_600_000L  // 2026-01-01T00:00:00Z
    private val monoStart = kotlin.time.TimeSource.Monotonic.markNow()

    fun currentTimeMillis(): Long =
        ANCHOR_MS + monoStart.elapsedNow().inWholeMilliseconds
}


