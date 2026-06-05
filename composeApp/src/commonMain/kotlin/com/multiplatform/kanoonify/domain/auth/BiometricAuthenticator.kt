package com.multiplatform.kanoonify.domain.auth

interface BiometricAuthenticator {

    fun isAvailable(): Boolean

    suspend fun authenticate(
        title: String,
        subtitle: String,
        description: String
    ): BiometricResult
}
