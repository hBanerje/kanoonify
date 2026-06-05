package com.multiplatform.kanoonify.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val KanoonifyColorScheme = lightColorScheme(
    primary             = KPrimary,
    onPrimary           = KOnPrimary,
    primaryContainer    = KPrimary.copy(alpha = 0.10f),
    onPrimaryContainer  = KPrimary,
    secondary           = KSecondary,
    onSecondary         = KOnSecondary,
    secondaryContainer  = KSecondary.copy(alpha = 0.10f),
    onSecondaryContainer = KSecondary,
    tertiary            = KAccent,
    onTertiary          = KOnAccent,
    tertiaryContainer   = KAccent.copy(alpha = 0.18f),
    onTertiaryContainer = KOnAccent,
    background          = KBackground,
    onBackground        = KOnBackground,
    surface             = KSurface,
    onSurface           = KOnSurface,
    surfaceVariant      = KBackground,
    onSurfaceVariant    = KOnSurfaceMuted,
    outline             = KOutline,
    outlineVariant      = KDivider,
    error               = KError,
    onError             = KOnError,
    errorContainer      = KTagJailBg,
    onErrorContainer    = KTagJailFg
)

@Composable
fun KanoonifyTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = KanoonifyColorScheme,
        typography  = KanoonifyTypography,
        content     = content
    )
}
