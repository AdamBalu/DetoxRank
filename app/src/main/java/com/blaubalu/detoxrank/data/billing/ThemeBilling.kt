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
        "theme_pixel" to UiTheme.Pixel
    )

    /** Formatted store price per premium theme, filled once product details load */
    val themePrices = mutableStateMapOf<UiTheme, String>()

    private var billingClient: BillingClient? = null
    private var onThemeUnlocked: ((UiTheme) -> Unit)? = null
    private val productDetails = mutableMapOf<String, ProductDetails>()

    fun isPremium(theme: UiTheme): Boolean = productIdToTheme.containsValue(theme)

    fun init(context: Context, onThemeUnlocked: (UiTheme) -> Unit) {
        if (billingClient != null) return
        this.onThemeUnlocked = onThemeUnlocked
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
        val client = billingClient ?: return false
        val productId = productIdToTheme.entries.firstOrNull { it.value == theme }?.key
            ?: return false
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
        val products = productIdToTheme.keys.map { id ->
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
                    val theme = productIdToTheme[details.productId] ?: return@forEach
                    details.oneTimePurchaseOfferDetails?.formattedPrice?.let { price ->
                        themePrices[theme] = price
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
            productIdToTheme[productId]?.let { theme -> onThemeUnlocked?.invoke(theme) }
        }

        if (!purchase.isAcknowledged) {
            val params = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
            billingClient?.acknowledgePurchase(params) {}
        }
    }
}
