package com.multiplatform.kanoonify.data.auth

import com.multiplatform.kanoonify.domain.auth.BiometricResult
import com.multiplatform.kanoonify.platform.auth.PlatformBiometricAuth

/**
 * Default [BiometricRepository] that delegates to the platform façade
 * and converts platform-specific outcomes into pure domain [BiometricResult]s.
 *
 * No biometric data is ever stored or persisted — we only proxy a yes/no
 * answer from the OS to the ViewModel layer.
 */
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

