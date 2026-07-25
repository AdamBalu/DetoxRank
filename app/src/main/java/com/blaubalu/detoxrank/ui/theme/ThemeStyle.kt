package com.blaubalu.detoxrank.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.blaubalu.detoxrank.data.user.UiTheme
import kotlin.math.floor
import kotlin.math.min
import kotlin.math.sin

/**
 * A rounded-rectangle shape with slightly irregular, wobbly edges, imitating a
 * hand-drawn pencil outline. The jitter is deterministic (seeded by segment
 * index), so the shape is stable across recompositions.
 */
class HandDrawnShape(
    private val cornerRadiusDp: Float = 14f,
    private val jitterDp: Float = 1.6f
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val r = min(cornerRadiusDp * density.density, min(size.width, size.height) / 4f)
        val j = jitterDp * density.density

        // deterministic pseudo-noise in -1..1
        fun noise(i: Int): Float {
            val x = sin(i * 12.9898f) * 43758.5453f
            return (x - floor(x)) * 2f - 1f
        }

        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(r, noise(1) * j)
            // top edge
            quadraticBezierTo(w / 2f, noise(2) * j, w - r, noise(3) * j)
            // top-right corner
            quadraticBezierTo(w + noise(4) * j, noise(5) * j, w + noise(6) * j, r)
            // right edge
            quadraticBezierTo(w + noise(7) * j, h / 2f, w + noise(8) * j, h - r)
            // bottom-right corner
            quadraticBezierTo(w + noise(9) * j, h + noise(10) * j, w - r, h + noise(11) * j)
            // bottom edge
            quadraticBezierTo(w / 2f, h + noise(12) * j, r, h + noise(13) * j)
            // bottom-left corner
            quadraticBezierTo(noise(14) * j, h + noise(15) * j, noise(16) * j, h - r)
            // left edge
            quadraticBezierTo(noise(17) * j, h / 2f, noise(18) * j, r)
            // top-left corner
            quadraticBezierTo(noise(19) * j, noise(20) * j, r, noise(1) * j)
            close()
        }
        return Outline.Generic(path)
    }
}

/**
 * A rectangle with stair-stepped corners, imitating 8-bit game UI panels
 */
class PixelCornerShape(
    private val stepDp: Float = 4f
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val s = min(stepDp * density.density, min(size.width, size.height) / 6f)
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(2 * s, 0f)
            lineTo(w - 2 * s, 0f)
            lineTo(w - 2 * s, s); lineTo(w - s, s)
            lineTo(w - s, 2 * s); lineTo(w, 2 * s)
            lineTo(w, h - 2 * s)
            lineTo(w - s, h - 2 * s); lineTo(w - s, h - s)
            lineTo(w - 2 * s, h - s); lineTo(w - 2 * s, h)
            lineTo(2 * s, h)
            lineTo(2 * s, h - s); lineTo(s, h - s)
            lineTo(s, h - 2 * s); lineTo(0f, h - 2 * s)
            lineTo(0f, 2 * s)
            lineTo(s, 2 * s); lineTo(s, s)
            lineTo(2 * s, s)
            close()
        }
        return Outline.Generic(path)
    }
}

/**
 * A rounded rectangle with castle battlements notched along the top edge
 */
class CastleShape(private val notchDp: Float = 6f) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val n = min(notchDp * density.density, size.height / 5f)
        val path = Path().apply {
            moveTo(0f, n)
            var x = 0f
            var up = true
            while (x < size.width) {
                val next = min(x + n * 2f, size.width)
                if (up) {
                    lineTo(x, 0f); lineTo(next, 0f); lineTo(next, n)
                } else {
                    lineTo(next, n)
                }
                x = next
                up = !up
            }
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        return Outline.Generic(path)
    }
}

private val DefaultShapes = Shapes()

private val ComicShapes = Shapes(
    extraSmall = RoundedCornerShape(2.dp),
    small = RoundedCornerShape(2.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(6.dp),
    extraLarge = RoundedCornerShape(8.dp)
)

private val CartoonShapes = Shapes(
    extraSmall = RoundedCornerShape(10.dp),
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(22.dp),
    large = RoundedCornerShape(26.dp),
    extraLarge = RoundedCornerShape(34.dp)
)

private val LuxuryShapes = Shapes(
    extraSmall = CutCornerShape(4.dp),
    small = CutCornerShape(6.dp),
    medium = CutCornerShape(10.dp),
    large = CutCornerShape(12.dp),
    extraLarge = CutCornerShape(18.dp)
)

// Material Shapes slots only accept corner-based shapes; the hand-drawn card
// outline is provided through [ThemeStyle.cardShape] instead
private val SketchShapes = Shapes(
    extraSmall = RoundedCornerShape(5.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(14.dp),
    extraLarge = RoundedCornerShape(18.dp)
)

private val BlueprintShapes = Shapes(
    extraSmall = RoundedCornerShape(2.dp),
    small = RoundedCornerShape(2.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(4.dp),
    extraLarge = RoundedCornerShape(6.dp)
)

private val PixelShapes = Shapes(
    extraSmall = RoundedCornerShape(0.dp),
    small = RoundedCornerShape(0.dp),
    medium = RoundedCornerShape(0.dp),
    large = RoundedCornerShape(0.dp),
    extraLarge = RoundedCornerShape(0.dp)
)

// symmetric gem facets for the diamond rank
private val FacetShapes = Shapes(
    extraSmall = CutCornerShape(6.dp),
    small = CutCornerShape(9.dp),
    medium = CutCornerShape(14.dp),
    large = CutCornerShape(16.dp),
    extraLarge = CutCornerShape(22.dp)
)

// asymmetric-cut cyberpunk HUD panels
private val HudShapes = Shapes(
    extraSmall = CutCornerShape(topStart = 6.dp, bottomEnd = 6.dp),
    small = CutCornerShape(topStart = 8.dp, bottomEnd = 8.dp),
    medium = CutCornerShape(topStart = 14.dp, bottomEnd = 14.dp, topEnd = 2.dp, bottomStart = 2.dp),
    large = CutCornerShape(topStart = 16.dp, bottomEnd = 16.dp, topEnd = 2.dp, bottomStart = 2.dp),
    extraLarge = CutCornerShape(topStart = 22.dp, bottomEnd = 22.dp, topEnd = 3.dp, bottomStart = 3.dp)
)

private val MasterShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(30.dp)
)

private val FlameShapes = Shapes(
    extraSmall = CutCornerShape(3.dp),
    small = CutCornerShape(5.dp),
    medium = CutCornerShape(8.dp),
    large = CutCornerShape(10.dp),
    extraLarge = CutCornerShape(16.dp)
)

private val FluidShapes = Shapes(
    extraSmall = RoundedCornerShape(12.dp),
    small = RoundedCornerShape(16.dp),
    medium = RoundedCornerShape(24.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(36.dp)
)

private val SturdyShapes = Shapes(
    extraSmall = RoundedCornerShape(3.dp),
    small = RoundedCornerShape(5.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(10.dp),
    extraLarge = RoundedCornerShape(14.dp)
)

/**
 * Per-theme component shapes: comic panels are hard-edged, cartoon is extra
 * round, luxury gets gem-like cut corners and sketch a hand-drawn outline
 */
fun shapesFor(theme: UiTheme): Shapes = when (theme) {
    UiTheme.Comic -> ComicShapes
    UiTheme.Cartoon -> CartoonShapes
    UiTheme.Luxury -> LuxuryShapes
    UiTheme.Sketch, UiTheme.Paper -> SketchShapes
    UiTheme.Blueprint -> BlueprintShapes
    UiTheme.Pixel -> PixelShapes
    UiTheme.Master -> MasterShapes
    UiTheme.Fire, UiTheme.Scorched -> FlameShapes
    UiTheme.Water, UiTheme.Wind, UiTheme.Princess -> FluidShapes
    UiTheme.Earth -> SturdyShapes
    UiTheme.Avatar -> MasterShapes
    UiTheme.Ninja -> FlameShapes
    UiTheme.Medieval -> SturdyShapes
    UiTheme.Cyber -> HudShapes
    UiTheme.Bronze -> SturdyShapes
    UiTheme.Silver -> FluidShapes
    UiTheme.Gold -> LuxuryShapes
    UiTheme.Platinum -> MasterShapes
    UiTheme.Diamond -> FacetShapes
    else -> DefaultShapes
}

/**
 * Extra, non-Material styling knobs a theme can define
 */
data class ThemeStyle(
    /** outline drawn around content cards (comic ink, pencil stroke, gilded edge) */
    val cardBorder: BorderStroke? = null,
    /** overrides the card surface shape, e.g. the wobbly hand-drawn outline */
    val cardShape: Shape? = null,
    /** custom (user-created) task card color; null keeps the signature gold */
    val customTaskColor: Color? = null
)

val LocalThemeStyle = staticCompositionLocalOf { ThemeStyle() }

fun themeStyleFor(theme: UiTheme): ThemeStyle = when (theme) {
    UiTheme.Monochrome -> ThemeStyle(customTaskColor = Color(0xFF4F4F4F))
    UiTheme.GreenShades -> ThemeStyle(customTaskColor = Color(0xFF4C6B2F))
    UiTheme.BlueShades -> ThemeStyle(customTaskColor = Color(0xFF1E5A8A))
    UiTheme.Comic -> ThemeStyle(
        cardBorder = BorderStroke(3.dp, Color(0xFF06090F)),
        customTaskColor = Color(0xFF4A2B66)
    )
    UiTheme.Sketch -> ThemeStyle(
        cardBorder = BorderStroke(2.dp, Color(0xCCE8E6E1)),
        cardShape = HandDrawnShape(cornerRadiusDp = 14f, jitterDp = 1.8f),
        customTaskColor = Color(0xFF5A5D52)
    )
    UiTheme.Paper -> ThemeStyle(
        cardBorder = BorderStroke(2.dp, Color(0x8C55524B)),
        cardShape = HandDrawnShape(cornerRadiusDp = 14f, jitterDp = 1.8f),
        customTaskColor = Color(0xFFF0E3B2)
    )
    UiTheme.Luxury -> ThemeStyle(
        cardBorder = BorderStroke(1.dp, Color(0x66E5C558)),
        customTaskColor = Color(0xFF6B5518)
    )
    UiTheme.Cartoon -> ThemeStyle(
        cardBorder = BorderStroke(3.dp, Color(0xFF0E0721)),
        customTaskColor = Color(0xFF5A2E8E)
    )
    UiTheme.Blueprint -> ThemeStyle(
        cardBorder = BorderStroke(1.5.dp, Color(0x807FD1FF)),
        customTaskColor = Color(0xFF175C86)
    )
    UiTheme.Pixel -> ThemeStyle(
        cardBorder = BorderStroke(3.dp, Color(0xFF0F1020)),
        cardShape = PixelCornerShape(stepDp = 4f),
        customTaskColor = Color(0xFF7E2553)
    )
    UiTheme.Master -> ThemeStyle(
        // aurora-gradient ink around every card
        cardBorder = BorderStroke(
            1.5.dp,
            Brush.linearGradient(
                listOf(Color(0x99F2D57E), Color(0x997FE7D0), Color(0x99B79CFF))
            )
        ),
        customTaskColor = Color(0xFF57431A)
    )
    UiTheme.Fire -> ThemeStyle(
        cardBorder = BorderStroke(2.dp, Color(0x66FF6B35)),
        customTaskColor = Color(0xFF6E3A0E)
    )
    UiTheme.Water -> ThemeStyle(
        cardBorder = BorderStroke(1.5.dp, Color(0x664DD0E1)),
        customTaskColor = Color(0xFF0E5A50)
    )
    UiTheme.Wind -> ThemeStyle(
        cardBorder = BorderStroke(1.5.dp, Color(0x66559AB0)),
        customTaskColor = Color(0xFFDCE8C8)
    )
    UiTheme.Earth -> ThemeStyle(
        cardBorder = BorderStroke(2.dp, Color(0x66A98547)),
        customTaskColor = Color(0xFF5A4A28)
    )
    UiTheme.Princess -> ThemeStyle(
        cardBorder = BorderStroke(1.5.dp, Color(0x66D6659E)),
        customTaskColor = Color(0xFFF5E3B0)
    )
    UiTheme.Scorched -> ThemeStyle(
        cardBorder = BorderStroke(2.dp, Color(0x66E85D2F)),
        customTaskColor = Color(0xFF4A5A2E)
    )
    UiTheme.Avatar -> ThemeStyle(
        // all four elements circling every card, like the four nations
        cardBorder = BorderStroke(
            2.5.dp,
            Brush.sweepGradient(
                listOf(
                    Color(0xFFFF7A3D), // fire
                    Color(0xFF4CCFE0), // water
                    Color(0xFFD8E8EC), // wind
                    Color(0xFFE2CC8F), // earth
                    Color(0xFFFF7A3D)  // back to fire for a seamless loop
                )
            )
        ),
        customTaskColor = Color(0xFF43596B) // air
    )
    UiTheme.Ninja -> ThemeStyle(
        cardBorder = BorderStroke(1.5.dp, Color(0x66E8324A)),
        customTaskColor = Color(0xFF5A1E28)
    )
    UiTheme.Medieval -> ThemeStyle(
        cardBorder = BorderStroke(2.dp, Color(0x8CC9A227)),
        cardShape = CastleShape(notchDp = 6f),
        customTaskColor = Color(0xFF3E2258) // purpure banner
    )
    UiTheme.Cyber -> ThemeStyle(
        cardBorder = BorderStroke(
            1.5.dp,
            Brush.linearGradient(listOf(Color(0x99F5D90A), Color(0x9900E5C7)))
        ),
        customTaskColor = Color(0xFF4A1030)
    )
    UiTheme.Bronze -> ThemeStyle(
        cardBorder = BorderStroke(2.dp, Color(0x80CD7F32)),
        customTaskColor = Color(0xFF5E3A1E)
    )
    UiTheme.Silver -> ThemeStyle(
        cardBorder = BorderStroke(1.5.dp, Color(0x73C0C8D0)),
        customTaskColor = Color(0xFF4A545E)
    )
    UiTheme.Gold -> ThemeStyle(
        cardBorder = BorderStroke(2.dp, Color(0x8CFFD24A)),
        customTaskColor = Color(0xFF6E5A14)
    )
    UiTheme.Platinum -> ThemeStyle(
        cardBorder = BorderStroke(1.5.dp, Color(0x66D8E8E8)),
        customTaskColor = Color(0xFF3E5458)
    )
    UiTheme.Diamond -> ThemeStyle(
        cardBorder = BorderStroke(
            1.5.dp,
            Brush.linearGradient(listOf(Color(0xCCFFFFFF), Color(0x997FE8FF), Color(0x99B8D8FF)))
        ),
        customTaskColor = Color(0xFF164A6A)
    )
    else -> ThemeStyle()
}

/**
 * Subtle full-screen texture drawn behind the app content: halftone dots for
 * the comic theme, diagonal chalk hatching for the sketch theme
 */
fun Modifier.themeTexture(theme: UiTheme): Modifier = when (theme) {
    UiTheme.Comic -> drawBehind {
        val step = 14.dp.toPx()
        val radius = 1.6.dp.toPx()
        val dot = Color.White.copy(alpha = 0.035f)
        var y = step / 2f
        var row = 0
        while (y < size.height) {
            var x = if (row % 2 == 0) step / 2f else step
            while (x < size.width) {
                drawCircle(dot, radius, Offset(x, y))
                x += step
            }
            y += step
            row++
        }
    }

    UiTheme.Sketch -> drawBehind {
        val step = 26.dp.toPx()
        val stroke = Color.White.copy(alpha = 0.03f)
        var offset = -size.height
        while (offset < size.width) {
            drawLine(
                color = stroke,
                start = Offset(offset, 0f),
                end = Offset(offset + size.height, size.height),
                strokeWidth = 1.5.dp.toPx()
            )
            offset += step
        }
    }

    // graphite hatching on paper
    UiTheme.Paper -> drawBehind {
        val step = 26.dp.toPx()
        val stroke = Color(0xFF3A3733).copy(alpha = 0.035f)
        var offset = -size.height
        while (offset < size.width) {
            drawLine(
                color = stroke,
                start = Offset(offset, 0f),
                end = Offset(offset + size.height, size.height),
                strokeWidth = 1.5.dp.toPx()
            )
            offset += step
        }
    }

    // drafting grid
    UiTheme.Blueprint -> drawBehind {
        val step = 22.dp.toPx()
        val stroke = Color.White.copy(alpha = 0.045f)
        val width = 1.dp.toPx()
        var x = step
        while (x < size.width) {
            drawLine(stroke, Offset(x, 0f), Offset(x, size.height), width)
            x += step
        }
        var y = step
        while (y < size.height) {
            drawLine(stroke, Offset(0f, y), Offset(size.width, y), width)
            y += step
        }
    }

    // CRT scanlines
    UiTheme.Pixel -> drawBehind {
        val step = 4.dp.toPx()
        val stroke = Color.Black.copy(alpha = 0.08f)
        val width = 1.5.dp.toPx()
        var y = 0f
        while (y < size.height) {
            drawLine(stroke, Offset(0f, y), Offset(size.width, y), width)
            y += step
        }
    }

    // drifting embers
    UiTheme.Fire -> drawBehind {
        fun n(i: Int): Float {
            val x = sin(i * 12.9898f) * 43758.5453f
            return x - floor(x)
        }
        val step = 52.dp.toPx()
        var row = 0
        var y = step / 2f
        while (y < size.height) {
            var col = 0
            var x = step / 2f
            while (x < size.width) {
                val seed = row * 113 + col * 11
                val warm = if (n(seed) > 0.5f) Color(0xFFFF6B35) else Color(0xFFFFC15E)
                drawCircle(
                    warm.copy(alpha = 0.03f + n(seed + 1) * 0.07f),
                    (0.7f + n(seed + 2) * 1.3f) * density,
                    Offset(x + (n(seed + 3) - 0.5f) * step, y + (n(seed + 4) - 0.5f) * step)
                )
                x += step; col++
            }
            y += step; row++
        }
    }

    // rolling waves
    UiTheme.Water -> drawBehind {
        val rowStep = 90.dp.toPx()
        val amplitude = 5.dp.toPx()
        val wavelength = 70.dp.toPx()
        val stroke = Color(0xFF4DD0E1).copy(alpha = 0.05f)
        var y = rowStep / 2f
        var row = 0
        while (y < size.height) {
            val phase = row * 1.7f
            var x = 0f
            var prev = Offset(0f, y + amplitude * sin(phase))
            while (x < size.width) {
                x += 12f
                val next = Offset(x, y + amplitude * sin(phase + x / wavelength * 6.283f))
                drawLine(stroke, prev, next, 1.5.dp.toPx())
                prev = next
            }
            y += rowStep; row++
        }
    }

    // streaking gusts
    UiTheme.Wind -> drawBehind {
        fun n(i: Int): Float {
            val x = sin(i * 12.9898f) * 43758.5453f
            return x - floor(x)
        }
        val stroke = Color(0xFF4A7A8C)
        for (i in 0 until 42) {
            val startX = n(i * 3) * size.width
            val startY = n(i * 3 + 1) * size.height
            val len = (60 + n(i * 3 + 2) * 120) * density
            drawLine(
                stroke.copy(alpha = 0.03f + n(i * 7) * 0.05f),
                Offset(startX, startY),
                Offset(startX + len, startY - len * 0.18f),
                (1f + n(i * 5) * 0.8f) * density
            )
        }
    }

    // sediment strata
    UiTheme.Earth -> drawBehind {
        fun n(i: Int): Float {
            val x = sin(i * 12.9898f) * 43758.5453f
            return x - floor(x)
        }
        val stroke = Color(0xFFD9B98C)
        var y = 0f
        var i = 0
        while (y < size.height) {
            y += (26 + n(i) * 22) * density
            drawLine(
                stroke.copy(alpha = 0.03f + n(i + 1) * 0.03f),
                Offset(0f, y),
                Offset(size.width, y),
                (1f + n(i + 2) * 1.5f) * density
            )
            i += 3
        }
    }

    // scattered twinkles
    UiTheme.Princess -> drawBehind {
        fun n(i: Int): Float {
            val x = sin(i * 12.9898f) * 43758.5453f
            return x - floor(x)
        }
        val step = 64.dp.toPx()
        var row = 0
        var y = step / 2f
        while (y < size.height) {
            var col = 0
            var x = step / 2f
            while (x < size.width) {
                val seed = row * 97 + col * 13
                val cx = x + (n(seed) - 0.5f) * step
                val cy = y + (n(seed + 1) - 0.5f) * step
                val r = (2f + n(seed + 2) * 3f) * density
                val tint = if (n(seed + 3) > 0.5f) Color(0xFFD6659E) else Color(0xFFC9A227)
                val sparkle = tint.copy(alpha = 0.10f + n(seed + 4) * 0.10f)
                val w = 1.2f * density
                drawLine(sparkle, Offset(cx - r, cy), Offset(cx + r, cy), w)
                drawLine(sparkle, Offset(cx, cy - r), Offset(cx, cy + r), w)
                x += step; col++
            }
            y += step; row++
        }
    }

    // each element claims one quadrant of the screen
    UiTheme.Avatar -> drawBehind {
        fun n(i: Int): Float {
            val x = sin(i * 12.9898f) * 43758.5453f
            return x - floor(x)
        }
        val midX = size.width / 2f
        val midY = size.height / 2f

        // top-left: wind gusts
        for (i in 0 until 14) {
            val sx = n(i * 3) * midX
            val sy = n(i * 3 + 1) * midY
            val len = (40 + n(i * 3 + 2) * 80) * density
            drawLine(
                Color(0xFFD8E8EC).copy(alpha = 0.05f + n(i * 7) * 0.06f),
                Offset(sx, sy),
                Offset(sx + len, sy - len * 0.2f),
                (1f + n(i * 5) * 0.8f) * density
            )
        }

        // top-right: water waves
        run {
            val rowStep = 60.dp.toPx()
            val amplitude = 4.dp.toPx()
            val wavelength = 60.dp.toPx()
            var y = rowStep / 2f
            var row = 0
            while (y < midY) {
                var x = midX
                var prev = Offset(x, y + amplitude * sin(row * 1.7f))
                while (x < size.width) {
                    x += 12f
                    val next =
                        Offset(x, y + amplitude * sin(row * 1.7f + x / wavelength * 6.283f))
                    drawLine(
                        Color(0xFF4CCFE0).copy(alpha = 0.08f),
                        prev, next, 1.5.dp.toPx()
                    )
                    prev = next
                }
                y += rowStep; row++
            }
        }

        // bottom-left: embers
        run {
            val step = 40.dp.toPx()
            var row = 0
            var y = midY + step / 2f
            while (y < size.height) {
                var col = 0
                var x = step / 2f
                while (x < midX) {
                    val seed = row * 113 + col * 11
                    val warm = if (n(seed) > 0.5f) Color(0xFFFF7A3D) else Color(0xFFFFC15E)
                    drawCircle(
                        warm.copy(alpha = 0.06f + n(seed + 1) * 0.08f),
                        (0.8f + n(seed + 2) * 1.4f) * density,
                        Offset(x + (n(seed + 3) - 0.5f) * step, y + (n(seed + 4) - 0.5f) * step)
                    )
                    x += step; col++
                }
                y += step; row++
            }
        }

        // bottom-right: earth strata
        run {
            var y = midY
            var i = 0
            while (y < size.height) {
                y += (20 + n(i) * 18) * density
                drawLine(
                    Color(0xFFE2CC8F).copy(alpha = 0.05f + n(i + 1) * 0.05f),
                    Offset(midX, y),
                    Offset(size.width, y),
                    (1f + n(i + 2) * 1.6f) * density
                )
                i += 3
            }
        }
    }

    // falling ash
    UiTheme.Scorched -> drawBehind {
        fun n(i: Int): Float {
            val x = sin(i * 12.9898f) * 43758.5453f
            return x - floor(x)
        }
        val step = 44.dp.toPx()
        var row = 0
        var y = step / 2f
        while (y < size.height) {
            var col = 0
            var x = step / 2f
            while (x < size.width) {
                val seed = row * 151 + col * 17
                val grey = if (n(seed) > 0.85f) Color(0xFFE85D2F) else Color(0xFFB0A080)
                drawCircle(
                    grey.copy(alpha = 0.025f + n(seed + 1) * 0.055f),
                    (0.6f + n(seed + 2) * 1.1f) * density,
                    Offset(x + (n(seed + 3) - 0.5f) * step, y + (n(seed + 4) - 0.5f) * step)
                )
                x += step; col++
            }
            y += step; row++
        }
    }

    // blade slashes in the dark
    UiTheme.Ninja -> drawBehind {
        fun n(i: Int): Float {
            val x = sin(i * 12.9898f) * 43758.5453f
            return x - floor(x)
        }
        for (i in 0 until 26) {
            val sx = n(i * 3) * size.width
            val sy = n(i * 3 + 1) * size.height
            val len = (50 + n(i * 3 + 2) * 110) * density
            val tint = if (n(i * 11) > 0.8f) Color(0xFFE8324A) else Color(0xFF8A8F98)
            drawLine(
                tint.copy(alpha = 0.04f + n(i * 7) * 0.05f),
                Offset(sx, sy),
                Offset(sx + len * 0.5f, sy + len),
                (1f + n(i * 5) * 0.6f) * density
            )
        }
    }

    // castle-wall masonry in running bond
    UiTheme.Medieval -> drawBehind {
        fun n(i: Int): Float {
            val x = sin(i * 12.9898f) * 43758.5453f
            return x - floor(x)
        }
        val rowH = 42.dp.toPx()
        val brickW = 82.dp.toPx()
        val mortar = Color(0xFFE8D5A3)
        val stroke = 1.2.dp.toPx()
        var y = 0f
        var row = 0
        while (y < size.height) {
            drawLine(
                mortar.copy(alpha = 0.035f + n(row) * 0.02f),
                Offset(0f, y), Offset(size.width, y), stroke
            )
            var x = if (row % 2 == 0) 0f else brickW / 2f
            var col = 0
            while (x < size.width) {
                drawLine(
                    mortar.copy(alpha = 0.03f + n(row * 31 + col) * 0.025f),
                    Offset(x, y), Offset(x, y + rowH), stroke
                )
                x += brickW
                col++
            }
            y += rowH
            row++
        }
    }
    // circuit traces with solder-point nodes
    UiTheme.Cyber -> drawBehind {
        fun n(i: Int): Float {
            val x = sin(i * 12.9898f) * 43758.5453f
            return x - floor(x)
        }
        val stroke = 1.2f * density
        for (i in 0 until 30) {
            val tint = (if (n(i * 13) > 0.5f) Color(0xFFF5D90A) else Color(0xFF00E5C7))
                .copy(alpha = 0.04f + n(i * 17) * 0.05f)
            var x = n(i * 3) * size.width
            var y = n(i * 3 + 1) * size.height
            val horizontalFirst = n(i * 7) > 0.5f
            val l1 = (30 + n(i * 3 + 2) * 90) * density
            val l2 = (24 + n(i * 5) * 70) * density
            val midX = if (horizontalFirst) x + l1 else x
            val midY = if (horizontalFirst) y else y + l1
            val endX = if (horizontalFirst) midX else midX + l2
            val endY = if (horizontalFirst) midY + l2 else midY
            drawLine(tint, Offset(x, y), Offset(midX, midY), stroke)
            drawLine(tint, Offset(midX, midY), Offset(endX, endY), stroke)
            drawCircle(tint, 2.2f * density, Offset(x, y))
            drawCircle(tint, 2.2f * density, Offset(endX, endY))
        }
    }

    // hammered copper dimples
    UiTheme.Bronze -> drawBehind {
        fun n(i: Int): Float { val x = sin(i * 12.9898f) * 43758.5453f; return x - floor(x) }
        val step = 40.dp.toPx()
        var row = 0; var y = step / 2f
        while (y < size.height) {
            var col = 0; var x = step / 2f
            while (x < size.width) {
                val seed = row * 101 + col * 13
                drawCircle(
                    Color(0xFFE0A878).copy(alpha = 0.028f + n(seed) * 0.04f),
                    (2.5f + n(seed + 1) * 3.5f) * density,
                    Offset(x + (n(seed + 2) - 0.5f) * step * 0.6f, y + (n(seed + 3) - 0.5f) * step * 0.6f)
                )
                x += step; col++
            }
            y += step; row++
        }
    }

    // brushed metal grain
    UiTheme.Silver -> drawBehind {
        fun n(i: Int): Float { val x = sin(i * 12.9898f) * 43758.5453f; return x - floor(x) }
        for (i in 0 until 90) {
            val y = n(i * 3) * size.height
            val sx = n(i * 3 + 1) * size.width
            val len = (40 + n(i * 3 + 2) * 200) * density
            drawLine(
                Color(0xFFE4EAF0).copy(alpha = 0.02f + n(i * 7) * 0.035f),
                Offset(sx, y), Offset(sx + len, y), 1f * density
            )
        }
    }

    // molten glints
    UiTheme.Gold -> drawBehind {
        fun n(i: Int): Float { val x = sin(i * 12.9898f) * 43758.5453f; return x - floor(x) }
        val step = 44.dp.toPx()
        var row = 0; var y = step / 2f
        while (y < size.height) {
            var col = 0; var x = step / 2f
            while (x < size.width) {
                val seed = row * 119 + col * 17
                val tint = if (n(seed) > 0.6f) Color(0xFFFFE8A0) else Color(0xFFFFD24A)
                drawCircle(
                    tint.copy(alpha = 0.03f + n(seed + 1) * 0.08f),
                    (0.6f + n(seed + 2) * 1.4f) * density,
                    Offset(x + (n(seed + 3) - 0.5f) * step, y + (n(seed + 4) - 0.5f) * step)
                )
                x += step; col++
            }
            y += step; row++
        }
    }

    // sleek sheen sweeps
    UiTheme.Platinum -> drawBehind {
        val band = 120.dp.toPx()
        var offset = -size.height
        var i = 0
        while (offset < size.width) {
            drawLine(
                Color(0xFFEAF5F2).copy(alpha = if (i % 3 == 0) 0.035f else 0.018f),
                Offset(offset, size.height), Offset(offset + size.height, 0f),
                band * 0.3f
            )
            offset += band
            i++
        }
    }

    // scattered gem facets
    UiTheme.Diamond -> drawBehind {
        fun n(i: Int): Float { val x = sin(i * 12.9898f) * 43758.5453f; return x - floor(x) }
        val step = 60.dp.toPx()
        var row = 0; var y = step / 2f
        while (y < size.height) {
            var col = 0; var x = step / 2f
            while (x < size.width) {
                val seed = row * 131 + col * 19
                val cx = x + (n(seed) - 0.5f) * step
                val cy = y + (n(seed + 1) - 0.5f) * step
                val r = (2.5f + n(seed + 2) * 4f) * density
                val tint = (if (n(seed + 3) > 0.5f) Color(0xFF7FE8FF) else Color(0xFFFFFFFF))
                    .copy(alpha = 0.05f + n(seed + 4) * 0.08f)
                val w = 1f * density
                drawLine(tint, Offset(cx, cy - r), Offset(cx + r, cy), w)
                drawLine(tint, Offset(cx + r, cy), Offset(cx, cy + r), w)
                drawLine(tint, Offset(cx, cy + r), Offset(cx - r, cy), w)
                drawLine(tint, Offset(cx - r, cy), Offset(cx, cy - r), w)
                x += step; col++
            }
            y += step; row++
        }
    }

    // star field
    UiTheme.Master -> drawBehind {
        fun sparkle(i: Int): Float {
            val x = sin(i * 12.9898f) * 43758.5453f
            return x - floor(x)
        }

        val step = 46.dp.toPx()
        var row = 0
        var y = step / 2f
        while (y < size.height) {
            var col = 0
            var x = step / 2f
            while (x < size.width) {
                val seed = row * 131 + col * 7
                val dx = (sparkle(seed) - 0.5f) * step * 0.8f
                val dy = (sparkle(seed + 1) - 0.5f) * step * 0.8f
                val alpha = 0.03f + sparkle(seed + 2) * 0.09f
                val radius = (0.6f + sparkle(seed + 3) * 1.2f) * density
                drawCircle(Color.White.copy(alpha = alpha), radius, Offset(x + dx, y + dy))
                x += step
                col++
            }
            y += step
            row++
        }
    }

    else -> this
}
