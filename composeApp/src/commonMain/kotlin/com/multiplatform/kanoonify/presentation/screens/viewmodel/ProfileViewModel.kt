package com.multiplatform.kanoonify.presentation.screens.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ProfileViewModel {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state

    fun onToggleNotifications(value: Boolean) = updatePrefs { it.copy(notificationsEnabled = value) }
    fun onToggleBiometricLock(value: Boolean) = updatePrefs { it.copy(biometricLockEnabled = value) }
    fun onToggleAppLock(value: Boolean)       = updatePrefs { it.copy(appLockEnabled = value) }
    fun onToggleFaceId(value: Boolean)        = updatePrefs { it.copy(faceIdEnabled = value) }
    fun onToggleSecureDocuments(value: Boolean) = updatePrefs { it.copy(secureDocumentsEnabled = value) }

    fun setLanguage(label: String) = updatePrefs { it.copy(language = label) }
    fun setTheme(label: String)    = updatePrefs { it.copy(themeLabel = label) }

    fun onPrivacyPolicyClick() {  }
    fun onTermsClick()         {  }
    fun onContactSupportClick(){  }
    fun onAboutClick()         {  }
    fun onEditProfileClick()   {  }
    fun onPremiumCtaClick()    {  }

    private inline fun updatePrefs(transform: (ProfilePreferences) -> ProfilePreferences) {
        _state.update { it.copy(preferences = transform(it.preferences)) }
    }
}
