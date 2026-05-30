package com.multiplatform.kanoonify.presentation.screens.viewmodel

/**
 * UI state for biometric-gated lawyer access.
 *
 *  - [isAuthenticating]      → show progress, disable CTA
 *  - [authenticationSuccess] → one-shot navigation trigger (consumed by UI)
 *  - [errorMessage]          → transient snackbar text (one-shot, must be cleared)
 *  - [biometricAvailable]    → drives "biometric unavailable" UX
 *
 * Designed for reuse beyond the lawyer flow (docs vault, AI history, drafts).
 */
data class LawyerAccessState(
    val isAuthenticating: Boolean = false,
    val authenticationSuccess: Boolean = false,
    val errorMessage: String? = null,
    val biometricAvailable: Boolean = true
)

