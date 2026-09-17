package com.example.util

import android.content.Context

/**
 * Architecture placeholder for Google AdMob monetization.
 * As per specification, ads are disabled in V1 / initial development build.
 * When ready to publish with AdMob, set [ADS_ENABLED] to true and configure Ad Unit IDs.
 */
object AdMobManager {
    const val ADS_ENABLED = false

    // Placeholder AdMob App ID & Unit IDs for future integration
    const val ADMOB_APP_ID = "ca-app-pub-3940256099942544~3347511713" // Google Test App ID
    const val BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
    const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"

    fun initialize(context: Context) {
        if (!ADS_ENABLED) return
        // AdMob initialization will be executed here once SDK is enabled
    }

    fun showInterstitial(context: Context, onDismissed: () -> Unit) {
        if (!ADS_ENABLED) {
            onDismissed()
            return
        }
        // Show interstitial ad logic
        onDismissed()
    }
}
