package com.multiplatform.kanoonify.data.auth

import com.multiplatform.kanoonify.domain.auth.BiometricAuthenticator

/**
 * Data-layer marker interface for biometric authentication.
 *
 * Currently extends [BiometricAuthenticator] verbatim — kept as a separate
 * interface so swap-in alternatives (e.g. PIN-only repo, mock for tests)
 * remain a one-line change in the DI graph.
 */
interface BiometricRepository : BiometricAuthenticator

