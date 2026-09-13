package namu0329.coinwindow

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.unity3d.mediation.LevelPlayAdError
import com.unity3d.mediation.LevelPlayAdInfo
import com.unity3d.mediation.banner.LevelPlayBannerAdView
import com.unity3d.mediation.banner.LevelPlayBannerAdViewListener

class MainActivity : AppCompatActivity() {
    private lateinit var bannerAdView: LevelPlayBannerAdView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView = findViewById<WebView>(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.loadUrl("https://namuapplication.cloud/coinwatch")

        bannerAdView = LevelPlayBannerAdView(this, AdsManager.BANNER_AD_UNIT_ID)
        bannerAdView.setBannerListener(object : LevelPlayBannerAdViewListener {
            override fun onAdLoaded(adInfo: LevelPlayAdInfo) {}

            override fun onAdLoadFailed(error: LevelPlayAdError) {
                Log.e(TAG, "LevelPlay banner failed to load: ${error.errorMessage}")
            }
        })
        findViewById<FrameLayout>(R.id.bannerContainer).addView(bannerAdView)

        AdsManager.initialize(this) {
            runOnUiThread { bannerAdView.loadAd() }
        }
    }

    override fun onDestroy() {
        bannerAdView.destroy()
        super.onDestroy()
    }

    private companion object {
        const val TAG = "MainActivity"
    }
}
