package com.multiplatform.kanoonify.platform.auth

import androidx.fragment.app.FragmentActivity

object BiometricActivityHolder {
    @Volatile
    var activity: FragmentActivity? = null
}
