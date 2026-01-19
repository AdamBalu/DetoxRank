package com.blaubalu.detoxrank.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.blaubalu.detoxrank.data.user.UiTheme

val LocalThemeIsDark = staticCompositionLocalOf { false }



private val LightColors = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    errorContainer = md_theme_light_errorContainer,
    onError = md_theme_light_onError,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    outline = md_theme_light_outline,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inverseSurface = md_theme_light_inverseSurface,
    inversePrimary = md_theme_light_inversePrimary,
    surfaceTint = md_theme_light_surfaceTint,
    outlineVariant = md_theme_light_outlineVariant,
    scrim = md_theme_light_scrim,
)


private val DarkColors = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    errorContainer = md_theme_dark_errorContainer,
    onError = md_theme_dark_onError,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    inversePrimary = md_theme_dark_inversePrimary,
    surfaceTint = md_theme_dark_surfaceTint,
    outlineVariant = md_theme_dark_outlineVariant,
    scrim = md_theme_dark_scrim,
)

private val MonochromeColors = darkColorScheme(
    primary = Color(0xFF9E9E9E),
    onPrimary = Color(0xFF000000),
    primaryContainer = Color(0xFF424242),
    onPrimaryContainer = Color(0xFFE0E0E0),
    secondary = Color(0xFF757575),
    onSecondary = Color(0xFF2C2C2C), // Daily background 
    secondaryContainer = Color(0xFF424242), // Nav indicator
    tertiary = Color(0xFFE0E0E0), // Hours, Completed text, Progress bar (Very light grey)
    onTertiary = Color(0xFF222222), // Weekly background
    tertiaryContainer = Color(0xFF616161),
    error = Color(0xFFBDBDBD), 
    onError = Color(0xFF181818), // Monthly background (Darkest)
    errorContainer = Color(0xFF424242),
    background = Color(0xFF121212),
    surface = Color(0xFF121212),
    onSurface = Color(0xFFE0E0E0),
    surfaceVariant = Color(0xFF383838), // For custom tasks (Brightest)
    inversePrimary = Color(0xFF9E9E9E) // For Achievement bar
)

private val GreenShadesColors = darkColorScheme(
    primary = Color(0xFF4CAF50),
    onPrimary = Color(0xFF000000),
    primaryContainer = Color(0xFF1B5E20),
    onPrimaryContainer = Color(0xFFC8E6C9),
    secondary = Color(0xFF81C784),
    onSecondary = Color(0xFF244026), // Daily background 
    secondaryContainer = Color(0xFF2D512E), // Nav indicator
    tertiary = Color(0xFFA5D6A7), // Hours, Completed text, Progress bar
    onTertiary = Color(0xFF1A301B), // Weekly background
    tertiaryContainer = Color(0xFF388E3C),
    error = Color(0xFF81C784),
    onError = Color(0xFF122313), // Monthly background (Darkest)
    background = Color(0xFF0D1F0E),
    surface = Color(0xFF0D1F0E),
    onSurface = Color(0xFFE8F5E9),
    surfaceVariant = Color(0xFF305532), // For custom tasks (Brightest)
    inversePrimary = Color(0xFF4CAF50)
)

private val BlueShadesColors = darkColorScheme(
    primary = Color(0xFF2196F3),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF0D47A1),
    onPrimaryContainer = Color(0xFFBBDEFB),
    secondary = Color(0xFF64B5F6),
    onSecondary = Color(0xFF213F59), // Daily background
    secondaryContainer = Color(0xFF153152), // Nav indicator
    tertiary = Color(0xFF90CAF9), // Hours, Completed text, Progress bar
    onTertiary = Color(0xFF172D40), // Weekly background
    tertiaryContainer = Color(0xFF1976D2),
    error = Color(0xFF64B5F6),
    onError = Color(0xFF0F1E2E), // Monthly background (Darkest)
    background = Color(0xFF0A1929),
    surface = Color(0xFF0A1929),
    onSurface = Color(0xFFE3F2FD),
    surfaceVariant = Color(0xFF2D5577), // For custom tasks (Brightest)
    inversePrimary = Color(0xFF2196F3)
)




@Composable
fun DetoxRankTheme(
    theme: UiTheme = UiTheme.Default,
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val isActuallyDark = when (theme) {
        UiTheme.Default -> useDarkTheme
        UiTheme.Light -> false
        else -> true // All custom themes and Dark theme are dark-based
    }

    val colors = when (theme) {
        UiTheme.Default -> if (useDarkTheme) DarkColors else LightColors
        UiTheme.Light -> LightColors
        UiTheme.Dark -> DarkColors
        UiTheme.Monochrome -> MonochromeColors
        UiTheme.GreenShades -> GreenShadesColors
        UiTheme.BlueShades -> BlueShadesColors
    }

    CompositionLocalProvider(LocalThemeIsDark provides isActuallyDark) {
        MaterialTheme(
            colorScheme = colors,
            content = content
        )
    }
}

