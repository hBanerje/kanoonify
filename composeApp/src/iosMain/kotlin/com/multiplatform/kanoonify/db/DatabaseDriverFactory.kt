package com.multiplatform.kanoonify.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = KanoonifyDatabase.Schema,
            name = "kanoonify.db"
        )
    }
}