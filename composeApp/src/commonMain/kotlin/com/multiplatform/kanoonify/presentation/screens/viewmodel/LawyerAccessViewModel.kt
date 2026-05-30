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

/**
 * ViewModel that gates access to a secure feature (currently the lawyer
 * chat) behind a biometric / device-credential check.
 *
 * The VM is feature-agnostic — only the navigation effect on
 * `authenticationSuccess` ties it to a destination. Reuse for evidence
 * vault, AI history, secure drafts, etc. by instantiating one per gated
 * route.
 *
 * Security:
 *  - State lives only in this VM instance; it is NOT persisted to disk,
 *    a shared singleton, or saved-state. Leaving the screen disposes the VM
 *    via [dispose] and the next entry forces re-authentication.
 *  - No biometric template is ever stored or exposed; only an opaque
 *    success/failure [BiometricResult] is consumed.
 */
class LawyerAccessViewModel(
    private val repository: BiometricRepository = BiometricRepositoryImpl(),
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
) {

    private val _state = MutableStateFlow(
        LawyerAccessState(biometricAvailable = repository.isAvailable())
    )
    val state: StateFlow<LawyerAccessState> = _state.asStateFlow()

    private var authJob: Job? = null

    /**
     * Re-probes capability — call when returning to the screen so a user
     * who just enrolled biometrics in Settings sees the gate enabled.
     */
    fun refreshAvailability() {
        _state.update { it.copy(biometricAvailable = repository.isAvailable()) }
    }

    /**
     * Triggers the system biometric prompt.
     *
     * @param title       short prompt title
     * @param subtitle    secondary line
     * @param description longer description shown in the sheet
     */
    fun requestAuthentication(
        title: String,
        subtitle: String,
        description: String
    ) {
        if (_state.value.isAuthenticating) return

        // Re-probe — availability can change between screens.
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

    /** Consume the one-shot navigation event after the UI has acted on it. */
    fun consumeAuthenticationSuccess() {
        _state.update { it.copy(authenticationSuccess = false) }
    }

    /** Consume the one-shot snackbar error after the UI has shown it. */
    fun consumeError() {
        _state.update { it.copy(errorMessage = null) }
    }

    /**
     * Cancel any in-flight prompt and wipe state. The nav graph calls this
     * when the gated screen leaves composition so authentication state
     * never crosses screen boundaries.
     */
    fun dispose() {
        authJob?.cancel()
        scope.coroutineContext[Job]?.cancel()
        _state.value = LawyerAccessState(biometricAvailable = repository.isAvailable())
    }
}

