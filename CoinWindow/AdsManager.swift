//
//  AdsManager.swift
//  CoinWindow
//

import AppTrackingTransparency
import Foundation
import GoogleMobileAds
import IronSource
import UIKit

enum AdsManager {
    static let appKey = "2816fe56d"
    static let bannerAdUnitId = "kt8tkxm09mlbkzud"

    /// Real devices that should always receive clearly-labeled TEST ads instead of live ones.
    /// AdMob prints each device's identifier to the console the first time it requests an ad —
    /// paste it here so testing on that device never counts as real (potentially invalid) traffic.
    /// Simulators are always treated as test devices automatically, no entry needed.
    static let testDeviceIdentifiers: [String] = []

    private static var isInitialized = false

    /// Requests App Tracking Transparency permission. Must be shown before the ad SDKs are
    /// initialized so mediated networks know whether they may use the device's IDFA.
    static func requestTrackingAuthorization(completion: @escaping () -> Void) {
        guard ATTrackingManager.trackingAuthorizationStatus == .notDetermined else {
            completion()
            return
        }
        ATTrackingManager.requestTrackingAuthorization { _ in
            DispatchQueue.main.async {
                completion()
            }
        }
    }

    static func initialize(completion: @escaping () -> Void) {
        guard !isInitialized else {
            completion()
            return
        }

        #if DEBUG
        MobileAds.shared.requestConfiguration.testDeviceIdentifiers = testDeviceIdentifiers
        #endif

        let initRequest = LPMInitRequestBuilder(appKey: appKey).build()
        LevelPlay.initWith(initRequest) { _, error in
            if let error {
                print("LevelPlay init failed: \(error.localizedDescription)")
            } else {
                isInitialized = true
            }
            DispatchQueue.main.async {
                completion()
            }
        }
    }

    static func rootViewController() -> UIViewController? {
        UIApplication.shared.connectedScenes
            .compactMap { $0 as? UIWindowScene }
            .flatMap { $0.windows }
            .first { $0.isKeyWindow }?
            .rootViewController
    }

    #if DEBUG
    /// Launches ironSource's official Test Suite, which previews mediated ad creatives in a
    /// sandboxed way — use this instead of tapping the live embedded banner while developing.
    static func launchTestSuite() {
        guard let viewController = rootViewController() else { return }
        LevelPlay.launchTestSuite(viewController)
    }
    #endif
}
