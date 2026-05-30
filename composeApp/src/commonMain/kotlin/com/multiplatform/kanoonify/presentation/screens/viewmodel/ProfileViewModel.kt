package com.multiplatform.kanoonify.presentation.screens.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/**
 * Drives the Profile screen. UI orchestrates only — repositories will be
 * injected later (user repo for [ProfileUser], analytics for [ProfileStats],
 * settings store for [ProfilePreferences]).
 */
class ProfileViewModel {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state

    /* ---------------------- preference intents ----------------------------- */

    fun onToggleNotifications(value: Boolean) = updatePrefs { it.copy(notificationsEnabled = value) }
    fun onToggleBiometricLock(value: Boolean) = updatePrefs { it.copy(biometricLockEnabled = value) }
    fun onToggleAppLock(value: Boolean)       = updatePrefs { it.copy(appLockEnabled = value) }
    fun onToggleFaceId(value: Boolean)        = updatePrefs { it.copy(faceIdEnabled = value) }
    fun onToggleSecureDocuments(value: Boolean) = updatePrefs { it.copy(secureDocumentsEnabled = value) }

    fun setLanguage(label: String) = updatePrefs { it.copy(language = label) }
    fun setTheme(label: String)    = updatePrefs { it.copy(themeLabel = label) }

    /* ---------------------- placeholder navigation hooks ------------------- */

    fun onPrivacyPolicyClick() { /* hook for navigation */ }
    fun onTermsClick()         { /* hook for navigation */ }
    fun onContactSupportClick(){ /* hook for navigation */ }
    fun onAboutClick()         { /* hook for navigation */ }
    fun onEditProfileClick()   { /* hook for navigation */ }
    fun onPremiumCtaClick()    { /* hook for waitlist */ }

    private inline fun updatePrefs(transform: (ProfilePreferences) -> ProfilePreferences) {
        _state.update { it.copy(preferences = transform(it.preferences)) }
    }
}

