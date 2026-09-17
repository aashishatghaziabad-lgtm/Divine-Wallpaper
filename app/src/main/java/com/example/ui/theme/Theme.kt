package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.graphics.Color

private val DivineDarkColorScheme = darkColorScheme(
    primary = DivineGoldPrimary,
    onPrimary = Color.Black,
    primaryContainer = DivineGoldContainer,
    onPrimaryContainer = DivineGoldOnContainer,
    secondary = SaffronOrange,
    onSecondary = Color.Black,
    tertiary = SacredAmber,
    onTertiary = Color.Black,
    background = DevotionalNavyDark,
    onBackground = TextPrimaryLight,
    surface = DevotionalNavySurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = DevotionalNavyCard,
    onSurfaceVariant = TextSecondaryLight,
    outline = DevotionalNavyBorder,
    error = LotusRose
)

private val DivineLightColorScheme = lightColorScheme(
    primary = SaffronOrange,
    onPrimary = Color.White,
    primaryContainer = DivineGoldContainer,
    onPrimaryContainer = DivineGoldOnContainer,
    secondary = DivineGoldPrimary,
    onSecondary = Color.White,
    tertiary = SacredAmber,
    onTertiary = Color.White,
    background = DivineCreamBg,
    onBackground = DivineTextDark,
    surface = DivineCreamSurface,
    onSurface = DivineTextDark,
    surfaceVariant = DivineCreamBg,
    onSurfaceVariant = DivineTextSubtitle,
    outline = DivineCreamBorder,
    error = LotusRose
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false, // Default to radiant warm cream aesthetic shown in design
    dynamicColor: Boolean = false, // Keep devotional brand palette by default
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DivineDarkColorScheme
        else -> DivineLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
