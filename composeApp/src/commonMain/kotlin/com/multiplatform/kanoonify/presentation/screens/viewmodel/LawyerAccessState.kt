package com.multiplatform.kanoonify.presentation.screens.viewmodel

data class LawyerAccessState(
    val isAuthenticating: Boolean = false,
    val authenticationSuccess: Boolean = false,
    val errorMessage: String? = null,
    val biometricAvailable: Boolean = true
)
