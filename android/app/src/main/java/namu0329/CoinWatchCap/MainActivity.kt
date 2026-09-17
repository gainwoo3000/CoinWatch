package namu0329.coinwatchcap

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.unity3d.mediation.LevelPlayAdError
import com.unity3d.mediation.LevelPlayAdInfo
import com.unity3d.mediation.LevelPlayAdSize
import com.unity3d.mediation.banner.LevelPlayBannerAdView
import com.unity3d.mediation.banner.LevelPlayBannerAdViewListener

class MainActivity : AppCompatActivity() {
    private lateinit var bannerAdView: LevelPlayBannerAdView
    private lateinit var adStatusText: TextView

    private fun showAdStatus(message: String) {
        runOnUiThread {
            adStatusText.text = message
            adStatusText.visibility = android.view.View.VISIBLE
        }
    }

    private fun hideAdStatus() {
        runOnUiThread {
            adStatusText.visibility = android.view.View.GONE
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rootLayout = findViewById<LinearLayout>(R.id.rootLayout)
        ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = systemBars.top, bottom = systemBars.bottom)
            insets
        }

        adStatusText = findViewById(R.id.adStatusText)
        showAdStatus("광고 준비 중...")

        val webView = findViewById<WebView>(R.id.webView)
        webView.setBackgroundColor(ContextCompat.getColor(this, R.color.app_background))
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.loadUrl("https://namuapplication.cloud/coinwatch")

        val bannerConfig = LevelPlayBannerAdView.Config.Builder()
            .setAdSize(LevelPlayAdSize.BANNER)
            .build()
        Log.d(TAG, "[광고] 배너 뷰 생성 (adUnitId=${AdsManager.BANNER_AD_UNIT_ID})")
        bannerAdView = LevelPlayBannerAdView(this, AdsManager.BANNER_AD_UNIT_ID, bannerConfig)
        bannerAdView.setBannerListener(object : LevelPlayBannerAdViewListener {
            override fun onAdLoaded(adInfo: LevelPlayAdInfo) {
                Log.d(TAG, "[광고] 광고 로드 성공 (adNetwork=${adInfo.adNetwork}, adUnitId=${adInfo.adUnitId})")
                hideAdStatus()
            }

            override fun onAdLoadFailed(error: LevelPlayAdError) {
                Log.e(
                    TAG,
                    "[광고] 광고 로드 실패 (errorCode=${error.errorCode}, message=${error.errorMessage}, adUnitId=${error.adUnitId})"
                )
                showAdStatus("광고 로드 실패 (${error.errorCode}): ${error.errorMessage}")
            }

            override fun onAdDisplayed(adInfo: LevelPlayAdInfo) {
                Log.d(TAG, "[광고] 광고 화면에 표시됨")
                hideAdStatus()
            }

            override fun onAdDisplayFailed(adInfo: LevelPlayAdInfo, error: LevelPlayAdError) {
                Log.e(TAG, "[광고] 광고 표시 실패 (errorCode=${error.errorCode}, message=${error.errorMessage})")
                showAdStatus("광고 표시 실패 (${error.errorCode}): ${error.errorMessage}")
            }

            override fun onAdClicked(adInfo: LevelPlayAdInfo) {
                Log.d(TAG, "[광고] 광고 클릭됨")
            }
        })
        val density = resources.displayMetrics.density
        val bannerLayoutParams = FrameLayout.LayoutParams(
            (LevelPlayAdSize.BANNER.width * density).toInt(),
            (LevelPlayAdSize.BANNER.height * density).toInt()
        ).apply { gravity = Gravity.CENTER_HORIZONTAL }
        findViewById<FrameLayout>(R.id.bannerContainer).addView(bannerAdView, bannerLayoutParams)

        Log.d(TAG, "[광고] SDK 초기화 시작 (appKey=${AdsManager.APP_KEY})")
        showAdStatus("광고 SDK 초기화 중...")
        AdsManager.initialize(this) { success ->
            if (success) {
                Log.d(TAG, "[광고] SDK 초기화 성공, 배너 로드 요청")
                showAdStatus("광고 로드 중...")
                runOnUiThread { bannerAdView.loadAd() }
            } else {
                Log.e(TAG, "[광고] SDK 초기화 실패 — 배너 로드를 건너뜀")
                showAdStatus("SDK 로드 실패")
            }
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
