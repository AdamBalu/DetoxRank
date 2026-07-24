package com.blaubalu.detoxrank.ui.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.blaubalu.detoxrank.data.billing.ThemeBilling
import com.blaubalu.detoxrank.data.user.Rank
import com.blaubalu.detoxrank.data.user.UiTheme
import com.blaubalu.detoxrank.ui.utils.Constants.ALL_THEMES_UNLOCKED_FOR_TESTING
import com.blaubalu.detoxrank.ui.utils.Constants.MAX_LEVEL
import com.blaubalu.detoxrank.ui.utils.toastShort

/**
 * Unwraps the [Activity] from a composable context, needed for the Play billing flow
 */
private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

/**
 * Data class representing a theme option for display.
 * [requiredLevel] of 0 means the theme is free; anything above that
 * unlocks once the user reaches the given level (or owns the theme).
 * [isPremium] themes are bought through Google Play instead.
 */
data class ThemeOption(
    val theme: UiTheme,
    val name: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val backgroundColor: Color,
    val requiredLevel: Int = 0,
    val isPremium: Boolean = false,
    /** the theme whose purchase unlocks this one (bundled variants) */
    val purchaseKey: UiTheme = theme,
    /** unlocked only at max level + Legend rank */
    val requiresMastery: Boolean = false
)

/**
 * All selectable theme options
 */
val themeOptions = listOf(
    ThemeOption(
        theme = UiTheme.Default,
        name = "Default",
        primaryColor = md_theme_dark_primary,
        secondaryColor = md_theme_dark_secondary,
        backgroundColor = md_theme_dark_background
    ),
    ThemeOption(
        theme = UiTheme.Light,
        name = "Light",
        primaryColor = md_theme_light_primary,
        secondaryColor = md_theme_light_secondary,
        backgroundColor = md_theme_light_background
    ),
    ThemeOption(
        theme = UiTheme.Dark,
        name = "Dark",
        primaryColor = md_theme_dark_primary,
        secondaryColor = md_theme_dark_secondary,
        backgroundColor = Color(0xFF0A0A0A)
    ),
    ThemeOption(
        theme = UiTheme.Monochrome,
        name = "Monochrome",
        primaryColor = Color(0xFF9E9E9E),
        secondaryColor = Color(0xFF616161),
        backgroundColor = Color(0xFF1A1A1A),
        requiredLevel = 5
    ),
    ThemeOption(
        theme = UiTheme.GreenShades,
        name = "Forest",
        primaryColor = Color(0xFF4CAF50),
        secondaryColor = Color(0xFF81C784),
        backgroundColor = Color(0xFF0D1F0E),
        requiredLevel = 10
    ),
    ThemeOption(
        theme = UiTheme.BlueShades,
        name = "Ocean",
        primaryColor = Color(0xFF2196F3),
        secondaryColor = Color(0xFF64B5F6),
        backgroundColor = Color(0xFF0A1929),
        requiredLevel = 15
    ),
    ThemeOption(
        theme = UiTheme.Luxury,
        name = "Luxury",
        primaryColor = Color(0xFFE5C558),
        secondaryColor = Color(0xFFD3B36A),
        backgroundColor = Color(0xFF12100A),
        isPremium = true
    ),
    ThemeOption(
        theme = UiTheme.Comic,
        name = "Comic",
        primaryColor = Color(0xFFFF5252),
        secondaryColor = Color(0xFFFFD60A),
        backgroundColor = Color(0xFF101B2D),
        isPremium = true
    ),
    ThemeOption(
        theme = UiTheme.Sketch,
        name = "Sketch",
        primaryColor = Color(0xFFE8E6E1),
        secondaryColor = Color(0xFFBFC5B9),
        backgroundColor = Color(0xFF252A24),
        isPremium = true
    ),
    ThemeOption(
        theme = UiTheme.Paper,
        name = "Paper",
        primaryColor = Color(0xFF4A4642),
        secondaryColor = Color(0xFF8A857A),
        backgroundColor = Color(0xFFF5F1E8),
        isPremium = true,
        purchaseKey = UiTheme.Sketch // bundled with Sketch
    ),
    ThemeOption(
        theme = UiTheme.Cartoon,
        name = "Cartoon",
        primaryColor = Color(0xFFFF7AC6),
        secondaryColor = Color(0xFF7FE3A0),
        backgroundColor = Color(0xFF1A1035),
        isPremium = true
    ),
    ThemeOption(
        theme = UiTheme.Blueprint,
        name = "Blueprint",
        primaryColor = Color(0xFF7FD1FF),
        secondaryColor = Color(0xFFA8C7E8),
        backgroundColor = Color(0xFF0E2A4A),
        isPremium = true
    ),
    ThemeOption(
        theme = UiTheme.Pixel,
        name = "Pixel",
        primaryColor = Color(0xFFFF77A8),
        secondaryColor = Color(0xFFFFEC27),
        backgroundColor = Color(0xFF1A1C2C),
        isPremium = true
    ),
    ThemeOption(
        theme = UiTheme.Master,
        name = "Master",
        primaryColor = Color(0xFF7FE7D0),
        secondaryColor = Color(0xFFB79CFF),
        backgroundColor = Color(0xFF0A0A14),
        requiresMastery = true
    )
)

/**
 * Icon button to open theme selector
 */
@Composable
fun ThemeSelectorButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Filled.Palette,
            contentDescription = "Select Theme",
            tint = MaterialTheme.colorScheme.secondaryContainer
        )
    }
}

/**
 * Bottom sheet for theme selection. Themes with a required level are
 * unlocked by leveling up; already purchased themes stay unlocked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSelectorSheet(
    isVisible: Boolean,
    currentTheme: UiTheme,
    currentLevel: Int,
    currentRank: Rank,
    purchasedThemes: Set<UiTheme>,
    onThemeSelected: (UiTheme) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current
    var previewOption by remember { mutableStateOf<ThemeOption?>(null) }

    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select Theme",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (ALL_THEMES_UNLOCKED_FOR_TESTING) {
                        "All themes unlocked for testing"
                    } else {
                        "Level up to unlock themes — or grab a premium one to support the dev!"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(bottom = 32.dp)
                ) {
                    items(themeOptions) { option ->
                        val isUnlocked = ALL_THEMES_UNLOCKED_FOR_TESTING ||
                                when {
                                    option.requiresMastery ->
                                        currentLevel >= MAX_LEVEL && currentRank == Rank.Legend

                                    option.isPremium ->
                                        purchasedThemes.contains(option.theme) ||
                                                purchasedThemes.contains(option.purchaseKey)

                                    else -> option.requiredLevel <= currentLevel ||
                                            purchasedThemes.contains(option.theme)
                                }

                        ThemeCard(
                            option = option,
                            isSelected = currentTheme == option.theme,
                            isUnlocked = isUnlocked,
                            onClick = {
                                if (isUnlocked) {
                                    onThemeSelected(option.theme)
                                } else {
                                    // locked: show a live preview with the unlock action
                                    previewOption = option
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    previewOption?.let { option ->
        ThemePreviewDialog(
            option = option,
            onUnlock = {
                if (option.isPremium) {
                    val activity = context.findActivity()
                    val launched = activity != null &&
                            ThemeBilling.purchase(activity, option.purchaseKey)
                    if (!launched) {
                        toastShort("Google Play is not available right now", context)
                    }
                    previewOption = null
                }
            },
            onDismiss = { previewOption = null }
        )
    }
}

/**
 * Full-fidelity preview of a locked theme: rendered inside the target theme so
 * the user sees its real fonts, shapes, card outlines and background texture
 * before unlocking it
 */
@Composable
fun ThemePreviewDialog(
    option: ThemeOption,
    onUnlock: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        DetoxRankTheme(theme = option.theme) {
            val style = LocalThemeStyle.current
            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .themeTexture(option.theme)
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = option.name,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "This is how the app will look",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
                    )

                    PreviewTaskCard(
                        text = "Meditate",
                        containerColor = if (LocalThemeIsDark.current) {
                            MaterialTheme.colorScheme.onSecondary
                        } else {
                            MaterialTheme.colorScheme.secondaryContainer
                        },
                        style = style
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    PreviewTaskCard(
                        text = "Completed!",
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        style = style,
                        contentColor = MaterialTheme.colorScheme.tertiary,
                        checked = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    PreviewTaskCard(
                        text = "Your custom task",
                        containerColor = style.customTaskColor
                            ?: if (LocalThemeIsDark.current) rank_color_ultra_dark else rank_color_ultra_light,
                        style = style
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    if (option.isPremium) {
                        Button(onClick = onUnlock) {
                            val price = ThemeBilling.themePrices[option.purchaseKey]
                            Text(
                                text = if (price != null) "Unlock for $price" else "Unlock",
                                fontWeight = FontWeight.Bold
                            )
                        }
                        if (option.purchaseKey != option.theme) {
                            Text(
                                text = "Included in the ${option.purchaseKey.name} theme",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 6.dp)
                            )
                        }
                    } else {
                        Text(
                            text = if (option.requiresMastery) {
                                "Reach level 25 and the Legend rank to unlock"
                            } else {
                                "Reach level ${option.requiredLevel} to unlock"
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    TextButton(onClick = onDismiss, modifier = Modifier.padding(top = 4.dp)) {
                        Text("Close")
                    }
                }
            }
        }
    }
}

/**
 * A miniature themed task card used inside the theme preview
 */
@Composable
private fun PreviewTaskCard(
    text: String,
    containerColor: Color,
    style: ThemeStyle,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    checked: Boolean = false
) {
    Card(
        shape = style.cardShape ?: MaterialTheme.shapes.medium,
        border = style.cardBorder,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 15.sp
            )
            Checkbox(checked = checked, onCheckedChange = null)
        }
    }
}

/**
 * Card representing a single theme option
 */
@Composable
fun ThemeCard(
    option: ThemeOption,
    isSelected: Boolean,
    isUnlocked: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .then(
                if (isSelected) Modifier.border(
                    width = 3.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(16.dp)
                ) else Modifier
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = option.backgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Color preview circles
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .alpha(if (isUnlocked) 1f else 0.4f),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(option.primaryColor)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(option.secondaryColor)
                )
            }

            // Theme name (with unlock requirement when locked)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
            ) {
                if (!isUnlocked) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = "Locked",
                            tint = rank_color,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = when {
                                option.requiresMastery -> " Lvl 25 · Legend"
                                option.isPremium ->
                                    " " + (ThemeBilling.themePrices[option.purchaseKey] ?: "Premium")
                                else -> " Level ${option.requiredLevel}"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = rank_color,
                            fontSize = 10.sp
                        )
                    }
                }
                Text(
                    text = option.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = option.primaryColor,
                    textAlign = TextAlign.Center,
                )
            }

            // Selected checkmark
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Selected",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
