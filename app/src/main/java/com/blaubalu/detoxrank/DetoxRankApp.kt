package com.blaubalu.detoxrank

import android.app.Application
import com.blaubalu.detoxrank.data.AppContainer
import com.blaubalu.detoxrank.data.AppDataContainer
import com.blaubalu.detoxrank.data.ads.RewardedAdManager
import com.blaubalu.detoxrank.data.billing.ThemeBilling
import com.blaubalu.detoxrank.data.user.UiTheme
import com.blaubalu.detoxrank.ui.utils.PopupManager
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
        RewardedAdManager.init(this)

        ThemeBilling.init(
            this,
            onThemeUnlocked = { theme -> applicationScope.launch { unlockThemes(listOf(theme)) } },
            onBundleUnlocked = { title, themes ->
                applicationScope.launch { unlockThemes(themes, title) }
            }
        )
    }

    private suspend fun unlockThemes(themes: List<UiTheme>, bundleTitle: String? = null) {
        val user = container.userDataRepository.getUserStream().first()
        val owned = user.purchasedThemes
            .split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .toMutableSet()
        // the Awesome Supporter grants a permanent all-themes flag so that themes
        // added in future updates unlock automatically
        if (bundleTitle != null && themes.isEmpty()) {
            if (owned.add("ALL")) {
                container.userDataRepository.updatePurchasedThemes(owned.joinToString(","))
                PopupManager.showBundleUnlock(bundleTitle)
            }
            return
        }
        val newOnes = themes.filter { it.name !in owned }
        if (owned.addAll(themes.map { it.name })) {
            container.userDataRepository.updatePurchasedThemes(owned.joinToString(","))
            if (bundleTitle != null) {
                PopupManager.showBundleUnlock(bundleTitle)
            } else {
                newOnes.forEach { PopupManager.showThemeUnlock(it.name) }
            }
        }
    }
}
