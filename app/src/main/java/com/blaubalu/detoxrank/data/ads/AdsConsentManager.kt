package com.blaubalu.detoxrank.data.ads

import android.app.Activity
import androidx.compose.runtime.mutableStateOf
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform

/**
 * Gathers the GDPR ad consent required in the EEA through Google's UMP SDK.
 * The consent form (configured in the AdMob console under Privacy &
 * messaging) is shown once when required; the ad SDK must only start after
 * [gatherConsent] reports that ads may be requested.
 */
object AdsConsentManager {

    /** true when EEA rules require offering a way to revisit the consent choice */
    val privacyOptionsRequired = mutableStateOf(false)

    /** true when ads may be requested (outside the EEA or consent already given) */
    fun canRequestAds(activity: Activity): Boolean =
        UserMessagingPlatform.getConsentInformation(activity).canRequestAds()

    /**
     * Silent startup refresh: updates the consent info without ever showing
     * the form, so the first-launch impression stays clean. [onCanRequestAds]
     * fires only when ads are already allowed (non-EEA or prior consent).
     */
    fun refreshConsentInfo(activity: Activity, onCanRequestAds: () -> Unit) {
        val consentInfo = UserMessagingPlatform.getConsentInformation(activity)
        val params = ConsentRequestParameters.Builder().build()
        consentInfo.requestConsentInfoUpdate(
            activity,
            params,
            {
                updatePrivacyOptionsRequirement(consentInfo)
                if (consentInfo.canRequestAds()) {
                    onCanRequestAds()
                }
            },
            {
                // offline or misconfigured: consent from a previous session still counts
                updatePrivacyOptionsRequirement(consentInfo)
                if (consentInfo.canRequestAds()) {
                    onCanRequestAds()
                }
            }
        )
    }

    /**
     * Shows the consent form if required — called at the moment the user
     * first wants to watch an ad, so the dialog appears in context instead of
     * greeting them at startup
     */
    fun gatherConsent(activity: Activity, onCanRequestAds: () -> Unit) {
        UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { _ ->
            val consentInfo = UserMessagingPlatform.getConsentInformation(activity)
            updatePrivacyOptionsRequirement(consentInfo)
            if (consentInfo.canRequestAds()) {
                onCanRequestAds()
            }
        }
    }

    /** Reopens the consent form so EEA users can change their choice */
    fun showPrivacyOptions(activity: Activity) {
        UserMessagingPlatform.showPrivacyOptionsForm(activity) {}
    }

    private fun updatePrivacyOptionsRequirement(consentInfo: ConsentInformation) {
        privacyOptionsRequired.value = consentInfo.privacyOptionsRequirementStatus ==
                ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED
    }
}
