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
import com.blaubalu.detoxrank.data.Section
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
    // the variant is what weekly task cards render; nothing else reads this slot
    tertiaryContainer = md_theme_light_tertiaryContainerVariant,
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

private val LuxuryColors = darkColorScheme(
    primary = Color(0xFFE5C558),
    onPrimary = Color(0xFF1A1403),
    primaryContainer = Color(0xFF574410),
    onPrimaryContainer = Color(0xFFF8E8B9),
    secondary = Color(0xFFD3B36A),
    onSecondary = Color(0xFF33280F), // Daily background
    secondaryContainer = Color(0xFF4A3A12), // Nav indicator
    tertiary = Color(0xFFF3E2AC), // Hours, Completed text, Progress bar
    onTertiary = Color(0xFF2A210C), // Weekly background
    tertiaryContainer = Color(0xFF6B5518),
    error = Color(0xFFE7C87B),
    onError = Color(0xFF1D1706), // Monthly background (Darkest)
    background = Color(0xFF12100A),
    surface = Color(0xFF12100A),
    onSurface = Color(0xFFF2EAD5),
    surfaceVariant = Color(0xFF4D3F1C), // For custom tasks (Brightest)
    inversePrimary = Color(0xFFE5C558)
)

private val ComicColors = darkColorScheme(
    primary = Color(0xFFFF5252),
    onPrimary = Color(0xFF250505),
    primaryContainer = Color(0xFF7F1D1D),
    onPrimaryContainer = Color(0xFFFFD9D9),
    secondary = Color(0xFFFFD60A),
    onSecondary = Color(0xFF38300A), // Daily background
    secondaryContainer = Color(0xFF4A3D00), // Nav indicator
    tertiary = Color(0xFF4FC3F7), // Hours, Completed text, Progress bar
    onTertiary = Color(0xFF0E2A38), // Weekly background
    tertiaryContainer = Color(0xFF0277BD),
    error = Color(0xFFFF8A80),
    onError = Color(0xFF26060E), // Monthly background (Darkest)
    background = Color(0xFF101B2D),
    surface = Color(0xFF101B2D),
    onSurface = Color(0xFFF2F6FF),
    surfaceVariant = Color(0xFF31415E), // For custom tasks (Brightest)
    inversePrimary = Color(0xFFFF5252)
)

private val SketchColors = darkColorScheme(
    primary = Color(0xFFE8E6E1),
    onPrimary = Color(0xFF20241F),
    primaryContainer = Color(0xFF55584F),
    onPrimaryContainer = Color(0xFFF0EFE9),
    secondary = Color(0xFFBFC5B9),
    onSecondary = Color(0xFF333831), // Daily background
    secondaryContainer = Color(0xFF464B44), // Nav indicator
    tertiary = Color(0xFFD9D5C8), // Hours, Completed text, Progress bar
    onTertiary = Color(0xFF2B2F2A), // Weekly background
    tertiaryContainer = Color(0xFF6A6E64),
    error = Color(0xFFCFCBC0),
    onError = Color(0xFF1F231E), // Monthly background (Darkest)
    background = Color(0xFF252A24),
    surface = Color(0xFF252A24),
    onSurface = Color(0xFFEDEBE4),
    surfaceVariant = Color(0xFF4C524A), // For custom tasks (Brightest)
    inversePrimary = Color(0xFFE8E6E1)
)

private val CartoonColors = darkColorScheme(
    primary = Color(0xFFFF7AC6),
    onPrimary = Color(0xFF33001B),
    primaryContainer = Color(0xFF8E2464),
    onPrimaryContainer = Color(0xFFFFD9EC),
    secondary = Color(0xFF7FE3A0),
    onSecondary = Color(0xFF17371F), // Daily background
    secondaryContainer = Color(0xFF1F5230), // Nav indicator
    tertiary = Color(0xFFFFC94D), // Hours, Completed text, Progress bar
    onTertiary = Color(0xFF33270B), // Weekly background
    tertiaryContainer = Color(0xFF8A6A16),
    error = Color(0xFFFF9E80),
    onError = Color(0xFF1D0F2A), // Monthly background (Darkest)
    background = Color(0xFF1A1035),
    surface = Color(0xFF1A1035),
    onSurface = Color(0xFFF6F1FF),
    surfaceVariant = Color(0xFF3D2E63), // For custom tasks (Brightest)
    inversePrimary = Color(0xFFFF7AC6)
)

// light variant of Sketch: graphite pencil on warm paper
private val PaperColors = lightColorScheme(
    primary = Color(0xFF4A4642),
    onPrimary = Color(0xFFF7F3EA),
    primaryContainer = Color(0xFFD8D2C4),
    onPrimaryContainer = Color(0xFF35322C),
    secondary = Color(0xFF6B665E),
    onSecondary = Color(0xFFF7F3EA),
    secondaryContainer = Color(0xFFE6E0D2), // Daily background + nav indicator
    onSecondaryContainer = Color(0xFF34302A),
    tertiary = Color(0xFF55524B), // Hours, Completed text, Progress bar
    onTertiary = Color(0xFFF7F3EA),
    tertiaryContainer = Color(0xFFDBD4C2), // Weekly background
    onTertiaryContainer = Color(0xFF322F28),
    error = Color(0xFF8C4A3F),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFE8CDC2), // Monthly background
    onErrorContainer = Color(0xFF3A180F),
    background = Color(0xFFF5F1E8),
    onBackground = Color(0xFF34312B),
    surface = Color(0xFFF5F1E8),
    onSurface = Color(0xFF34312B),
    surfaceVariant = Color(0xFFDAD5C8), // Completed tasks
    onSurfaceVariant = Color(0xFF555147),
    outline = Color(0xFF8A857A),
    outlineVariant = Color(0xFFCAC4B5),
    inversePrimary = Color(0xFFB8B2A5)
)

private val BlueprintColors = darkColorScheme(
    primary = Color(0xFF7FD1FF),
    onPrimary = Color(0xFF06263D),
    primaryContainer = Color(0xFF14476B),
    onPrimaryContainer = Color(0xFFD6EDFF),
    secondary = Color(0xFFA8C7E8),
    onSecondary = Color(0xFF16385C), // Daily background
    secondaryContainer = Color(0xFF1B4166), // Nav indicator
    tertiary = Color(0xFFCFE6FF), // Hours, Completed text, Progress bar
    onTertiary = Color(0xFF122F4E), // Weekly background
    tertiaryContainer = Color(0xFF2B587F),
    error = Color(0xFF9FE8FF),
    onError = Color(0xFF0B2138), // Monthly background (Darkest)
    background = Color(0xFF0E2A4A),
    surface = Color(0xFF0E2A4A),
    onSurface = Color(0xFFE3F0FF),
    surfaceVariant = Color(0xFF2A4E74), // Completed tasks
    inversePrimary = Color(0xFF7FD1FF)
)

private val PixelColors = darkColorScheme(
    primary = Color(0xFFFF77A8),
    onPrimary = Color(0xFF2A0A1A),
    primaryContainer = Color(0xFF7E2553),
    onPrimaryContainer = Color(0xFFFFD9E8),
    secondary = Color(0xFFFFEC27),
    onSecondary = Color(0xFF37320D), // Daily background
    secondaryContainer = Color(0xFF4A4210), // Nav indicator
    tertiary = Color(0xFF00E436), // Hours, Completed text, Progress bar
    onTertiary = Color(0xFF10331C), // Weekly background
    tertiaryContainer = Color(0xFF008751),
    error = Color(0xFFFF9EBF),
    onError = Color(0xFF170E28), // Monthly background (Darkest)
    background = Color(0xFF1A1C2C),
    surface = Color(0xFF1A1C2C),
    onSurface = Color(0xFFF4F4F8),
    surfaceVariant = Color(0xFF3B3F5C), // Completed tasks
    inversePrimary = Color(0xFFFF77A8)
)

private val FireColors = darkColorScheme(
    primary = Color(0xFFFF6B35),
    onPrimary = Color(0xFF2A0E04),
    primaryContainer = Color(0xFF7A2E10),
    onPrimaryContainer = Color(0xFFFFDBCB),
    secondary = Color(0xFFFFC15E),
    onSecondary = Color(0xFF4A1E10), // Daily background
    secondaryContainer = Color(0xFF5E2814), // Nav indicator
    tertiary = Color(0xFFFFE0A3), // Hours, Completed text, Progress bar
    onTertiary = Color(0xFF3A150B), // Weekly background
    tertiaryContainer = Color(0xFF9C4A1E),
    error = Color(0xFFFFB59E),
    onError = Color(0xFF240C06), // Monthly background (Darkest)
    background = Color(0xFF1C0A05),
    surface = Color(0xFF1C0A05),
    onSurface = Color(0xFFFFEDE5),
    surfaceVariant = Color(0xFF5A2A16), // Completed tasks
    inversePrimary = Color(0xFFFF6B35)
)

private val WaterColors = darkColorScheme(
    primary = Color(0xFF4DD0E1),
    onPrimary = Color(0xFF03272C),
    primaryContainer = Color(0xFF0E535E),
    onPrimaryContainer = Color(0xFFD2F5FA),
    secondary = Color(0xFF80DEEA),
    onSecondary = Color(0xFF0E3A42), // Daily background
    secondaryContainer = Color(0xFF11444E), // Nav indicator
    tertiary = Color(0xFFB2EBF2), // Hours, Completed text, Progress bar
    onTertiary = Color(0xFF0A2E35), // Weekly background
    tertiaryContainer = Color(0xFF1A6E7C),
    error = Color(0xFFA3E4EE),
    onError = Color(0xFF051D22), // Monthly background (Darkest)
    background = Color(0xFF041F24),
    surface = Color(0xFF041F24),
    onSurface = Color(0xFFE4F7FA),
    surfaceVariant = Color(0xFF14505A), // Completed tasks
    inversePrimary = Color(0xFF4DD0E1)
)

// light, airy element
private val WindColors = lightColorScheme(
    primary = Color(0xFF4A7A8C),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFC8DEE8),
    onPrimaryContainer = Color(0xFF163038),
    secondary = Color(0xFF7FA8B8),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFDCEAF0), // Daily background + nav indicator
    onSecondaryContainer = Color(0xFF1E3640),
    tertiary = Color(0xFF5E8CA0), // Hours, Completed text, Progress bar
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFD0E2E8), // Weekly background
    onTertiaryContainer = Color(0xFF182E36),
    error = Color(0xFF9C5340),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFE8D5CC), // Monthly background
    onErrorContainer = Color(0xFF3A180F),
    background = Color(0xFFF2F7F7),
    onBackground = Color(0xFF25333A),
    surface = Color(0xFFF2F7F7),
    onSurface = Color(0xFF25333A),
    surfaceVariant = Color(0xFFDDE8EA), // Completed tasks
    onSurfaceVariant = Color(0xFF48606A),
    outline = Color(0xFF7A929C),
    outlineVariant = Color(0xFFC4D4DA),
    inversePrimary = Color(0xFFA8C8D6)
)

private val EarthColors = darkColorScheme(
    primary = Color(0xFFC77B4A),
    onPrimary = Color(0xFF2A1608),
    primaryContainer = Color(0xFF5E3A1C),
    onPrimaryContainer = Color(0xFFF5DFC8),
    secondary = Color(0xFF8FA05A),
    onSecondary = Color(0xFF3A2C18), // Daily background
    secondaryContainer = Color(0xFF44341C), // Nav indicator
    tertiary = Color(0xFFD9B98C), // Hours, Completed text, Progress bar
    onTertiary = Color(0xFF2E2212), // Weekly background
    tertiaryContainer = Color(0xFF6E5230),
    error = Color(0xFFDCA987),
    onError = Color(0xFF1C1509), // Monthly background (Darkest)
    background = Color(0xFF171208),
    surface = Color(0xFF171208),
    onSurface = Color(0xFFF2E9DC),
    surfaceVariant = Color(0xFF4A3A22), // Completed tasks
    inversePrimary = Color(0xFFC77B4A)
)

// all four elements at once: each task category carries one element's hue
private val AvatarColors = darkColorScheme(
    primary = Color(0xFFFF7A3D),
    onPrimary = Color(0xFF2A1004),
    primaryContainer = Color(0xFF7A3A14),
    onPrimaryContainer = Color(0xFFFFE0CC),
    secondary = Color(0xFF4CCFE0),
    onSecondary = Color(0xFF4A2410), // Daily background (fire)
    secondaryContainer = Color(0xFF2C3444), // Nav indicator
    tertiary = Color(0xFFE2CC8F), // Hours, Completed text, Progress bar (earth sand)
    onTertiary = Color(0xFF0E3A44), // Weekly background (water)
    tertiaryContainer = Color(0xFF5E4A24),
    error = Color(0xFFE8B487),
    onError = Color(0xFF2E2210), // Monthly background (earth)
    background = Color(0xFF0C0F16),
    surface = Color(0xFF0C0F16),
    onSurface = Color(0xFFF0F2F5),
    surfaceVariant = Color(0xFF343C50), // Completed tasks
    inversePrimary = Color(0xFFFF7A3D)
)

// rose-and-lavender pastels
private val PrincessColors = lightColorScheme(
    primary = Color(0xFFD6659E),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFF8D7E8),
    onPrimaryContainer = Color(0xFF4A1030),
    secondary = Color(0xFFB388D9),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFF8DCEB), // Daily background + nav indicator
    onSecondaryContainer = Color(0xFF3E2050),
    tertiary = Color(0xFFC2708F), // Hours, Completed text, Progress bar
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFEEDCF8), // Weekly background
    onTertiaryContainer = Color(0xFF35204A),
    error = Color(0xFFB0426A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFF8D7DC), // Monthly background
    onErrorContainer = Color(0xFF4A101E),
    background = Color(0xFFFDF2F7),
    onBackground = Color(0xFF3E2A34),
    surface = Color(0xFFFDF2F7),
    onSurface = Color(0xFF3E2A34),
    surfaceVariant = Color(0xFFF0DEE8), // Completed tasks
    onSurfaceVariant = Color(0xFF6A4A5A),
    outline = Color(0xFFA87A92),
    outlineVariant = Color(0xFFE2C8D6),
    inversePrimary = Color(0xFFF0A8CC)
)

// war-torn charcoal and embers
private val ScorchedColors = darkColorScheme(
    primary = Color(0xFFE85D2F),
    onPrimary = Color(0xFF260D04),
    primaryContainer = Color(0xFF6E2A12),
    onPrimaryContainer = Color(0xFFFAD9CB),
    secondary = Color(0xFFB0A080),
    onSecondary = Color(0xFF3A2A1E), // Daily background
    secondaryContainer = Color(0xFF44301E), // Nav indicator
    tertiary = Color(0xFFE8C097), // Hours, Completed text, Progress bar
    onTertiary = Color(0xFF2E211A), // Weekly background
    tertiaryContainer = Color(0xFF7A4A28),
    error = Color(0xFFE8A487),
    onError = Color(0xFF1C1310), // Monthly background (Darkest)
    background = Color(0xFF16130F),
    surface = Color(0xFF16130F),
    onSurface = Color(0xFFF0E8DE),
    surfaceVariant = Color(0xFF4A3A2E), // Completed tasks
    inversePrimary = Color(0xFFE85D2F)
)

private val NinjaColors = darkColorScheme(
    primary = Color(0xFFE8324A),
    onPrimary = Color(0xFF26060B),
    primaryContainer = Color(0xFF6E1420),
    onPrimaryContainer = Color(0xFFFAD2D8),
    secondary = Color(0xFF8A8F98),
    onSecondary = Color(0xFF2E1218), // Daily background
    secondaryContainer = Color(0xFF33141C), // Nav indicator
    tertiary = Color(0xFFD8DCE2), // Hours, Completed text, Progress bar
    onTertiary = Color(0xFF1E2126), // Weekly background
    tertiaryContainer = Color(0xFF4A4F58),
    error = Color(0xFFE8A0AC),
    onError = Color(0xFF121016), // Monthly background (Darkest)
    background = Color(0xFF0B0B0D),
    surface = Color(0xFF0B0B0D),
    onSurface = Color(0xFFEFF0F2),
    surfaceVariant = Color(0xFF33363E), // Completed tasks
    inversePrimary = Color(0xFFE8324A)
)

private val MedievalColors = darkColorScheme(
    primary = Color(0xFFC9A227),
    onPrimary = Color(0xFF241A02),
    primaryContainer = Color(0xFF5E4A0E),
    onPrimaryContainer = Color(0xFFF5E6B8),
    secondary = Color(0xFFA83232),
    onSecondary = Color(0xFF3E2A14), // Daily background
    secondaryContainer = Color(0xFF4A2E12), // Nav indicator
    tertiary = Color(0xFFE8D5A3), // Hours, Completed text, Progress bar
    onTertiary = Color(0xFF33210F), // Weekly background
    tertiaryContainer = Color(0xFF6E5518),
    error = Color(0xFFE0A0A0),
    onError = Color(0xFF241708), // Monthly background (Darkest)
    background = Color(0xFF1A130C),
    surface = Color(0xFF1A130C),
    onSurface = Color(0xFFF2E8D5),
    surfaceVariant = Color(0xFF4A3820), // Completed tasks
    inversePrimary = Color(0xFFC9A227)
)

private val CyberColors = darkColorScheme(
    primary = Color(0xFF00E5FF),
    onPrimary = Color(0xFF00262C),
    primaryContainer = Color(0xFF00495A),
    onPrimaryContainer = Color(0xFFC2F5FF),
    secondary = Color(0xFFFF3DDB),
    onSecondary = Color(0xFF0C2E3A), // Daily background
    secondaryContainer = Color(0xFF163A48), // Nav indicator
    tertiary = Color(0xFF9CFF57), // Hours, Completed text, Progress bar
    onTertiary = Color(0xFF2E0C33), // Weekly background
    tertiaryContainer = Color(0xFF6E1C78),
    error = Color(0xFFFF9EEB),
    onError = Color(0xFF081420), // Monthly background (Darkest)
    background = Color(0xFF060A12),
    surface = Color(0xFF060A12),
    onSurface = Color(0xFFE8F8FF),
    surfaceVariant = Color(0xFF1E3A4A), // Completed tasks
    inversePrimary = Color(0xFF00E5FF)
)

// the ascension theme for max level + Legend rank: aurora light over the void
private val MasterColors = darkColorScheme(
    primary = Color(0xFF7FE7D0),
    onPrimary = Color(0xFF032620),
    primaryContainer = Color(0xFF0E4A3E),
    onPrimaryContainer = Color(0xFFC9FFF1),
    secondary = Color(0xFFB79CFF),
    onSecondary = Color(0xFF241A45), // Daily background
    secondaryContainer = Color(0xFF33245E), // Nav indicator
    tertiary = Color(0xFFF2D57E), // Hours, Completed text, Progress bar
    onTertiary = Color(0xFF191233), // Weekly background
    tertiaryContainer = Color(0xFF6E5A1E),
    error = Color(0xFFF2A9C4),
    onError = Color(0xFF120C22), // Monthly background (Darkest)
    background = Color(0xFF0A0A14),
    surface = Color(0xFF0A0A14),
    onSurface = Color(0xFFF1EEFF),
    surfaceVariant = Color(0xFF2C2748), // Completed tasks
    inversePrimary = Color(0xFF7FE7D0)
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




/**
 * The Avatar theme cycles the elements by app section: every screen is a
 * different nation. Other themes are returned unchanged.
 */
fun effectiveUiTheme(theme: UiTheme, section: Section?): UiTheme =
    if (theme == UiTheme.Avatar && section != null) {
        when (section) {
            Section.Rank -> UiTheme.Fire
            Section.Tasks -> UiTheme.Earth
            Section.Timer -> UiTheme.Water
            Section.Theory -> UiTheme.Wind
        }
    } else {
        theme
    }

@Composable
fun DetoxRankTheme(
    theme: UiTheme = UiTheme.Default,
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    section: Section? = null,
    content: @Composable() () -> Unit
) {
    @Suppress("NAME_SHADOWING")
    val theme = effectiveUiTheme(theme, section)
    val isActuallyDark = when (theme) {
        UiTheme.Default -> useDarkTheme
        UiTheme.Light, UiTheme.Paper, UiTheme.Wind, UiTheme.Princess -> false
        else -> true // the remaining custom themes and Dark theme are dark-based
    }

    val colors = when (theme) {
        UiTheme.Default -> if (useDarkTheme) DarkColors else LightColors
        UiTheme.Light -> LightColors
        UiTheme.Dark -> DarkColors
        UiTheme.Monochrome -> MonochromeColors
        UiTheme.GreenShades -> GreenShadesColors
        UiTheme.BlueShades -> BlueShadesColors
        UiTheme.Luxury -> LuxuryColors
        UiTheme.Comic -> ComicColors
        UiTheme.Sketch -> SketchColors
        UiTheme.Cartoon -> CartoonColors
        UiTheme.Paper -> PaperColors
        UiTheme.Blueprint -> BlueprintColors
        UiTheme.Pixel -> PixelColors
        UiTheme.Master -> MasterColors
        UiTheme.Fire -> FireColors
        UiTheme.Water -> WaterColors
        UiTheme.Wind -> WindColors
        UiTheme.Earth -> EarthColors
        UiTheme.Avatar -> AvatarColors
        UiTheme.Princess -> PrincessColors
        UiTheme.Scorched -> ScorchedColors
        UiTheme.Ninja -> NinjaColors
        UiTheme.Medieval -> MedievalColors
        UiTheme.Cyber -> CyberColors
    }

    CompositionLocalProvider(
        LocalThemeIsDark provides isActuallyDark,
        LocalThemeStyle provides themeStyleFor(theme)
    ) {
        MaterialTheme(
            colorScheme = colors,
            typography = typographyFor(theme),
            shapes = shapesFor(theme),
            content = content
        )
    }
}

