package com.multiplatform.kanoonify.presentation.screens.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashViewModel {

    private val _state = MutableStateFlow(SplashState())
    val state: StateFlow<SplashState> = _state

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    init { start() }

    private fun start() {
        scope.launch {

            delay(SPLASH_DWELL_MS)
            _state.update { it.copy(navigateToLanding = true) }
        }
    }

    fun onNavigationHandled() {
        _state.update { it.copy(navigateToLanding = false) }
    }

    private companion object {
        const val SPLASH_DWELL_MS = 1700L
    }
}
