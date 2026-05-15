package com.multiplatform.kanoonify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.multiplatform.kanoonify.data.local.appContext
import com.multiplatform.kanoonify.db.DatabaseDriverFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appContext = applicationContext

        setContent {
            App(driverFactory = DatabaseDriverFactory(applicationContext))
        }
    }
}