package com.multiplatform.kanoonify.domain.auth

sealed class BiometricResult {

    data object Success : BiometricResult()

    data object Failed : BiometricResult()

    data object Cancelled : BiometricResult()

    data object NotAvailable : BiometricResult()

    data class Error(val message: String) : BiometricResult()
}
