package com.multiplatform.kanoonify.domain.auth

/**
 * Platform-independent representation of a biometric authentication outcome.
 *
 * No platform types leak out of the data layer — UI and ViewModels reason
 * exclusively in terms of [BiometricResult].
 */
sealed class BiometricResult {
    /** Authentication completed successfully. */
    data object Success : BiometricResult()

    /** User attempted but failed (wrong fingerprint / face mismatch). */
    data object Failed : BiometricResult()

    /** User explicitly cancelled the prompt or chose negative button. */
    data object Cancelled : BiometricResult()

    /** Hardware is missing, disabled, or no biometrics are enrolled. */
    data object NotAvailable : BiometricResult()

    /** Any other unexpected error. */
    data class Error(val message: String) : BiometricResult()
}

