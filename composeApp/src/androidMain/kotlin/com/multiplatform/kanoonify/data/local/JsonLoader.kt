package com.multiplatform.kanoonify.data.local

import android.content.Context

lateinit var appContext: Context

actual fun loadJsonFile(fileName: String): String {
    return appContext.assets.open(fileName)
        .bufferedReader()
        .use { it.readText() }
}
