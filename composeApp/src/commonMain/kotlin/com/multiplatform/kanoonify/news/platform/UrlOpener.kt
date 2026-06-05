package com.multiplatform.kanoonify.news.platform

/**
 * Platform bridge for opening URLs in the system browser and triggering the
 * native share sheet. Kept in the news package since it's the only consumer
 * today, but happy to live at a higher level if other modules need it later.
 */
expect class UrlOpener() {
    /** Open [url] in the system browser. No-op if the URL is empty or malformed. */
    fun openUrl(url: String)

    /** Present the platform share sheet for [text]. */
    fun shareText(text: String, title: String = "Share")
}

