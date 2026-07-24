package com.blaubalu.detoxrank

import android.app.Application
import com.blaubalu.detoxrank.data.AppContainer
import com.blaubalu.detoxrank.data.AppDataContainer
import com.blaubalu.detoxrank.data.billing.ThemeBilling
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

        ThemeBilling.init(this) { theme ->
            applicationScope.launch {
                val user = container.userDataRepository.getUserStream().first()
                val owned = user.purchasedThemes
                    .split(",")
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
                    .toMutableSet()
                if (owned.add(theme.name)) {
                    container.userDataRepository.updatePurchasedThemes(owned.joinToString(","))
                }
            }
        }
    }
}
