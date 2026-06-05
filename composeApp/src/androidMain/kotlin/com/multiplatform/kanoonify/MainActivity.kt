package com.multiplatform.kanoonify

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import com.multiplatform.kanoonify.data.local.appContext
import com.multiplatform.kanoonify.db.DatabaseDriverFactory
import com.multiplatform.kanoonify.platform.auth.BiometricActivityHolder

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appContext = applicationContext

        setContent {
            App(driverFactory = DatabaseDriverFactory(applicationContext))
        }
    }

    override fun onResume() {
        super.onResume()
        BiometricActivityHolder.activity = this
    }

    override fun onPause() {

        if (BiometricActivityHolder.activity === this) {
            BiometricActivityHolder.activity = null
        }
        super.onPause()
    }
}
