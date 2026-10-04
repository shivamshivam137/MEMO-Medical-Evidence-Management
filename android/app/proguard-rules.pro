# ProGuard rules for MEMO
-dontobfuscate
-keepattributes *Annotation*
-keepclassmembers class * {
    @androidx.compose.runtime.Composable *;
}
