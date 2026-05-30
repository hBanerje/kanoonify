package com.multiplatform.kanoonify.platform.auth

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.multiplatform.kanoonify.domain.auth.BiometricResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

/**
 * Android implementation of [PlatformBiometricAuth] using AndroidX BiometricPrompt.
 *
 * Strategy:
 *  - Try BIOMETRIC_STRONG | BIOMETRIC_WEAK | DEVICE_CREDENTIAL (PIN/pattern fallback).
 *  - If the device only supports weaker auth, downgrade gracefully.
 *  - The Activity reference is read from [BiometricActivityHolder] at call-time
 *    so it always matches the currently-foregrounded screen and we never hold
 *    a stale reference.
 */
actual class PlatformBiometricAuth actual constructor() {

    private val allowedAuthenticators: Int =
        BIOMETRIC_STRONG or BIOMETRIC_WEAK or DEVICE_CREDENTIAL

    actual fun isBiometricAvailable(): Boolean {
        val activity = BiometricActivityHolder.activity ?: return false
        val manager = BiometricManager.from(activity)
        return when (manager.canAuthenticate(allowedAuthenticators)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            else -> false
        }
    }

    actual suspend fun authenticate(
        title: String,
        subtitle: String,
        description: String
    ): BiometricResult {
        val activity = BiometricActivityHolder.activity
            ?: return BiometricResult.Error("No active Activity for biometric prompt")

        // BiometricPrompt must be built on the main thread.
        return withContext(Dispatchers.Main) {
            suspendCancellableCoroutine { cont ->
                val executor = ContextCompat.getMainExecutor(activity)

                val callback = object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        if (cont.isActive) cont.resume(BiometricResult.Success)
                    }

                    override fun onAuthenticationFailed() {
                        // Not terminal — system gives the user another try.
                        // We do NOT resume here.
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        if (!cont.isActive) return
                        val mapped = when (errorCode) {
                            BiometricPrompt.ERROR_USER_CANCELED,
                            BiometricPrompt.ERROR_NEGATIVE_BUTTON,
                            BiometricPrompt.ERROR_CANCELED -> BiometricResult.Cancelled

                            BiometricPrompt.ERROR_NO_BIOMETRICS,
                            BiometricPrompt.ERROR_HW_NOT_PRESENT,
                            BiometricPrompt.ERROR_HW_UNAVAILABLE,
                            BiometricPrompt.ERROR_NO_DEVICE_CREDENTIAL -> BiometricResult.NotAvailable

                            BiometricPrompt.ERROR_LOCKOUT,
                            BiometricPrompt.ERROR_LOCKOUT_PERMANENT -> BiometricResult.Failed

                            else -> BiometricResult.Error(errString.toString())
                        }
                        cont.resume(mapped)
                    }
                }

                val prompt = BiometricPrompt(activity, executor, callback)

                val promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle(title)
                    .setSubtitle(subtitle)
                    .setDescription(description)
                    .setAllowedAuthenticators(allowedAuthenticators)
                    // Negative button is not allowed when DEVICE_CREDENTIAL is set.
                    .build()

                try {
                    prompt.authenticate(promptInfo)
                } catch (t: Throwable) {
                    if (cont.isActive) {
                        cont.resume(BiometricResult.Error(t.message ?: "Unknown biometric error"))
                    }
                }

                cont.invokeOnCancellation {
                    runCatching { prompt.cancelAuthentication() }
                }
            }
        }
    }
}

