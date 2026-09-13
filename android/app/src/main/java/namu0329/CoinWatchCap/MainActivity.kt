package namu0329.coinwatchcap

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.unity3d.mediation.LevelPlayAdError
import com.unity3d.mediation.LevelPlayAdInfo
import com.unity3d.mediation.LevelPlayAdSize
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

        val bannerConfig = LevelPlayBannerAdView.Config.Builder()
            .setAdSize(LevelPlayAdSize.BANNER)
            .build()
        bannerAdView = LevelPlayBannerAdView(this, AdsManager.BANNER_AD_UNIT_ID, bannerConfig)
        bannerAdView.setBannerListener(object : LevelPlayBannerAdViewListener {
            override fun onAdLoaded(adInfo: LevelPlayAdInfo) {}

            override fun onAdLoadFailed(error: LevelPlayAdError) {
                Log.e(TAG, "LevelPlay banner failed to load: ${error.errorMessage}")
            }
        })
        val density = resources.displayMetrics.density
        val bannerLayoutParams = FrameLayout.LayoutParams(
            (LevelPlayAdSize.BANNER.width * density).toInt(),
            (LevelPlayAdSize.BANNER.height * density).toInt()
        ).apply { gravity = Gravity.CENTER_HORIZONTAL }
        findViewById<FrameLayout>(R.id.bannerContainer).addView(bannerAdView, bannerLayoutParams)

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
