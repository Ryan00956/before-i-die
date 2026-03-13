package com.lastregrets.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// 始终使用暗色主题 - 符合项目调性
private val DarkColorScheme = darkColorScheme(
    primary = WarmAmber,
    onPrimary = DeepNavy,
    primaryContainer = DarkBlue,
    onPrimaryContainer = CandleGlow,
    secondary = RoseGold,
    onSecondary = DeepNavy,
    secondaryContainer = MidnightBlue,
    onSecondaryContainer = TextPrimary,
    tertiary = SoftGold,
    onTertiary = DeepNavy,
    background = DeepNavy,
    onBackground = TextPrimary,
    surface = DarkBlue,
    onSurface = TextPrimary,
    surfaceVariant = MidnightBlue,
    onSurfaceVariant = TextSecondary,
    outline = SlateBlue,
    error = ErrorRed,
    onError = Color.White,
)

@Composable
fun LastRegretsTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = DeepNavy.toArgb()
            window.navigationBarColor = DeepNavy.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
