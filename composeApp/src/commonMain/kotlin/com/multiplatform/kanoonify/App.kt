package com.multiplatform.kanoonify

import androidx.compose.runtime.Composable
import com.multiplatform.kanoonify.db.DatabaseDriverFactory
import com.multiplatform.kanoonify.presentation.screens.navigation.KanoonifyRoot
import com.multiplatform.kanoonify.presentation.theme.KanoonifyTheme

@Composable
fun App(driverFactory: DatabaseDriverFactory) {
    KanoonifyTheme {
        KanoonifyRoot(driverFactory)
    }
}
