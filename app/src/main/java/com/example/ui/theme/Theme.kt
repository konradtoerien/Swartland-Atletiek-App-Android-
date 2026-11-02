package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Laerskool Swartland Official Colors
val NavyHeader = Color(0xFF0A192F)
val NavyBackground = Color(0xFF0A192F)
val NavySurface = Color(0xFF112240)
val NavySurfaceVariant = Color(0xFF1D2D50)
val NavyBorder = Color(0xFF233554)
val GoldAccent = Color(0xFFF4C430)
val GoldLight = Color(0xFFFFD700)
val GoldDark = Color(0xFFD4A017)
val TextWhite = Color(0xFFFFFFFF)
val TextLight = Color(0xFFE6F1FF)
val TextMuted = Color(0xFF8892B0)
val CardBackground = Color(0xFF112240)
val AccentGreen = Color(0xFF4CAF50)
val AccentRed = Color(0xFFEF5350)
val SilverAccent = Color(0xFFC0C0C0)
val BronzeAccent = Color(0xFFCD7F32)
val BoyBlue = Color(0xFF1E3A8A)
val GirlPink = Color(0xFF831843)

private val DarkColorScheme = darkColorScheme(
    primary = GoldAccent,
    onPrimary = NavyHeader,
    primaryContainer = NavySurfaceVariant,
    onPrimaryContainer = GoldAccent,
    secondary = GoldLight,
    onSecondary = NavyHeader,
    background = NavyBackground,
    onBackground = TextLight,
    surface = NavySurface,
    onSurface = TextWhite,
    surfaceVariant = NavySurfaceVariant,
    onSurfaceVariant = TextMuted,
    outline = NavyBorder
)

private val LightColorScheme = darkColorScheme( // Preserve dark Navy/Gold school branding
    primary = GoldAccent,
    onPrimary = NavyHeader,
    primaryContainer = NavySurfaceVariant,
    onPrimaryContainer = GoldAccent,
    secondary = GoldLight,
    onSecondary = NavyHeader,
    background = NavyBackground,
    onBackground = TextLight,
    surface = NavySurface,
    onSurface = TextWhite,
    surfaceVariant = NavySurfaceVariant,
    onSurfaceVariant = TextMuted,
    outline = NavyBorder
)

@Composable
fun SwartlandTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
