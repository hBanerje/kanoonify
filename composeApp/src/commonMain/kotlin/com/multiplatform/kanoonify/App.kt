package com.multiplatform.kanoonify

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.multiplatform.kanoonify.db.DatabaseDriverFactory
import com.multiplatform.kanoonify.presentation.screens.navigation.KanoonifyRoot

@Composable
fun App(driverFactory: DatabaseDriverFactory) {
    MaterialTheme {
        KanoonifyRoot(driverFactory)
    }
}