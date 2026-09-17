# ironSource / Unity LevelPlay mediation SDK relies on reflection to discover
# adapter classes at runtime; without these keep rules R8's obfuscation pass
# can rename/strip pieces it needs, causing ads to silently fail to load.
-keep class com.ironsource.** { *; }
-keep class com.unity3d.mediation.** { *; }
-keep class com.unity3d.ads.** { *; }
-keep class com.unity3d.services.** { *; }
-dontwarn com.ironsource.**
-dontwarn com.unity3d.**

# Google Mobile Ads SDK (used here as a mediated network via the ironSource
# AdMob adapter).
-keep public class com.google.android.gms.ads.** {
    public *;
}
-keep class com.google.android.gms.internal.ads.** { *; }
-dontwarn com.google.android.gms.ads.**

-keepattributes JavascriptInterface
-keepattributes *Annotation*
-keepattributes Signature
