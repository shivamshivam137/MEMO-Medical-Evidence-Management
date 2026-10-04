package com.memo.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Teal600,
    onPrimary = SurfaceWhite,
    primaryContainer = Teal100,
    onPrimaryContainer = Teal700,
    secondary = Slate700,
    onSecondary = SurfaceWhite,
    secondaryContainer = Slate100,
    onSecondaryContainer = Slate900,
    tertiary = Slate600,
    onTertiary = SurfaceWhite,
    background = BackgroundLight,
    onBackground = Slate900,
    surface = SurfaceWhite,
    onSurface = Slate900,
    surfaceVariant = Slate100,
    onSurfaceVariant = Slate700,
    outline = Slate300,
    outlineVariant = Slate200,
    error = StatusAbnormal,
    onError = SurfaceWhite,
    errorContainer = StatusAbnormalBg,
    onErrorContainer = StatusAbnormal
)

@Composable
fun MEMOTheme(
    darkTheme: Boolean = false, // Keep light mode crisp for academic medical demonstration
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Slate900.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
