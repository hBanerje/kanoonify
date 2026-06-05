package com.multiplatform.kanoonify.news.platform

import platform.Foundation.NSURL
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication

actual class UrlOpener actual constructor() {

    actual fun openUrl(url: String) {
        val trimmed = url.trim()
        if (trimmed.isBlank()) return
        val nsUrl = NSURL.URLWithString(trimmed) ?: return
        val app = UIApplication.sharedApplication
        if (app.canOpenURL(nsUrl)) {
            app.openURL(nsUrl, options = emptyMap<Any?, Any?>(), completionHandler = null)
        }
    }

    actual fun shareText(text: String, title: String) {
        if (text.isBlank()) return
        val activityVC = UIActivityViewController(
            activityItems = listOf(text),
            applicationActivities = null
        )
        val root = UIApplication.sharedApplication.keyWindow?.rootViewController ?: return

        var presenter: platform.UIKit.UIViewController = root
        while (presenter.presentedViewController != null) {
            presenter = presenter.presentedViewController!!
        }
        presenter.presentViewController(activityVC, animated = true, completion = null)
    }
}
