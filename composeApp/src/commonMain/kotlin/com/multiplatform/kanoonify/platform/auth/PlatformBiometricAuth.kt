package com.multiplatform.kanoonify.platform.auth

import com.multiplatform.kanoonify.domain.auth.BiometricResult

expect class PlatformBiometricAuth() {

    fun isBiometricAvailable(): Boolean

    suspend fun authenticate(
        title: String,
        subtitle: String,
        description: String
    ): BiometricResult
}
