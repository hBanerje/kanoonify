package com.multiplatform.kanoonify.news.platform

import android.content.Intent
import android.net.Uri
import com.multiplatform.kanoonify.data.local.appContext

actual class UrlOpener actual constructor() {

    actual fun openUrl(url: String) {
        val trimmed = url.trim()
        if (trimmed.isBlank()) return
        runCatching {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(trimmed)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            appContext.startActivity(intent)
        }
    }

    actual fun shareText(text: String, title: String) {
        if (text.isBlank()) return
        runCatching {
            val send = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
                putExtra(Intent.EXTRA_TITLE, title)
            }
            val chooser = Intent.createChooser(send, title).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            appContext.startActivity(chooser)
        }
    }
}

