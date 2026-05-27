package com.multiplatform.kanoonify.data.local

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.stringWithContentsOfFile

/**
 * iOS implementation of [loadJsonFile].
 *
 * Loads a JSON file from the main app bundle (UTF-8 decoded).
 * The [fileName] is expected to include the extension, e.g. "coi_articles.json".
 * Returns an empty string if the file cannot be located or read — callers
 * are expected to handle empty input gracefully.
 *
 * IMPORTANT: the file must be added to the iOS app target in Xcode
 * (Build Phases -> Copy Bundle Resources) for it to be present at runtime.
 */
@OptIn(ExperimentalForeignApi::class)
actual fun loadJsonFile(fileName: String): String {
    val dotIndex = fileName.lastIndexOf('.')
    val name = if (dotIndex >= 0) fileName.substring(0, dotIndex) else fileName
    val ext = if (dotIndex >= 0) fileName.substring(dotIndex + 1) else ""

    val path = NSBundle.mainBundle.pathForResource(name, ext) ?: return ""

    return try {
        NSString.stringWithContentsOfFile(
            path = path,
            encoding = NSUTF8StringEncoding,
            error = null
        ) ?: ""
    } catch (_: Throwable) {
        ""
    }
}
