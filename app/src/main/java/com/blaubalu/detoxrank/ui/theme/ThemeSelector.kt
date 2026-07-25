package com.blaubalu.detoxrank.ui.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Storefront
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.blaubalu.detoxrank.data.billing.ThemeBilling
import com.blaubalu.detoxrank.data.billing.ThemeShopState
import com.blaubalu.detoxrank.data.user.Rank
import com.blaubalu.detoxrank.data.user.UiTheme
import com.blaubalu.detoxrank.ui.utils.Constants.ALL_THEMES_UNLOCKED_FOR_TESTING
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
    /** human-readable name of the purchase this theme is bundled in */
    val bundleLabel: String? = null,
    /** unlocked upon reaching this rank */
    val requiredRank: Rank? = null,
    /** unlocked only at the Legend rank */
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
        purchaseKey = UiTheme.Sketch, // bundled with Sketch
        bundleLabel = "Sketch theme"
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
        theme = UiTheme.Fire,
        name = "Fire",
        primaryColor = Color(0xFFFF6B35),
        secondaryColor = Color(0xFFFFC15E),
        backgroundColor = Color(0xFF1C0A05),
        isPremium = true,
        bundleLabel = "Elements pack"
    ),
    ThemeOption(
        theme = UiTheme.Water,
        name = "Water",
        primaryColor = Color(0xFF4DD0E1),
        secondaryColor = Color(0xFF80DEEA),
        backgroundColor = Color(0xFF041F24),
        isPremium = true,
        purchaseKey = UiTheme.Fire,
        bundleLabel = "Elements pack"
    ),
    ThemeOption(
        theme = UiTheme.Wind,
        name = "Wind",
        primaryColor = Color(0xFF4A7A8C),
        secondaryColor = Color(0xFF7FA8B8),
        backgroundColor = Color(0xFFF2F7F7),
        isPremium = true,
        purchaseKey = UiTheme.Fire,
        bundleLabel = "Elements pack"
    ),
    ThemeOption(
        theme = UiTheme.Earth,
        name = "Earth",
        primaryColor = Color(0xFFC77B4A),
        secondaryColor = Color(0xFF8FA05A),
        backgroundColor = Color(0xFF171208),
        isPremium = true,
        purchaseKey = UiTheme.Fire,
        bundleLabel = "Elements pack"
    ),
    ThemeOption(
        theme = UiTheme.Avatar,
        name = "Avatar",
        primaryColor = Color(0xFFFF8A50),
        secondaryColor = Color(0xFF55C6D8),
        backgroundColor = Color(0xFF10131A),
        isPremium = true,
        purchaseKey = UiTheme.Fire,
        bundleLabel = "Elements pack"
    ),
    ThemeOption(
        theme = UiTheme.Princess,
        name = "Princess",
        primaryColor = Color(0xFFD6659E),
        secondaryColor = Color(0xFFB388D9),
        backgroundColor = Color(0xFFFDF2F7),
        isPremium = true
    ),
    ThemeOption(
        theme = UiTheme.Scorched,
        name = "Scorched",
        primaryColor = Color(0xFFE85D2F),
        secondaryColor = Color(0xFFB0A080),
        backgroundColor = Color(0xFF16130F),
        isPremium = true
    ),
    ThemeOption(
        theme = UiTheme.Bronze,
        name = "Bronze",
        primaryColor = Color(0xFFCD7F32),
        secondaryColor = Color(0xFFE0A878),
        backgroundColor = Color(0xFF150E08),
        requiredRank = Rank.Bronze1
    ),
    ThemeOption(
        theme = UiTheme.Silver,
        name = "Silver",
        primaryColor = Color(0xFFC0C8D0),
        secondaryColor = Color(0xFF8A94A0),
        backgroundColor = Color(0xFF101214),
        requiredRank = Rank.Silver1
    ),
    ThemeOption(
        theme = UiTheme.Gold,
        name = "Gold",
        primaryColor = Color(0xFFFFD24A),
        secondaryColor = Color(0xFFE8B820),
        backgroundColor = Color(0xFF141005),
        requiredRank = Rank.Gold1
    ),
    ThemeOption(
        theme = UiTheme.Platinum,
        name = "Platinum",
        primaryColor = Color(0xFFD8E8E8),
        secondaryColor = Color(0xFF9FB8B8),
        backgroundColor = Color(0xFF0E1216),
        requiredRank = Rank.Platinum1
    ),
    ThemeOption(
        theme = UiTheme.Diamond,
        name = "Diamond",
        primaryColor = Color(0xFF7FE8FF),
        secondaryColor = Color(0xFFB8D8FF),
        backgroundColor = Color(0xFF071018),
        requiredRank = Rank.Diamond1
    ),
    ThemeOption(
        theme = UiTheme.Ninja,
        name = "Ninja",
        primaryColor = Color(0xFFE8324A),
        secondaryColor = Color(0xFF8A8F98),
        backgroundColor = Color(0xFF0B0B0D),
        isPremium = true
    ),
    ThemeOption(
        theme = UiTheme.Medieval,
        name = "Medieval",
        primaryColor = Color(0xFFC9A227),
        secondaryColor = Color(0xFFC85A5A),
        backgroundColor = Color(0xFF17130C),
        isPremium = true
    ),
    ThemeOption(
        theme = UiTheme.Cyber,
        name = "Cyber",
        primaryColor = Color(0xFFF5D90A),
        secondaryColor = Color(0xFF00E5C7),
        backgroundColor = Color(0xFF0A0E0C),
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
            tint = MaterialTheme.colorScheme.primary
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
    onPickRedeemed: (UiTheme) -> Unit,
    onOpenShop: () -> Unit,
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
                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                )
                TextButton(onClick = onOpenShop, modifier = Modifier.padding(bottom = 8.dp)) {
                    Icon(
                        imageVector = Icons.Filled.Storefront,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(" Theme Shop", fontWeight = FontWeight.Bold)
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(bottom = 32.dp)
                ) {
                    items(themeOptions) { option ->
                        val isUnlocked = ALL_THEMES_UNLOCKED_FOR_TESTING ||
                                when {
                                    option.requiresMastery -> currentRank == Rank.Legend

                                    option.requiredRank != null ->
                                        currentRank.ordinal >= option.requiredRank.ordinal

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
            onUsePick = if (option.isPremium) {
                {
                    onPickRedeemed(option.purchaseKey)
                    previewOption = null
                }
            } else null,
            onDismiss = { previewOption = null }
        )
    }
}

/**
 * The full theme store: supporter bundles up top, every purchasable theme
 * below; tapping a theme opens its live preview with buy / pick actions
 */
@Composable
fun ThemeShopDialog(
    currentTheme: UiTheme,
    purchasedThemes: Set<UiTheme>,
    onThemeSelected: (UiTheme) -> Unit,
    onPickRedeemed: (UiTheme) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var previewOption by remember { mutableStateOf<ThemeOption?>(null) }

    fun buyProduct(productId: String) {
        val activity = context.findActivity()
        val launched = activity != null && ThemeBilling.purchaseProduct(activity, productId)
        if (!launched) toastShort("Google Play is not available right now", context)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                item(span = { GridItemSpan(2) }) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Theme Shop",
                                style = MaterialTheme.typography.headlineMedium
                            )
                            IconButton(onClick = onDismiss) {
                                Icon(Icons.Filled.Close, contentDescription = "Close")
                            }
                        }
                        Text(
                            text = "Buy themes one by one, or grab a supporter bundle — " +
                                    "thank you for keeping this app alive! 💛",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 4.dp, bottom = 10.dp)
                        )
                        if (ThemeShopState.availablePicks > 0) {
                            Text(
                                text = "You have ${ThemeShopState.availablePicks} theme " +
                                        "pick${if (ThemeShopState.availablePicks == 1) "" else "s"} " +
                                        "left — tap a theme to redeem!",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = 10.dp)
                            )
                        }
                        SupporterBundleCard(
                            title = "Supporter",
                            productId = "bundle_supporter_10",
                            fallbackPrice = "$10",
                            description = "Pick any 2 themes",
                            onBuy = ::buyProduct
                        )
                        SupporterBundleCard(
                            title = "Patron",
                            productId = "bundle_supporter_25",
                            fallbackPrice = "$25",
                            description = "Pick any 6 themes",
                            onBuy = ::buyProduct
                        )
                        SupporterBundleCard(
                            title = "Champion",
                            productId = "bundle_supporter_50",
                            fallbackPrice = "$50",
                            description = "Every theme, forever — including future ones on request",
                            onBuy = ::buyProduct
                        )
                        Text(
                            text = "Themes",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                        )
                    }
                }

                items(themeOptions.filter { it.isPremium }) { option ->
                    val isUnlocked = purchasedThemes.contains(option.theme) ||
                            purchasedThemes.contains(option.purchaseKey)
                    ThemeCard(
                        option = option,
                        isSelected = currentTheme == option.theme,
                        isUnlocked = isUnlocked,
                        onClick = {
                            if (isUnlocked) onThemeSelected(option.theme)
                            else previewOption = option
                        }
                    )
                }
            }
        }
    }

    previewOption?.let { option ->
        ThemePreviewDialog(
            option = option,
            onUnlock = {
                val activity = context.findActivity()
                val launched = activity != null &&
                        ThemeBilling.purchase(activity, option.purchaseKey)
                if (!launched) toastShort("Google Play is not available right now", context)
                previewOption = null
            },
            onUsePick = {
                onPickRedeemed(option.purchaseKey)
                previewOption = null
            },
            onDismiss = { previewOption = null }
        )
    }
}

/**
 * One supporter tier row in the shop
 */
@Composable
private fun SupporterBundleCard(
    title: String,
    productId: String,
    fallbackPrice: String,
    description: String,
    onBuy: (String) -> Unit
) {
    val themeStyle = LocalThemeStyle.current
    Card(
        shape = themeStyle.cardShape ?: MaterialTheme.shapes.medium,
        border = themeStyle.cardBorder,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.45f),
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Button(onClick = { onBuy(productId) }) {
                Text(
                    text = ThemeBilling.bundlePrices[productId] ?: fallbackPrice,
                    fontWeight = FontWeight.Bold
                )
            }
        }
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
    onDismiss: () -> Unit,
    onUsePick: (() -> Unit)? = null
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        DetoxRankTheme(theme = option.theme) {
            val style = LocalThemeStyle.current
            val primaryArc = MaterialTheme.colorScheme.primary
            val secondaryArc = MaterialTheme.colorScheme.secondary
            val tertiaryArc = MaterialTheme.colorScheme.tertiary
            Surface(
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .themeTexture(option.theme)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                        border = BorderStroke(
                            1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                        )
                    ) {
                        Text(
                            text = when {
                                option.requiresMastery -> "✦ MASTERY REWARD ✦"
                                option.isPremium -> "✦ PREMIUM THEME ✦"
                                else -> "✦ LEVEL ${option.requiredLevel} REWARD ✦"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                        )
                    }
                    Text(
                        text = option.name,
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    // miniature of the detox clock: the app's signature element
                    Box(contentAlignment = Alignment.Center) {
                        Canvas(modifier = Modifier.size(130.dp).padding(10.dp)) {
                            val stroke = Stroke(width = 7.dp.toPx(), cap = StrokeCap.Round)
                            drawArc(primaryArc, 135f, 270f, false, style = stroke)
                            inset(14.dp.toPx()) {
                                drawArc(secondaryArc, 155f, 230f, false, style = stroke)
                            }
                            inset(28.dp.toPx()) {
                                drawArc(tertiaryArc, 175f, 190f, false, style = stroke)
                            }
                        }
                        Row {
                            Text(
                                "12 ",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                            Text(
                                "34",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
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
                        if (onUsePick != null && ThemeShopState.availablePicks > 0) {
                            TextButton(onClick = onUsePick) {
                                Text(
                                    "Use 1 pick (${ThemeShopState.availablePicks} left)",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        if (option.bundleLabel != null) {
                            Text(
                                text = "Included in the ${option.bundleLabel}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 6.dp)
                            )
                        }
                    } else {
                        Text(
                            text = when {
                                option.requiresMastery -> "Reach the Legend rank to unlock"
                                option.requiredRank != null ->
                                    "Reach the ${option.requiredRank.rankName.substringBefore(" ")} rank to unlock"
                                else -> "Reach level ${option.requiredLevel} to unlock"
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
                                option.requiresMastery -> " Legend rank"
                                option.requiredRank != null ->
                                    " " + option.requiredRank.rankName.substringBefore(" ") + " rank"
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
