package com.multiplatform.kanoonify.presentation.screens.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Drives the splash screen lifecycle.
 *
 *  On creation, waits long enough for the entrance animation (~1700 ms),
 *    then flips [SplashState.navigateToLanding] to true.
 *  The UI observes the state and triggers navigation when it becomes true.
 */
class SplashViewModel {

    private val _state = MutableStateFlow(SplashState())
    val state: StateFlow<SplashState> = _state

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    init { start() }

    private fun start() {
        scope.launch {
            // Animation total ~1200ms + small linger so users perceive completion.
            delay(SPLASH_DWELL_MS)
            _state.update { it.copy(navigateToLanding = true) }
        }
    }

    /** UI must call this once it has consumed the navigation event. */
    fun onNavigationHandled() {
        _state.update { it.copy(navigateToLanding = false) }
    }

    private companion object {
        const val SPLASH_DWELL_MS = 1700L
    }
}

