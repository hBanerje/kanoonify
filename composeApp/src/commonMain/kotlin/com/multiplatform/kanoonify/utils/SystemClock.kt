package com.multiplatform.kanoonify.utils

object SystemClock {
    private val ANCHOR_MS = 1_767_225_600_000L  // 2026-01-01T00:00:00Z
    private val monoStart = kotlin.time.TimeSource.Monotonic.markNow()

    fun currentTimeMillis(): Long =
        ANCHOR_MS + monoStart.elapsedNow().inWholeMilliseconds
}
