package com.multiplatform.kanoonify

import androidx.compose.ui.window.ComposeUIViewController
import com.multiplatform.kanoonify.db.DatabaseDriverFactory

fun MainViewController() = ComposeUIViewController {
    App(driverFactory = DatabaseDriverFactory())
}
