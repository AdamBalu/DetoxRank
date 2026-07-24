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

private val MasterShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(30.dp)
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
