package com.blaubalu.detoxrank.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.blaubalu.detoxrank.R
import com.blaubalu.detoxrank.data.user.UiTheme

/**
 * Typography powered by Google Fonts library
 */

val DMSans = FontFamily(
    Font(R.font.dm_sans_regular),
    Font(R.font.dm_sans_bold),
    Font(R.font.dm_sans_medium)
)

val JosefinSans = FontFamily(
    Font(R.font.josefin_sans_bold),
    Font(R.font.josefin_sans_regular),
    Font(R.font.josefin_sans_thin)
)

// premium theme fonts (all Google Fonts, OFL licensed)
val PatrickHand = FontFamily(Font(R.font.patrick_hand))
val Bangers = FontFamily(Font(R.font.bangers))
val ComicNeue = FontFamily(
    Font(R.font.comic_neue),
    Font(R.font.comic_neue_bold, FontWeight.Bold)
)
val Baloo = FontFamily(Font(R.font.baloo2))
val Cinzel = FontFamily(Font(R.font.cinzel))
val EBGaramond = FontFamily(Font(R.font.eb_garamond))
val PressStart = FontFamily(Font(R.font.press_start))
val VT323 = FontFamily(Font(R.font.vt323))
val ShareTechMono = FontFamily(Font(R.font.share_tech_mono))
val Marcellus = FontFamily(Font(R.font.marcellus))
val Philosopher = FontFamily(Font(R.font.philosopher_bold, FontWeight.Bold))
val GreatVibes = FontFamily(Font(R.font.great_vibes))
val Quicksand = FontFamily(Font(R.font.quicksand))
val BlackOpsOne = FontFamily(Font(R.font.black_ops_one))
val Oswald = FontFamily(Font(R.font.oswald))

/**
 * Builds the app's type scale from a display font (headlines/titles) and a body
 * font. Sizes and metrics stay identical across themes so layouts don't shift;
 * [displayScale] compensates for unusually wide display fonts (pixel font).
 */
private fun buildTypography(
    display: FontFamily,
    body: FontFamily,
    displayScale: Float = 1f
): Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = body,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = body
    ),
    bodySmall = TextStyle(
        fontFamily = body,
        fontStyle = FontStyle.Italic
    ),
    titleMedium = TextStyle(
        fontFamily = display,
        fontWeight = FontWeight.Bold,
        fontSize = (18 * displayScale).sp
    ),
    titleLarge = TextStyle(
        fontFamily = body,
        fontWeight = FontWeight.Bold,
        fontSize = 25.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = display,
        fontWeight = FontWeight.Bold,
        fontSize = (40 * displayScale).sp
    ),
    headlineMedium = TextStyle(
        fontFamily = display,
        fontWeight = FontWeight.Bold,
        fontSize = (30 * displayScale).sp
    ),
    headlineSmall = TextStyle(
        fontFamily = display,
        fontWeight = FontWeight.Bold,
        fontSize = (20 * displayScale).sp
    )
)

/**
 * specifications and edits of individual fonts
 */
val Typography = buildTypography(display = JosefinSans, body = DMSans)

/**
 * Per-theme typography: premium themes swap the whole type character of the app
 */
fun typographyFor(theme: UiTheme): Typography = when (theme) {
    UiTheme.Luxury -> buildTypography(display = Cinzel, body = EBGaramond)
    UiTheme.Comic -> buildTypography(display = Bangers, body = ComicNeue)
    UiTheme.Sketch, UiTheme.Paper -> buildTypography(display = PatrickHand, body = PatrickHand)
    UiTheme.Cartoon -> buildTypography(display = Baloo, body = Baloo)
    UiTheme.Blueprint -> buildTypography(display = ShareTechMono, body = ShareTechMono)
    UiTheme.Pixel -> buildTypography(display = PressStart, body = VT323, displayScale = 0.7f)
    UiTheme.Master -> buildTypography(display = Marcellus, body = DMSans)
    UiTheme.Fire, UiTheme.Water, UiTheme.Wind, UiTheme.Earth, UiTheme.Avatar ->
        buildTypography(display = Philosopher, body = DMSans)
    UiTheme.Princess -> buildTypography(display = GreatVibes, body = Quicksand, displayScale = 1.15f)
    UiTheme.Scorched -> buildTypography(display = BlackOpsOne, body = Oswald, displayScale = 0.9f)
    else -> Typography
}