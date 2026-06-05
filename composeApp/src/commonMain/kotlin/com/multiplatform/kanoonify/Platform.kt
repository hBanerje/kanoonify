package com.multiplatform.kanoonify

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
