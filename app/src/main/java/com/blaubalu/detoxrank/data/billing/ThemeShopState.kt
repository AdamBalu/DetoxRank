package com.blaubalu.detoxrank.data.billing

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.core.content.edit

/**
 * Device-local shop state: how many theme picks the user still has from
 * supporter bundles. Bundles themselves restore through Play; the picks are
 * granted once per bundle product.
 */
object ThemeShopState {
    private const val PREFS = "theme_shop"
    private const val KEY_PICKS = "available_picks"
    private const val KEY_GRANTED = "granted_bundles"

    private lateinit var prefs: SharedPreferences

    private val _availablePicks = mutableIntStateOf(0)
    val availablePicks: Int get() = _availablePicks.intValue

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        _availablePicks.intValue = prefs.getInt(KEY_PICKS, 0)
    }

    /**
     * Grants a bundle's picks exactly once per product id
     * @return true if this was a new grant
     */
    fun grantBundle(productId: String, picks: Int): Boolean {
        val granted = prefs.getStringSet(KEY_GRANTED, emptySet()) ?: emptySet()
        if (productId in granted) return false
        prefs.edit {
            putStringSet(KEY_GRANTED, granted + productId)
            putInt(KEY_PICKS, _availablePicks.intValue + picks)
        }
        _availablePicks.intValue += picks
        return true
    }

    /**
     * Spends one pick, if available
     * @return true when a pick was consumed
     */
    fun usePick(): Boolean {
        if (_availablePicks.intValue <= 0) return false
        _availablePicks.intValue -= 1
        prefs.edit { putInt(KEY_PICKS, _availablePicks.intValue) }
        return true
    }
}
