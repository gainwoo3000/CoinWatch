//
//  LevelPlayBannerView.swift
//  CoinWatch
//

import SwiftUI
import IronSource

struct LevelPlayBannerView: UIViewRepresentable {
    func makeCoordinator() -> Coordinator {
        Coordinator()
    }

    func makeUIView(context: Context) -> LPMBannerAdView {
        let config = LPMBannerAdViewConfigBuilder()
            .set(adSize: .banner())
            .build()
        let bannerView = LPMBannerAdView(adUnitId: AdsManager.bannerAdUnitId, config: config)
        bannerView.setDelegate(context.coordinator)
        if let rootViewController = AdsManager.rootViewController() {
            bannerView.loadAd(with: rootViewController)
        }
        return bannerView
    }

    func updateUIView(_ uiView: LPMBannerAdView, context: Context) {}

    final class Coordinator: NSObject, LPMBannerAdViewDelegate {
        func didLoadAd(with adInfo: LPMAdInfo) {}

        func didFailToLoadAd(withAdUnitId adUnitId: String, error: Error) {
            print("LevelPlay banner failed to load: \(error.localizedDescription)")
        }
    }
}
