package com.multiplatform.kanoonify.db

class DatabaseHelper(
    driverFactory: DatabaseDriverFactory
) {
    val database: KanoonifyDatabase =
        KanoonifyDatabase(driverFactory.createDriver())
}