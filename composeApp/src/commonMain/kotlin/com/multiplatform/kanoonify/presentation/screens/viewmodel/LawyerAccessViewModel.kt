package com.multiplatform.kanoonify.presentation.screens.viewmodel

import com.multiplatform.kanoonify.data.auth.BiometricRepository
import com.multiplatform.kanoonify.data.auth.BiometricRepositoryImpl
import com.multiplatform.kanoonify.domain.auth.BiometricResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LawyerAccessViewModel(
    private val repository: BiometricRepository = BiometricRepositoryImpl(),
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
) {

    private val _state = MutableStateFlow(
        LawyerAccessState(biometricAvailable = repository.isAvailable())
    )
    val state: StateFlow<LawyerAccessState> = _state.asStateFlow()

    private var authJob: Job? = null

    fun refreshAvailability() {
        _state.update { it.copy(biometricAvailable = repository.isAvailable()) }
    }

    fun requestAuthentication(
        title: String,
        subtitle: String,
        description: String
    ) {
        if (_state.value.isAuthenticating) return

        val available = repository.isAvailable()
        if (!available) {
            _state.update {
                it.copy(
                    biometricAvailable = false,
                    errorMessage = "Biometric authentication is not available on this device."
                )
            }
            return
        }

        authJob?.cancel()
        _state.update {
            it.copy(
                isAuthenticating = true,
                errorMessage = null,
                authenticationSuccess = false,
                biometricAvailable = true
            )
        }
        authJob = scope.launch {
            val result = repository.authenticate(title, subtitle, description)
            _state.update { current ->
                when (result) {
                    BiometricResult.Success -> current.copy(
                        isAuthenticating = false,
                        authenticationSuccess = true,
                        errorMessage = null
                    )
                    BiometricResult.Failed -> current.copy(
                        isAuthenticating = false,
                        errorMessage = "Authentication failed. Please try again."
                    )
                    BiometricResult.Cancelled -> current.copy(
                        isAuthenticating = false,
                        errorMessage = null
                    )
                    BiometricResult.NotAvailable -> current.copy(
                        isAuthenticating = false,
                        biometricAvailable = false,
                        errorMessage = "No biometric or device credential is enrolled."
                    )
                    is BiometricResult.Error -> current.copy(
                        isAuthenticating = false,
                        errorMessage = result.message
                    )
                }
            }
        }
    }

    fun consumeAuthenticationSuccess() {
        _state.update { it.copy(authenticationSuccess = false) }
    }

    fun consumeError() {
        _state.update { it.copy(errorMessage = null) }
    }

    fun dispose() {
        authJob?.cancel()
        scope.coroutineContext[Job]?.cancel()
        _state.value = LawyerAccessState(biometricAvailable = repository.isAvailable())
    }
}
