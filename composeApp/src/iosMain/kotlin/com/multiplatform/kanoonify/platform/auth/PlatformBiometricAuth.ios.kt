package com.multiplatform.kanoonify.platform.auth

import com.multiplatform.kanoonify.domain.auth.BiometricResult
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import platform.LocalAuthentication.LAContext
import platform.LocalAuthentication.LAErrorAuthenticationFailed
import platform.LocalAuthentication.LAErrorBiometryNotAvailable
import platform.LocalAuthentication.LAErrorBiometryNotEnrolled
import platform.LocalAuthentication.LAErrorPasscodeNotSet
import platform.LocalAuthentication.LAErrorSystemCancel
import platform.LocalAuthentication.LAErrorUserCancel
import platform.LocalAuthentication.LAErrorUserFallback
import platform.LocalAuthentication.LAPolicyDeviceOwnerAuthentication
import kotlin.coroutines.resume

/**
 * iOS implementation of [PlatformBiometricAuth] using LocalAuthentication.
 *
 * Uses `LAPolicyDeviceOwnerAuthentication`, which allows:
 *  - Face ID
 *  - Touch ID
 *  - Device passcode fallback
 *
 * A fresh [LAContext] is constructed per call so previous authentication
 * state never leaks across requests (security requirement).
 */
@OptIn(ExperimentalForeignApi::class)
actual class PlatformBiometricAuth actual constructor() {

    actual fun isBiometricAvailable(): Boolean {
        val context = LAContext()
        return context.canEvaluatePolicy(LAPolicyDeviceOwnerAuthentication, null)
    }

    actual suspend fun authenticate(
        title: String,
        subtitle: String,
        description: String
    ): BiometricResult = withContext(Dispatchers.Main) {
        val context = LAContext()

        if (!context.canEvaluatePolicy(LAPolicyDeviceOwnerAuthentication, null)) {
            // Upcast so the surrounding lambda is inferred as BiometricResult,
            // not the specific NotAvailable subtype.
            return@withContext BiometricResult.NotAvailable as BiometricResult
        }

        // Localised reason shown beneath the system prompt.
        val reason = listOf(title, subtitle, description)
            .filter { it.isNotBlank() }
            .joinToString(separator = " — ")
            .ifBlank { "Authenticate to continue" }

        // Explicit type parameter pins `cont` to `Continuation<BiometricResult>`
        // so resume(Success) / resume(mapped) both type-check.
        suspendCancellableCoroutine<BiometricResult> { cont ->
            context.evaluatePolicy(
                policy = LAPolicyDeviceOwnerAuthentication,
                localizedReason = reason
            ) { success, error ->
                if (!cont.isActive) return@evaluatePolicy
                if (success) {
                    cont.resume(BiometricResult.Success)
                    return@evaluatePolicy
                }

                val code = error?.code?.toInt()
                val mapped: BiometricResult = when (code) {
                    LAErrorUserCancel.toInt(),
                    LAErrorSystemCancel.toInt(),
                    LAErrorUserFallback.toInt() -> BiometricResult.Cancelled

                    LAErrorAuthenticationFailed.toInt() -> BiometricResult.Failed

                    LAErrorBiometryNotAvailable.toInt(),
                    LAErrorBiometryNotEnrolled.toInt(),
                    LAErrorPasscodeNotSet.toInt() -> BiometricResult.NotAvailable

                    else -> BiometricResult.Error(
                        error?.localizedDescription ?: "Unknown authentication error"
                    )
                }
                cont.resume(mapped)
            }
        }
    }
}

