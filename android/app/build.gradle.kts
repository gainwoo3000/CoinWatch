plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "namu0329.CoinWatch"
    compileSdk = 36

    defaultConfig {
        applicationId = "namu0329.CoinWatch"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")

    // LevelPlay mediation SDK + mediated networks (mirrors the iOS integration)
    implementation("com.ironsource.sdk:mediationsdk:9.2.0")
    implementation("com.ironsource.adapters:admobadapter:5.2.0")
    implementation("com.ironsource.adapters:unityadsadapter:5.2.0")
    implementation("com.google.android.gms:play-services-ads:25.4.0")
    implementation("com.unity3d.ads:unity-ads:4.20.0")
}
