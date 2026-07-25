package com.blaubalu.detoxrank.data.ads

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.edit
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.blaubalu.detoxrank.ui.utils.Constants.COINS_PER_AD
import com.blaubalu.detoxrank.ui.utils.Constants.MAX_REWARDED_ADS_PER_DAY
import java.util.Calendar

/**
 * Loads and shows rewarded ads that pay out coins for the theme shop.
 *
 * AdMob places no hard limit on rewarded views, but revenue per user levels
 * off after roughly 5-8 ads a day and ad fill dries up for oversaturated
 * users, so views are capped at [MAX_REWARDED_ADS_PER_DAY] per calendar day
 * (device-local counter).
 */
object RewardedAdManager {

    // Google's public TEST rewarded ad unit — replace with your real AdMob
    // ad unit id (and the app id in AndroidManifest.xml) before release
    private const val AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"

    private const val PREFS = "rewarded_ads"
    private const val KEY_DAY = "day"
    private const val KEY_COUNT = "count"

    private lateinit var prefs: SharedPreferences
    private var rewardedAd: RewardedAd? = null
    private var isLoading = false

    /** true when a loaded ad is ready to show */
    val adReady = mutableStateOf(false)

    /** how many rewarded ads were watched today */
    val watchedToday = mutableIntStateOf(0)

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        watchedToday.intValue = countForToday()
        MobileAds.initialize(context.applicationContext) {}
        loadAd(context.applicationContext)
    }

    fun adsLeftToday(): Int {
        watchedToday.intValue = countForToday()
        return (MAX_REWARDED_ADS_PER_DAY - watchedToday.intValue).coerceAtLeast(0)
    }

    /**
     * Shows a rewarded ad; [onReward] receives the earned coin amount
     * @return false when no ad is ready or the daily cap is reached
     */
    fun showAd(activity: Activity, onReward: (Int) -> Unit): Boolean {
        if (adsLeftToday() <= 0) return false
        val ad = rewardedAd ?: return false

        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                rewardedAd = null
                adReady.value = false
                loadAd(activity.applicationContext)
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                rewardedAd = null
                adReady.value = false
                loadAd(activity.applicationContext)
            }
        }
        ad.show(activity) {
            bumpTodayCount()
            onReward(COINS_PER_AD)
        }
        return true
    }

    private fun loadAd(context: Context) {
        if (isLoading || rewardedAd != null) return
        isLoading = true
        RewardedAd.load(
            context,
            AD_UNIT_ID,
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    adReady.value = true
                    isLoading = false
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    rewardedAd = null
                    adReady.value = false
                    isLoading = false
                }
            }
        )
    }

    private fun todayKey(): String {
        val cal = Calendar.getInstance()
        return "${cal.get(Calendar.YEAR)}-${cal.get(Calendar.DAY_OF_YEAR)}"
    }

    private fun countForToday(): Int =
        if (prefs.getString(KEY_DAY, "") == todayKey()) prefs.getInt(KEY_COUNT, 0) else 0

    private fun bumpTodayCount() {
        val count = countForToday() + 1
        prefs.edit {
            putString(KEY_DAY, todayKey())
            putInt(KEY_COUNT, count)
        }
        watchedToday.intValue = count
    }
}
