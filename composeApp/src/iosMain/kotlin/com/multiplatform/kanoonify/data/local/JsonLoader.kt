package com.multiplatform.kanoonify.data.local

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.stringWithContentsOfFile

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
