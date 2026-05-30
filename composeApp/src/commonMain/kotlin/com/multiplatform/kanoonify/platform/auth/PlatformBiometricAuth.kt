package com.multiplatform.kanoonify.platform.auth

import com.multiplatform.kanoonify.domain.auth.BiometricResult

/**
 * Platform façade over the OS biometric APIs.
 *
 *  - Android: backed by `androidx.biometric.BiometricPrompt`.
 *  - iOS:     backed by `LAContext` (LocalAuthentication.framework).
 *
 * This type is intentionally minimal and side-effect-free at construction
 * time so it can be created in shared code without leaking platform deps.
 */
expect class PlatformBiometricAuth() {

    /** Quick capability probe — does NOT show any UI. */
    fun isBiometricAvailable(): Boolean

    /**
     * Shows the system biometric prompt and suspends until the user
     * succeeds, cancels, fails, or the system reports an error.
     *
     * @param title       prompt title (e.g. "Secure Consultation")
     * @param subtitle    secondary line (e.g. "Verify it's you")
     * @param description longer description shown in the sheet
     */
    suspend fun authenticate(
        title: String,
        subtitle: String,
        description: String
    ): BiometricResult
}

