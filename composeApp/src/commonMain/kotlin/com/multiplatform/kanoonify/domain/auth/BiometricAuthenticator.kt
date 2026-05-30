package com.multiplatform.kanoonify.domain.auth

/**
 * Domain abstraction for biometric authentication.
 *
 * Implemented by the data layer ([com.multiplatform.kanoonify.data.auth.BiometricRepository]).
 * ViewModels depend only on this interface — never on platform code.
 */
interface BiometricAuthenticator {

    /** Whether biometric authentication can be performed on this device right now. */
    fun isAvailable(): Boolean

    /**
     * Suspends until the system biometric prompt resolves.
     * Returns a [BiometricResult]; never throws for expected outcomes.
     */
    suspend fun authenticate(
        title: String,
        subtitle: String,
        description: String
    ): BiometricResult
}

