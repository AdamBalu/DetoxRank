package com.blaubalu.detoxrank.data.billing

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.security.MessageDigest

/**
 * Promo codes that unlock every theme. Codes are validated against SHA-256
 * hashes so the plaintext never ships in the APK, and the active set is fetched
 * live from a remote list so codes can be added or disabled without an app
 * update.
 *
 * The list must be reachable to redeem: if the device is offline (or the list
 * is missing) the fetch fails and no code is accepted. That is deliberate — it
 * means a code disabled in the list can never be redeemed from a stale copy,
 * and there is no baked-in fallback set to work around.
 *
 * To mint a new code, hash the UPPERCASE trimmed code and add it to the remote
 * JSON — PowerShell:
 *   $sha=[System.Security.Cryptography.SHA256]::Create()
 *   ($sha.ComputeHash([Text.Encoding]::UTF8.GetBytes('MY-NEW-CODE')) |
 *       % { $_.ToString('x2') }) -join ''
 *
 * Without a backend a redemption can't be counted across devices, so a code
 * works on any number of devices (once each); remove it from the remote list to
 * retire it.
 */
object PromoCodes {

    /** the live code list; editable without republishing the app */
    private const val REMOTE_URL = "https://adambalu.github.io/detoxrank/promo-codes.json"

    enum class CodeType { VIP, LEGEND, INVALID }

    /** the active code hashes from one successful fetch of the remote list */
    class ActiveCodes(val vipHashes: Set<String>, val legendHashes: Set<String>)

    private const val PREFS = "promo_codes"
    private const val KEY_REDEEMED = "redeemed"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    }

    /**
     * Fetches the live code list. Blocking, so call off the main thread.
     * Returns null when the list can't be reached (offline, missing, malformed)
     * — in which case no code may be redeemed.
     */
    fun fetchActiveCodes(): ActiveCodes? {
        var connection: HttpURLConnection? = null
        return try {
            connection = (URL(REMOTE_URL).openConnection() as HttpURLConnection).apply {
                connectTimeout = 5000
                readTimeout = 5000
                requestMethod = "GET"
            }
            val body = connection.inputStream.bufferedReader().use { it.readText() }
            val codes = JSONObject(body).getJSONArray("codes")
            val vip = mutableSetOf<String>()
            val legend = mutableSetOf<String>()
            for (i in 0 until codes.length()) {
                val entry = codes.getJSONObject(i)
                val hashHex = entry.getString("hash").trim().lowercase()
                if (hashHex.isEmpty()) continue
                when (entry.optString("type", "vip").lowercase()) {
                    "legend" -> legend.add(hashHex)
                    else -> vip.add(hashHex)
                }
            }
            ActiveCodes(vip, legend)
        } catch (e: Exception) {
            null
        } finally {
            connection?.disconnect()
        }
    }

    fun classify(code: String, active: ActiveCodes): CodeType = when (hash(code)) {
        in active.vipHashes -> CodeType.VIP
        in active.legendHashes -> CodeType.LEGEND
        else -> CodeType.INVALID
    }

    fun wasRedeemed(code: String): Boolean =
        hash(code) in (prefs.getStringSet(KEY_REDEEMED, emptySet()) ?: emptySet())

    fun markRedeemed(code: String) {
        val redeemed = prefs.getStringSet(KEY_REDEEMED, emptySet()) ?: emptySet()
        prefs.edit { putStringSet(KEY_REDEEMED, redeemed + hash(code)) }
    }

    private fun hash(code: String): String {
        val normalized = code.trim().uppercase()
        return MessageDigest.getInstance("SHA-256")
            .digest(normalized.toByteArray(Charsets.UTF_8))
            .joinToString("") { "%02x".format(it) }
    }
}
