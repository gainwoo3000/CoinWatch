//
//  ContentView.swift
//  CryptoWindow
//
//  Created by macOS on 9/12/26.
//

import SwiftUI

struct ContentView: View {
    private let pageURL = URL(string: "https://namuapplication.cloud/coinwatch")!
    @State private var isLoading = false
    @State private var isAdsReady = false

    var body: some View {
        VStack(spacing: 0) {
            ZStack {
                WebView(url: pageURL, isLoading: $isLoading)
                if isLoading {
                    ProgressView()
                }
            }
            .frame(maxHeight: .infinity)

            if isAdsReady {
                LevelPlayBannerView()
                    .frame(height: 50)
                    .overlay(alignment: .topTrailing) {
                        #if DEBUG
                        Button("Ad Test Suite") {
                            AdsManager.launchTestSuite()
                        }
                        .font(.caption2)
                        .padding(4)
                        .background(.thinMaterial)
                        .clipShape(Capsule())
                        .offset(y: -28)
                        #endif
                    }
            }
        }
        .ignoresSafeArea(edges: .top)
        .task {
            try? await Task.sleep(for: .seconds(1))
            AdsManager.requestTrackingAuthorization {
                AdsManager.initialize {
                    isAdsReady = true
                }
            }
        }
    }
}

#Preview {
    ContentView()
}
