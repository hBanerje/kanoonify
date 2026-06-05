package com.multiplatform.kanoonify.news.platform

expect class UrlOpener() {

    fun openUrl(url: String)

    fun shareText(text: String, title: String = "Share")
}
