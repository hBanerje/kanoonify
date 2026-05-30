package com.multiplatform.kanoonify.presentation.screens.viewmodel

/** Stats summary surfaced on the Profile screen. */
data class ProfileStats(
    val searches: Int = 0,
    val savedItems: Int = 0,
    val constitutionReads: Int = 0,
    val consultations: Int = 0
)

/** User-facing settings toggles (UI mirror of preferences). */
data class ProfilePreferences(
    val notificationsEnabled: Boolean = true,
    val biometricLockEnabled: Boolean = false,
    val appLockEnabled: Boolean = false,
    val secureDocumentsEnabled: Boolean = true,
    val faceIdEnabled: Boolean = true,
    val language: String = "English (India)",
    val themeLabel: String = "System default"
)

/**
 * Lightweight user identity. Replace with [com.multiplatform.kanoonify.domain.auth]
 * model once the auth pipeline produces one — the screen contract stays the same.
 */
data class ProfileUser(
    val name: String = "Hrithik Banerjee",
    val memberSinceLabel: String = "May 2026",
    val isPremium: Boolean = false
)

data class ProfileState(
    val user: ProfileUser = ProfileUser(),
    val stats: ProfileStats = ProfileStats(
        searches = 128,
        savedItems = 14,
        constitutionReads = 37,
        consultations = 3
    ),
    val preferences: ProfilePreferences = ProfilePreferences(),
    val isLoading: Boolean = false
)

