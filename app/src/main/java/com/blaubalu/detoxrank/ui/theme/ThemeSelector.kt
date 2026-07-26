package com.blaubalu.detoxrank.ui.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.asPaddingValues
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
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Cyclone
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.blaubalu.detoxrank.data.ads.AdsConsentManager
import com.blaubalu.detoxrank.data.ads.RewardedAdManager
import com.blaubalu.detoxrank.data.billing.ThemeBilling
import com.blaubalu.detoxrank.data.user.Rank
import com.blaubalu.detoxrank.data.user.UiTheme
import com.blaubalu.detoxrank.ui.utils.PanelHeader
import com.blaubalu.detoxrank.ui.utils.Constants.ALL_THEMES_UNLOCKED_FOR_TESTING
import com.blaubalu.detoxrank.ui.utils.Constants.COINS_PER_AD
import com.blaubalu.detoxrank.ui.utils.Constants.MAX_REWARDED_ADS_PER_DAY
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
        isPremium = true
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
        isPremium = true
    ),
    ThemeOption(
        theme = UiTheme.Water,
        name = "Water",
        primaryColor = Color(0xFF4DD0E1),
        secondaryColor = Color(0xFF80DEEA),
        backgroundColor = Color(0xFF041F24),
        isPremium = true
    ),
    ThemeOption(
        theme = UiTheme.Wind,
        name = "Wind",
        primaryColor = Color(0xFF4A7A8C),
        secondaryColor = Color(0xFF7FA8B8),
        backgroundColor = Color(0xFFF2F7F7),
        isPremium = true
    ),
    ThemeOption(
        theme = UiTheme.Earth,
        name = "Earth",
        primaryColor = Color(0xFFC77B4A),
        secondaryColor = Color(0xFF8FA05A),
        backgroundColor = Color(0xFF171208),
        isPremium = true
    ),
    ThemeOption(
        theme = UiTheme.Avatar,
        name = "Avatar",
        primaryColor = Color(0xFFFF8A50),
        secondaryColor = Color(0xFF55C6D8),
        backgroundColor = Color(0xFF10131A),
        isPremium = true
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
        theme = UiTheme.Glass,
        name = "Glass",
        primaryColor = Color(0xFF55C0F0),
        secondaryColor = Color(0xFFB58CF0),
        backgroundColor = Color(0xFFEAF1F8),
        isPremium = true
    ),
    ThemeOption(
        theme = UiTheme.Master,
        // internally UiTheme.Master, but the highest rank is Legend — named
        // after the rank that actually unlocks it
        name = "Legend",
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
    coins: Int,
    onCoinsEarned: (Int) -> Unit,
    onCoinUnlock: (UiTheme) -> Unit,
    onThemeSelected: (UiTheme) -> Unit,
    onRedeemCode: (String, (String) -> Unit) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var previewOption by remember { mutableStateOf<ThemeOption?>(null) }
    var showRedeemDialog by remember { mutableStateOf(false) }
    var showFontCredits by remember { mutableStateOf(false) }
    var ownedOnly by remember { mutableStateOf(false) }
    var shopTab by remember { mutableStateOf(ShopTab.Themes) }
    // captured here: dialog windows report zero system-bar insets to their
    // own content, so we pad with the activity-scope insets instead
    val systemBars = WindowInsets.systemBars.asPaddingValues()

    fun buyProduct(productId: String) {
        val activity = context.findActivity()
        val launched = activity != null && ThemeBilling.purchaseProduct(activity, productId)
        if (!launched) toastShort("Google Play is not available right now", context)
    }

    // resolve each theme's unlocked state up front so the "owned only" filter
    // and the cards stay in agreement
    val themeEntries = themeOptions.map { option ->
        val unlocked = ALL_THEMES_UNLOCKED_FOR_TESTING ||
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
        option to unlocked
    }
    val visibleThemes = if (ownedOnly) themeEntries.filter { it.second } else themeEntries

    if (isVisible) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier.fillMaxSize()
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        // the dialog decor insets below the status bar already; only
                        // the nav bar (drawn behind the content) needs clearance
                        top = 8.dp,
                        bottom = systemBars.calculateBottomPadding() + 40.dp
                    )
                ) {
                    item(span = { GridItemSpan(2) }) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            PanelHeader(
                                title = "Themes",
                                onClose = onDismiss,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                            ShopTabs(
                                selected = shopTab,
                                onSelect = { shopTab = it },
                                modifier = Modifier.padding(top = 6.dp)
                            )
                            // coins spend on themes only; the bundles tab stays
                            // money-focused with a short pitch in place of the wallet
                            if (shopTab == ShopTab.Themes) {
                                // wallet on the left, the single catalog filter on the
                                // right — one tidy toolbar instead of stacked controls
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 12.dp, start = 4.dp, end = 4.dp)
                                ) {
                                    CoinBalanceRow(coins = coins, onCoinsEarned = onCoinsEarned)
                                    FilterChip(
                                        selected = ownedOnly,
                                        onClick = { ownedOnly = !ownedOnly },
                                        label = { Text("Owned only") },
                                        leadingIcon = if (ownedOnly) {
                                            {
                                                Icon(
                                                    imageVector = Icons.Filled.Check,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        } else null
                                    )
                                }
                            } else {
                                Text(
                                    text = "Save on a set, or unlock everything",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(top = 10.dp)
                                )
                            }
                        }
                    }

                    if (shopTab == ShopTab.Themes) {
                        items(visibleThemes) { (option, isUnlocked) ->
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
                    } else {
                        items(
                            items = ThemeBilling.themeBundles,
                            span = { GridItemSpan(2) }
                        ) { bundle ->
                            ThemeBundleCard(bundle = bundle, onBuy = ::buyProduct)
                        }
                    }

                    // utility links live at the foot of both tabs, on one quiet line
                    item(span = { GridItemSpan(2) }) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp)
                        ) {
                            ShopFooterLink(text = "Redeem code") { showRedeemDialog = true }
                            ShopFooterDot()
                            // EEA users must be able to revisit their ad consent choice
                            if (AdsConsentManager.privacyOptionsRequired.value) {
                                ShopFooterLink(text = "Ad privacy") {
                                    context.findActivity()?.let {
                                        AdsConsentManager.showPrivacyOptions(it)
                                    }
                                }
                                ShopFooterDot()
                            }
                            ShopFooterLink(text = "Fonts") { showFontCredits = true }
                        }
                    }
                }
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
            coins = coins,
            onCoinUnlock = if (option.isPremium) {
                {
                    onCoinUnlock(option.purchaseKey)
                    previewOption = null
                }
            } else null,
            onDismiss = { previewOption = null }
        )
    }

    if (showRedeemDialog) {
        RedeemCodeDialog(
            onRedeem = onRedeemCode,
            onDismiss = { showRedeemDialog = false }
        )
    }

    if (showFontCredits) {
        FontCreditsDialog(onDismiss = { showFontCredits = false })
    }
}

/**
 * Attribution for the bundled theme typefaces. Every font shipped with the app
 * is a Google Fonts release under the SIL Open Font License, which asks that the
 * fonts be credited; this dialog is that credit.
 */
@Composable
private fun FontCreditsDialog(onDismiss: () -> Unit) {
    // font name to author(s), as published on Google Fonts
    val fonts = listOf(
        "Baloo 2" to "Ek Type",
        "Bangers" to "Vernon Adams",
        "Black Ops One" to "Google",
        "Cinzel" to "Natanael Gama",
        "Comic Neue" to "Craig Rozynski",
        "DM Sans" to "Colophon Foundry",
        "EB Garamond" to "Georg Duffner, Octavio Pardo",
        "Great Vibes" to "TypeSETit",
        "Josefin Sans" to "Santiago Orozco",
        "Marcellus" to "Astigmatic",
        "Oswald" to "Vernon Adams, Kalapi Gajjar, Cyreal",
        "Patrick Hand" to "Patrick Wagesreiter",
        "Philosopher" to "Jovanny Lemonad",
        "Press Start 2P" to "CodeMan38",
        "Quicksand" to "Andrew Paglinawan",
        "Share Tech Mono" to "Carrois Apostrophe",
        "VT323" to "Peter Hull"
    )
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Fonts & licenses",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "The typefaces used across DetoxRank's themes are provided by their authors under the SIL Open Font License (OFL).",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
                )
                Column(
                    modifier = Modifier
                        .heightIn(max = 320.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    fonts.forEach { (name, author) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = name,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = author,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.End,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
                Text(
                    text = "SIL Open Font License, Version 1.1 — scripts.sil.org/OFL",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 12.dp)
                )
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 8.dp)
                ) {
                    Text(text = "Close")
                }
            }
        }
    }
}

/**
 * Small prompt where a promo code can be typed in and redeemed
 */
@Composable
private fun RedeemCodeDialog(
    onRedeem: (String, (String) -> Unit) -> Unit,
    onDismiss: () -> Unit
) {
    var code by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Redeem Code",
                    style = MaterialTheme.typography.headlineSmall
                )
                OutlinedTextField(
                    value = code,
                    onValueChange = {
                        code = it
                        message = null
                    },
                    singleLine = true,
                    placeholder = { Text("YOUR-CODE") },
                    modifier = Modifier.padding(top = 14.dp)
                )
                message?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
                Row(modifier = Modifier.padding(top = 14.dp)) {
                    TextButton(onClick = onDismiss) {
                        Text("Close")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onRedeem(code) { result -> message = result } },
                        enabled = code.isNotBlank()
                    ) {
                        Text("Redeem", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

/**
 * Coin balance chip + the rewarded-ad earn button; coins exist only here in
 * the theme UIs and can pay for premium themes instead of real money
 */
@Composable
private fun CoinBalanceRow(
    coins: Int,
    onCoinsEarned: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coinColor = Color(0xFFE5C558)
    val adsLeft =
        (MAX_REWARDED_ADS_PER_DAY - RewardedAdManager.watchedToday.intValue).coerceAtLeast(0)
    val canEarn = adsLeft > 0
    // one connected pill: the wallet balance on the left, a tap-to-earn segment
    // on the right, so earning coins reads as part of the wallet rather than a
    // second free-floating button
    Surface(
        shape = CircleShape,
        color = coinColor.copy(alpha = 0.15f),
        border = BorderStroke(1.dp, coinColor.copy(alpha = 0.5f)),
        modifier = modifier
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.MonetizationOn,
                contentDescription = null,
                tint = coinColor,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(18.dp)
            )
            Text(
                text = "$coins",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 5.dp, end = 10.dp)
            )
            Box(
                modifier = Modifier
                    .height(20.dp)
                    .width(1.dp)
                    .background(coinColor.copy(alpha = 0.35f))
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(enabled = canEarn) {
                        val activity = context.findActivity()
                        when {
                            activity == null -> {}

                            adsLeft <= 0 -> {
                                toastShort("Daily ad limit reached — come back tomorrow!", context)
                            }

                            !AdsConsentManager.canRequestAds(activity) -> {
                                // first ad in the EEA: ask for consent right when it matters
                                AdsConsentManager.gatherConsent(activity) {
                                    RewardedAdManager.startAds(activity)
                                    toastShort(
                                        "Thanks! Your ad is loading — tap again in a moment",
                                        context
                                    )
                                }
                            }

                            else -> {
                                val shown = RewardedAdManager.showAd(activity) { onCoinsEarned(it) }
                                if (!shown) {
                                    toastShort(
                                        "No ad available right now, try again in a moment",
                                        context
                                    )
                                }
                            }
                        }
                    }
                    .alpha(if (canEarn) 1f else 0.4f)
                    .padding(start = 10.dp, end = 12.dp, top = 6.dp, bottom = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.OndemandVideo,
                    contentDescription = "Watch an ad to earn coins",
                    tint = coinColor,
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = " +$COINS_PER_AD",
                    fontWeight = FontWeight.Bold,
                    color = coinColor,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

/** the two faces of the shop: pick a single theme, or buy a curated pack */
private enum class ShopTab(val label: String, val icon: ImageVector) {
    Themes("Themes", Icons.Filled.Palette),
    Bundles("Bundles", Icons.Filled.AutoAwesome)
}

/**
 * Pill switch between the Themes catalog and the Bundles storefront. The
 * selected side fills with the primary color and animates on change so the
 * control reads as a real segmented tab rather than two loose buttons.
 */
@Composable
private fun ShopTabs(
    selected: ShopTab,
    onSelect: (ShopTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(percent = 50),
        color = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
        modifier = modifier
    ) {
        Row(modifier = Modifier.padding(4.dp)) {
            ShopTab.entries.forEach { tab ->
                val isSelected = tab == selected
                val bg by animateColorAsState(
                    targetValue = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.Transparent
                    },
                    label = "shopTabBg"
                )
                val fg by animateColorAsState(
                    targetValue = if (isSelected) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    label = "shopTabFg"
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(percent = 50))
                        .background(bg)
                        .clickable { onSelect(tab) }
                        .padding(horizontal = 22.dp, vertical = 9.dp)
                ) {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = null,
                        tint = fg,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = tab.label,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = fg
                    )
                }
            }
        }
    }
}

/** a quiet, tappable text link for the row of utility actions at the screen foot */
@Composable
private fun ShopFooterLink(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp)
    )
}

/** middot separating the footer links */
@Composable
private fun ShopFooterDot() {
    Text(
        text = "·",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
    )
}

/** distinctive glyph per bundle so each pack is recognizable at a glance */
private fun bundleIcon(productId: String): ImageVector = when (productId) {
    "bundle_avatar" -> Icons.Filled.Cyclone
    "bundle_drawing" -> Icons.Filled.Draw
    "bundle_battle" -> Icons.Filled.Shield
    "bundle_future" -> Icons.Filled.Memory
    "bundle_royal" -> Icons.Filled.Diamond
    else -> Icons.Filled.AutoAwesome
}

/** accent color giving each bundle its own identity */
private fun bundleAccent(productId: String): Color = when (productId) {
    "bundle_avatar" -> Color(0xFF4FC3F7)
    "bundle_drawing" -> Color(0xFFFFB74D)
    "bundle_battle" -> Color(0xFFEF5350)
    "bundle_future" -> Color(0xFF9575FF)
    "bundle_royal" -> Color(0xFFE5C558)
    else -> Color(0xFFFFD54F)
}

/**
 * One curated theme bundle in the shop, sold as a single Play product. The card
 * previews the pack as a ribbon of its real theme palettes so the reward is
 * obvious at a glance; the all-unlocking Supporter tier gets a rainbow border
 * and a value flag so it stands apart.
 */
@Composable
private fun ThemeBundleCard(
    bundle: ThemeBilling.ThemeBundle,
    onBuy: (String) -> Unit
) {
    val themeStyle = LocalThemeStyle.current
    val isDark = LocalThemeIsDark.current
    val accent = bundleAccent(bundle.productId)
    // the raw accents are tuned for dark surfaces; ink them down on light ones
    val accentInk = if (isDark) accent else lerp(accent, Color.Black, 0.35f)
    val isEverything = bundle.themes.isEmpty()

    // the pack previewed as its real theme palettes — its own themes, or a broad
    // sweep of the whole collection for the everything tier
    val previewThemes = if (isEverything) {
        themeOptions.filter { it.isPremium }
    } else {
        bundle.themes.mapNotNull { t -> themeOptions.firstOrNull { it.theme == t } }
    }
    val themeCount = if (isEverything) themeOptions.count { it.isPremium } else bundle.themes.size
    val rainbow = listOf(
        Color(0xFFE5C558), Color(0xFFFF7AC6), Color(0xFF4FC3F7),
        Color(0xFF81C784), Color(0xFFE5C558)
    )
    val tintAlpha = if (isDark) 0.14f else 0.08f

    Card(
        onClick = { onBuy(bundle.productId) },
        shape = themeStyle.cardShape ?: RoundedCornerShape(20.dp),
        border = if (isEverything) {
            BorderStroke(2.dp, Brush.sweepGradient(rainbow))
        } else {
            BorderStroke(1.5.dp, accentInk.copy(alpha = if (isDark) 0.45f else 0.55f))
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp),
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        if (isEverything) {
                            rainbow.map { it.copy(alpha = tintAlpha) }
                        } else {
                            listOf(accent.copy(alpha = tintAlpha * 2f), Color.Transparent)
                        }
                    )
                )
                .padding(16.dp)
        ) {
            // identity row: glyph + title, with the value flag on the top tier
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = bundleIcon(bundle.productId),
                    contentDescription = null,
                    tint = accentInk,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = bundle.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = accentInk,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                if (isEverything) {
                    Surface(
                        shape = RoundedCornerShape(percent = 50),
                        color = Color(0xFFE5C558).copy(alpha = if (isDark) 0.22f else 0.18f)
                    ) {
                        Text(
                            text = "BEST VALUE",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) Color(0xFFE5C558) else Color(0xFF8A6D00),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            // palette ribbon: a real look at every theme the pack unlocks
            val shown = previewThemes.take(if (previewThemes.size > 6) 5 else 6)
            val remaining = previewThemes.size - shown.size
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                shown.forEach { opt ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(opt.primaryColor, opt.secondaryColor)
                                )
                            )
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
                                RoundedCornerShape(10.dp)
                            )
                    )
                }
                if (remaining > 0) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(accent.copy(alpha = if (isDark) 0.18f else 0.14f))
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
                                RoundedCornerShape(10.dp)
                            )
                    ) {
                        Text(
                            text = "+$remaining",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = accentInk
                        )
                    }
                }
            }

            // tagline + count on the left, the price standing bold on the right
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = bundle.tagline,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isEverything) "Every theme + future ones" else "$themeCount themes",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
                Text(
                    text = ThemeBilling.bundlePrices[bundle.productId] ?: bundle.fallbackPrice,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = accentInk,
                    modifier = Modifier.padding(start = 12.dp)
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
    coins: Int = 0,
    onCoinUnlock: (() -> Unit)? = null
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
                        .systemBarsPadding()
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
                                option.requiredRank != null ->
                                    "✦ ${option.requiredRank.rankName.substringBefore(" ").uppercase()} REWARD ✦"
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
                        Canvas(modifier = Modifier.size(190.dp).padding(10.dp)) {
                            val stroke = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                            drawArc(primaryArc, 135f, 270f, false, style = stroke)
                            inset(20.dp.toPx()) {
                                drawArc(secondaryArc, 155f, 230f, false, style = stroke)
                            }
                            inset(40.dp.toPx()) {
                                drawArc(tertiaryArc, 175f, 190f, false, style = stroke)
                            }
                        }
                        Row {
                            val digitStyle = MaterialTheme.typography.headlineSmall.copy(
                                fontSize = MaterialTheme.typography.headlineSmall.fontSize *
                                        style.timerDigitScale
                            )
                            Text("12 ", style = digitStyle, color = MaterialTheme.colorScheme.tertiary)
                            Text("34", style = digitStyle, color = MaterialTheme.colorScheme.secondary)
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
                        Button(
                            onClick = onUnlock,
                            modifier = Modifier
                                .fillMaxWidth(0.82f)
                                .height(54.dp)
                        ) {
                            val price = ThemeBilling.priceFor(option.purchaseKey)
                            Text(
                                text = if (price != null) "Unlock for $price" else "Unlock",
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            )
                        }
                        if (onCoinUnlock != null) {
                            val coinCost = ThemeBilling.coinCostFor(option.purchaseKey)
                            val canAfford = coins >= coinCost
                            OutlinedButton(
                                onClick = onCoinUnlock,
                                enabled = canAfford,
                                modifier = Modifier
                                    .fillMaxWidth(0.82f)
                                    .padding(top = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.MonetizationOn,
                                    contentDescription = null,
                                    tint = Color(0xFFE5C558),
                                    modifier = Modifier.size(18.dp)
                                )
                                // the coin glyph already reads as "coins", so the
                                // label just mirrors the money button above it
                                Text(
                                    text = " Unlock for $coinCost",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = if (canAfford) {
                                    "You have $coins coins"
                                } else {
                                    "You have $coins coins — earn more by watching ads in the shop"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                        // labelled from bundle membership so every packaged theme
                        // shows its pack, not just the few with a hardcoded label
                        ThemeBilling.bundleFor(option.purchaseKey)?.let { bundle ->
                            Text(
                                text = "Included in the ${bundle.title}",
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

                    TextButton(onClick = onDismiss) {
                        Text(
                            "Close",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
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
        Box {
            style.cardSheen?.let { sheen ->
                Box(modifier = Modifier.matchParentSize().background(sheen))
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 14.dp)
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
        // each swatch previews its theme's own surface: texture + glass sheen
        Box(
            modifier = Modifier
                .fillMaxSize()
                .themeTexture(option.theme)
                .glassCardSheen(themeStyleFor(option.theme).cardSheen)
        ) {
            // overlapping glossy orbs over a soft halo, name below — centered
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.alpha(if (isUnlocked) 1f else 0.5f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(62.dp)
                            .background(
                                Brush.radialGradient(
                                    listOf(option.primaryColor.copy(alpha = 0.30f), Color.Transparent)
                                ),
                                CircleShape
                            )
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy((-10).dp)) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(option.primaryColor)
                                .border(2.dp, Color.White.copy(alpha = 0.7f), CircleShape)
                        )
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(option.secondaryColor)
                                .border(2.dp, Color.White.copy(alpha = 0.7f), CircleShape)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = option.name,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.35f),
                            offset = Offset(0f, 1f),
                            blurRadius = 4f
                        )
                    ),
                    fontWeight = FontWeight.Bold,
                    color = option.primaryColor,
                    textAlign = TextAlign.Center
                )
            }

            // lock requirement as a small corner chip, out of the name's way
            if (!isUnlocked) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color.Black.copy(alpha = 0.38f))
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = "Locked",
                        tint = rank_color,
                        modifier = Modifier.size(11.dp)
                    )
                    Text(
                        text = when {
                            option.requiresMastery -> " Legend"
                            option.requiredRank != null ->
                                " " + option.requiredRank.rankName.substringBefore(" ")
                            option.isPremium ->
                                " " + (ThemeBilling.themePrices[option.purchaseKey] ?: "Premium")
                            else -> " Lvl ${option.requiredLevel}"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = rank_color,
                        fontSize = 9.sp
                    )
                }
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
