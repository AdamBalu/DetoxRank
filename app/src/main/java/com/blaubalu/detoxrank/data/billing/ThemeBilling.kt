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
        "theme_elements" to UiTheme.Fire, // unlocks Fire, Water, Wind and Earth
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

    /** curated theme bundles shown in the shop; the last one unlocks everything */
    val themeBundles = listOf(
        ThemeBundle(
            productId = "theme_elements",
            title = "Avatar Bundle",
            tagline = "Fire, Water, Wind & Earth + the Avatar theme",
            fallbackPrice = "$8",
            themes = listOf(
                UiTheme.Fire, UiTheme.Water, UiTheme.Wind, UiTheme.Earth, UiTheme.Avatar
            )
        ),
        ThemeBundle(
            productId = "bundle_drawing",
            title = "Drawing Bundle",
            tagline = "Sketch, Paper, Comic & Cartoon",
            fallbackPrice = "$8",
            themes = listOf(UiTheme.Sketch, UiTheme.Paper, UiTheme.Comic, UiTheme.Cartoon)
        ),
        ThemeBundle(
            productId = "bundle_battle",
            title = "Battle Bundle",
            tagline = "Scorched, Ninja & Medieval",
            fallbackPrice = "$7",
            themes = listOf(UiTheme.Scorched, UiTheme.Ninja, UiTheme.Medieval)
        ),
        ThemeBundle(
            productId = "bundle_future",
            title = "Future Bundle",
            tagline = "Blueprint, Pixel & Cyber",
            fallbackPrice = "$7",
            themes = listOf(UiTheme.Blueprint, UiTheme.Pixel, UiTheme.Cyber)
        ),
        ThemeBundle(
            productId = "bundle_royal",
            title = "Royal Bundle",
            tagline = "Luxury & Princess",
            fallbackPrice = "$5",
            themes = listOf(UiTheme.Luxury, UiTheme.Princess)
        ),
        ThemeBundle(
            productId = "bundle_supporter_50",
            title = "Awesome Supporter",
            tagline = "Every theme, forever — including future ones on request",
            fallbackPrice = "$50",
            themes = emptyList()
        )
    )

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
     * Launches the Play purchase dialog for the given premium theme
     * @return false when billing is unavailable (no Play Store / products not loaded)
     */
    fun purchase(activity: Activity, theme: UiTheme): Boolean {
        val productId = productIdToTheme.entries.firstOrNull { it.value == theme }?.key
            ?: return false
        return purchaseProduct(activity, productId)
    }

    /**
     * Launches the Play purchase dialog for any known product (theme or bundle)
     * @return false when billing is unavailable
     */
    fun purchaseProduct(activity: Activity, productId: String): Boolean {
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
                val themes = bundle.themes.ifEmpty { allPurchasableThemes }
                onBundleUnlocked?.invoke(bundle.title, themes)
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
