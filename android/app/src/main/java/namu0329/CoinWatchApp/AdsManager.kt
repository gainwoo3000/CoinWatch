package namu0329.coinwatchapp

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.unity3d.mediation.LevelPlay
import com.unity3d.mediation.LevelPlayConfiguration
import com.unity3d.mediation.LevelPlayInitError
import com.unity3d.mediation.LevelPlayInitListener
import com.unity3d.mediation.LevelPlayInitRequest

object AdsManager {
    const val APP_KEY = "28174640d"
    const val BANNER_AD_UNIT_ID = "e65tmpxgxvgoq4j3"

    /**
     * Real devices that should always receive clearly-labeled TEST ads instead of live ones.
     * AdMob logs each device's identifier the first time it requests an ad — paste it here so
     * testing on that device never counts as real (potentially invalid) traffic. Emulators are
     * always treated as test devices automatically, no entry needed.
     */
    private val testDeviceIds: List<String> = emptyList()

    private var isInitialized = false

    fun initialize(context: Context, onComplete: () -> Unit) {
        if (isInitialized) {
            onComplete()
            return
        }

        if (BuildConfig.DEBUG) {
            MobileAds.setRequestConfiguration(
                RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
            )
        }

        val initRequest = LevelPlayInitRequest.Builder(APP_KEY).build()
        LevelPlay.init(context.applicationContext, initRequest, object : LevelPlayInitListener {
            override fun onInitSuccess(configuration: LevelPlayConfiguration) {
                isInitialized = true
                onComplete()
            }

            override fun onInitFailed(error: LevelPlayInitError) {
                Log.e("AdsManager", "LevelPlay init failed: $error")
                onComplete()
            }
        })
    }

    /**
     * Launches ironSource's official Test Suite, which previews mediated ad creatives in a
     * sandboxed way — use this instead of tapping the live embedded banner while developing.
     */
    fun launchTestSuite(context: Context) {
        LevelPlay.launchTestSuite(context)
    }
}
