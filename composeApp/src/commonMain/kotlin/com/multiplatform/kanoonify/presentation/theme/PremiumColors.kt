package com.multiplatform.kanoonify.presentation.theme

import androidx.compose.ui.graphics.Color

/**
 * Cinematic dark palette used by the premium LandingScreen.
 *
 * Intentionally *not* wired into [KanoonifyTheme] — the rest of the app
 * remains on the light scheme. The Landing surface paints its own dark
 * gradient and references these tokens directly so the global theme is
 * untouched and other screens are unaffected.
 */
object KanoonifyPremiumColors {

    /* ---- Backgrounds ---- */
    val BgDeep        = Color(0xFF050816)
    val BgMid         = Color(0xFF091428)
    val BgSoft        = Color(0xFF0F172A)

    /* ---- Surfaces / glass ---- */
    val GlassFill     = Color(0xFFFFFFFF).copy(alpha = 0.06f)
    val GlassFillHi   = Color(0xFFFFFFFF).copy(alpha = 0.10f)
    val GlassStroke   = Color(0xFFFFFFFF).copy(alpha = 0.14f)
    val GlassStrokeHi = Color(0xFFFFFFFF).copy(alpha = 0.22f)

    /* ---- Neon accents ---- */
    val NeonBlue      = Color(0xFF4DA3FF)
    val NeonIndigo    = Color(0xFF6A6CF6)
    val NeonViolet    = Color(0xFF8B5CF6)
    val NeonCyan      = Color(0xFF22D3EE)

    /* ---- Premium gold ---- */
    val GoldLight     = Color(0xFFFFE08A)
    val GoldMid       = Color(0xFFF2C94C)
    val GoldDeep      = Color(0xFFCBA135)

    /* ---- Feature accents ---- */
    val AccentLaws        = Color(0xFF4DA3FF)   // Blue
    val AccentConstitution = Color(0xFF34D399)  // Green
    val AccentLawyer      = Color(0xFF8B5CF6)   // Purple
    val AccentEmergency   = Color(0xFFFF6B6B)   // Red/Orange

    /* ---- Semantic text on dark ---- */
    val TextHi        = Color(0xFFF8FAFC)
    val TextMid       = Color(0xFFCBD5E1)
    val TextLow       = Color(0xFF94A3B8)
    val TextMuted     = Color(0xFF64748B)

    /* ---- Emergency / alert ---- */
    val AlertRed      = Color(0xFFFF4D6D)
    val AlertOrange   = Color(0xFFFF8A3D)
}

