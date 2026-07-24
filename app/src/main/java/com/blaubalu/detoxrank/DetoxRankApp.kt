package com.blaubalu.detoxrank

import android.app.Application
import com.blaubalu.detoxrank.data.AppContainer
import com.blaubalu.detoxrank.data.AppDataContainer
import com.blaubalu.detoxrank.data.billing.ThemeBilling
import com.blaubalu.detoxrank.data.billing.ThemeShopState
import com.blaubalu.detoxrank.data.user.UiTheme
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@HiltAndroidApp
class DetoxRankApp: Application() {
    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
        ThemeShopState.init(this)

        ThemeBilling.init(
            this,
            onThemeUnlocked = { theme -> applicationScope.launch { unlockThemes(listOf(theme)) } },
            onBundlePurchased = { productId ->
                val picks = ThemeBilling.bundlePicks[productId] ?: return@init
                if (picks == 0) {
                    // top tier: unlock every purchasable theme
                    applicationScope.launch { unlockThemes(ThemeBilling.allPurchasableThemes) }
                } else {
                    ThemeShopState.grantBundle(productId, picks)
                }
            }
        )
    }

    private suspend fun unlockThemes(themes: List<UiTheme>) {
        val user = container.userDataRepository.getUserStream().first()
        val owned = user.purchasedThemes
            .split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .toMutableSet()
        if (owned.addAll(themes.map { it.name })) {
            container.userDataRepository.updatePurchasedThemes(owned.joinToString(","))
        }
    }
}
