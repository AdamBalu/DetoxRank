package com.blaubalu.detoxrank.data.billing

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.mutableStateMapOf
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.blaubalu.detoxrank.data.user.UiTheme
import com.blaubalu.detoxrank.ui.utils.Constants

/**
 * Handles Google Play Billing for premium themes.
 *
 * The product ids below must exist as one-time in-app products in the Play Console.
 * A successful (or restored) purchase is reported through [onThemeUnlocked], which
 * persists the theme into the user's `purchased_themes` column.
 */
object ThemeBilling : PurchasesUpdatedListener {

    private val productIdToTheme = mapOf(
        "theme_luxury" to UiTheme.Luxury,
        "theme_comic" to UiTheme.Comic,
        "theme_sketch" to UiTheme.Sketch, // also unlocks the Paper variant
        "theme_cartoon" to UiTheme.Cartoon,
        "theme_blueprint" to UiTheme.Blueprint,
        "theme_pixel" to UiTheme.Pixel,
        "theme_fire" to UiTheme.Fire,
        "theme_water" to UiTheme.Water,
        "theme_wind" to UiTheme.Wind,
        "theme_earth" to UiTheme.Earth,
        "theme_avatar" to UiTheme.Avatar,
        "theme_princess" to UiTheme.Princess,
        "theme_scorched" to UiTheme.Scorched,
        "theme_ninja" to UiTheme.Ninja,
        "theme_medieval" to UiTheme.Medieval,
        "theme_cyber" to UiTheme.Cyber
    )

    /** every theme coverable by a purchase, including bundled variants */
    val allPurchasableThemes = listOf(
        UiTheme.Luxury, UiTheme.Comic, UiTheme.Sketch, UiTheme.Paper, UiTheme.Cartoon,
        UiTheme.Blueprint, UiTheme.Pixel, UiTheme.Fire, UiTheme.Water, UiTheme.Wind,
        UiTheme.Earth, UiTheme.Avatar, UiTheme.Princess, UiTheme.Scorched,
        UiTheme.Ninja, UiTheme.Medieval, UiTheme.Cyber
    )

    /**
     * A curated pack of themes sold as one Play product.
     * An empty [themes] list means "every purchasable theme".
     */
    data class ThemeBundle(
        val productId: String,
        val title: String,
        val tagline: String,
        val fallbackPrice: String,
        val themes: List<UiTheme>
    )

    /**
     * Curated theme bundles shown in the shop; the last one unlocks everything.
     *
     * Pricing (set the real prices in the Play Console, these are display
     * fallbacks): singles EUR 3-5, bundles ~25% under their singles' sum,
     * Awesome Supporter = the whole collection (~EUR 69 bought singly) at
     * a bigger discount but never below EUR 40.
     */
    val themeBundles = listOf(
        ThemeBundle(
            productId = "theme_elements",
            title = "Avatar Bundle",
            tagline = "Fire, Water, Wind & Earth + the Avatar theme",
            fallbackPrice = "€14.99",
            themes = listOf(
                UiTheme.Fire, UiTheme.Water, UiTheme.Wind, UiTheme.Earth, UiTheme.Avatar
            )
        ),
        ThemeBundle(
            productId = "bundle_drawing",
            title = "Drawing Bundle",
            tagline = "Sketch, Paper, Comic & Cartoon",
            fallbackPrice = "€9.99",
            themes = listOf(UiTheme.Sketch, UiTheme.Paper, UiTheme.Comic, UiTheme.Cartoon)
        ),
        ThemeBundle(
            productId = "bundle_battle",
            title = "Battle Bundle",
            tagline = "Scorched, Ninja & Medieval",
            fallbackPrice = "€9.99",
            themes = listOf(UiTheme.Scorched, UiTheme.Ninja, UiTheme.Medieval)
        ),
        ThemeBundle(
            productId = "bundle_future",
            title = "Future Bundle",
            tagline = "Blueprint, Pixel & Cyber",
            fallbackPrice = "€9.99",
            themes = listOf(UiTheme.Blueprint, UiTheme.Pixel, UiTheme.Cyber)
        ),
        ThemeBundle(
            productId = "bundle_royal",
            title = "Royal Bundle",
            tagline = "Luxury & Princess",
            fallbackPrice = "€7.99",
            themes = listOf(UiTheme.Luxury, UiTheme.Princess)
        ),
        ThemeBundle(
            productId = "bundle_supporter_50",
            title = "Awesome Supporter",
            tagline = "Every theme, forever — including future ones",
            fallbackPrice = "€40",
            themes = emptyList()
        )
    )

    /** fallback € price per single-theme purchase until Play details load */
    private val fallbackThemePrices = mapOf(
        UiTheme.Luxury to "€4.99",
        UiTheme.Comic to "€3.99",
        UiTheme.Sketch to "€4.99", // includes Paper
        UiTheme.Cartoon to "€3.99",
        UiTheme.Blueprint to "€2.99",
        UiTheme.Pixel to "€3.99",
        UiTheme.Fire to "€3.99",
        UiTheme.Water to "€3.99",
        UiTheme.Wind to "€3.99",
        UiTheme.Earth to "€3.99",
        UiTheme.Avatar to "€4.99",
        UiTheme.Princess to "€4.99",
        UiTheme.Scorched to "€3.99",
        UiTheme.Ninja to "€3.99",
        UiTheme.Medieval to "€4.99",
        UiTheme.Cyber to "€4.99"
    )

    /** the display price for a theme purchase: live Play price, fallback, or the bundle it sells in */
    fun priceFor(theme: UiTheme): String? =
        themePrices[theme]
            ?: fallbackThemePrices[theme]
            ?: themeBundles.firstOrNull { theme in it.themes }
                ?.let { bundlePrices[it.productId] ?: it.fallbackPrice }

    /** coin price per theme purchase, ~175 coins for each EUR of the cash price */
    private val themeCoinCosts = mapOf(
        UiTheme.Luxury to 900,
        UiTheme.Comic to 700,
        UiTheme.Sketch to 900, // includes Paper
        UiTheme.Cartoon to 700,
        UiTheme.Blueprint to 500,
        UiTheme.Pixel to 700,
        UiTheme.Fire to 700,
        UiTheme.Water to 700,
        UiTheme.Wind to 700,
        UiTheme.Earth to 700,
        UiTheme.Avatar to 900,
        UiTheme.Princess to 900,
        UiTheme.Scorched to 700,
        UiTheme.Ninja to 700,
        UiTheme.Medieval to 900,
        UiTheme.Cyber to 900
    )

    fun coinCostFor(theme: UiTheme): Int = themeCoinCosts[theme] ?: 700

    /** formatted store price per bundle product */
    val bundlePrices = mutableStateMapOf<String, String>()

    private var onBundleUnlocked: ((title: String, themes: List<UiTheme>) -> Unit)? = null

    /** Formatted store price per premium theme, filled once product details load */
    val themePrices = mutableStateMapOf<UiTheme, String>()

    private var billingClient: BillingClient? = null
    private var onThemeUnlocked: ((UiTheme) -> Unit)? = null
    private val productDetails = mutableMapOf<String, ProductDetails>()

    fun isPremium(theme: UiTheme): Boolean = productIdToTheme.containsValue(theme)

    fun init(
        context: Context,
        onThemeUnlocked: (UiTheme) -> Unit,
        onBundleUnlocked: (title: String, themes: List<UiTheme>) -> Unit
    ) {
        if (billingClient != null) return
        this.onThemeUnlocked = onThemeUnlocked
        this.onBundleUnlocked = onBundleUnlocked
        val client = BillingClient.newBuilder(context.applicationContext)
            .setListener(this)
            .enablePendingPurchases(
                PendingPurchasesParams.newBuilder().enableOneTimeProducts().build()
            )
            .build()
        billingClient = client
        client.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryThemeProducts()
                    restorePurchases()
                }
            }

            override fun onBillingServiceDisconnected() {
                // the client reconnects lazily on the next purchase attempt
            }
        })
    }

    /**
     * Launches the Play purchase dialog for the given premium theme. Themes
     * without their own product (the elements) buy the bundle containing them.
     * @return false when billing is unavailable (no Play Store / products not loaded)
     */
    fun purchase(activity: Activity, theme: UiTheme): Boolean {
        val productId = productIdToTheme.entries.firstOrNull { it.value == theme }?.key
            ?: themeBundles.firstOrNull { theme in it.themes }?.productId
            ?: return false
        return purchaseProduct(activity, productId)
    }

    /**
     * Launches the Play purchase dialog for any known product (theme or bundle)
     * @return false when billing is unavailable
     */
    fun purchaseProduct(activity: Activity, productId: String): Boolean {
        if (Constants.FAKE_BILLING_FOR_TESTING) {
            simulatePurchase(productId)
            return true
        }
        val client = billingClient ?: return false
        val details = productDetails[productId] ?: return false
        if (!client.isReady) return false

        val params = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(
                listOf(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(details)
                        .build()
                )
            )
            .build()
        return client.launchBillingFlow(activity, params).responseCode ==
                BillingClient.BillingResponseCode.OK
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            purchases?.forEach { handlePurchase(it) }
        }
    }

    /** Testing shortcut: unlocks as if Play reported a successful purchase */
    private fun simulatePurchase(productId: String) {
        val bundle = themeBundles.firstOrNull { it.productId == productId }
        if (bundle != null) {
            onBundleUnlocked?.invoke(bundle.title, bundle.themes)
        } else {
            productIdToTheme[productId]?.let { onThemeUnlocked?.invoke(it) }
        }
    }

    private fun queryThemeProducts() {
        val products = (productIdToTheme.keys + themeBundles.map { it.productId }).map { id ->
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(id)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        }
        val params = QueryProductDetailsParams.newBuilder().setProductList(products).build()
        billingClient?.queryProductDetailsAsync(params) { result, detailsList ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                detailsList.forEach { details ->
                    productDetails[details.productId] = details
                    val price = details.oneTimePurchaseOfferDetails?.formattedPrice
                    if (price != null) {
                        productIdToTheme[details.productId]?.let { themePrices[it] = price }
                        if (themeBundles.any { it.productId == details.productId }) {
                            bundlePrices[details.productId] = price
                        }
                    }
                }
            }
        }
    }

    /** Unlocks already bought themes again, e.g. after a reinstall */
    private fun restorePurchases() {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()
        billingClient?.queryPurchasesAsync(params) { result, purchases ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                purchases.forEach { handlePurchase(it) }
            }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState != Purchase.PurchaseState.PURCHASED) return

        purchase.products.forEach { productId ->
            val bundle = themeBundles.firstOrNull { it.productId == productId }
            if (bundle != null) {
                // an empty theme list means "everything, forever" — the app stores
                // a permanent all-themes flag instead of a snapshot of today's themes
                onBundleUnlocked?.invoke(bundle.title, bundle.themes)
            } else {
                productIdToTheme[productId]?.let { theme -> onThemeUnlocked?.invoke(theme) }
            }
        }

        if (!purchase.isAcknowledged) {
            val params = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
            billingClient?.acknowledgePurchase(params) {}
        }
    }
}
