package com.multiplatform.kanoonify.platform.auth

import androidx.fragment.app.FragmentActivity

/**
 * Process-wide holder for the currently-resumed [FragmentActivity].
 *
 * The biometric prompt is bound to an Activity lifecycle, so we need a
 * reference at the moment we trigger it. The reference is set / cleared
 * from [com.multiplatform.kanoonify.MainActivity] in `onResume` / `onPause`
 * so it never outlives a foregrounded Activity (no leak).
 */
object BiometricActivityHolder {
    @Volatile
    var activity: FragmentActivity? = null
}

