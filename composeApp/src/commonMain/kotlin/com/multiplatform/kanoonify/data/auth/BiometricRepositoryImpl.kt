package com.multiplatform.kanoonify.data.auth

import com.multiplatform.kanoonify.domain.auth.BiometricResult
import com.multiplatform.kanoonify.platform.auth.PlatformBiometricAuth

class BiometricRepositoryImpl(
    private val platform: PlatformBiometricAuth = PlatformBiometricAuth()
) : BiometricRepository {

    override fun isAvailable(): Boolean = platform.isBiometricAvailable()

    override suspend fun authenticate(
        title: String,
        subtitle: String,
        description: String
    ): BiometricResult = runCatching {
        platform.authenticate(title, subtitle, description)
    }.getOrElse { t ->
        BiometricResult.Error(t.message ?: "Unknown authentication failure")
    }
}
