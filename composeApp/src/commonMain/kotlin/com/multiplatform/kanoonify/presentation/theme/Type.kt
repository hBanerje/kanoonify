package com.multiplatform.kanoonify.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val KanoonifyTypography: Typography = Typography(
    // Title
    headlineLarge = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold,    lineHeight = 40.sp, letterSpacing = (-0.5).sp),
    headlineMedium = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Bold,   lineHeight = 34.sp, letterSpacing = (-0.25).sp),
    headlineSmall  = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.SemiBold, lineHeight = 30.sp),

    // Section
    titleLarge   = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold, lineHeight = 26.sp),
    titleMedium  = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.SemiBold, lineHeight = 24.sp),
    titleSmall   = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Medium,    lineHeight = 22.sp),

    // Body
    bodyLarge    = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal, lineHeight = 24.sp),
    bodyMedium   = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal, lineHeight = 22.sp),
    bodySmall    = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Normal, lineHeight = 20.sp),

    // Caption / labels
    labelLarge   = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold, lineHeight = 20.sp, letterSpacing = 0.1.sp),
    labelMedium  = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold, lineHeight = 16.sp, letterSpacing = 0.4.sp),
    labelSmall   = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Medium,   lineHeight = 14.sp, letterSpacing = 0.5.sp)
)
