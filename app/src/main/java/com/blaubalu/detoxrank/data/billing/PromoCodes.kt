package com.blaubalu.detoxrank.data.billing

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import java.security.MessageDigest

/**
 * Offline promo codes that unlock every theme, validated against SHA-256
 * hashes so the plaintext codes never ship in the APK.
 *
 * To mint a new code, hash the UPPERCASE trimmed code and add it below —
 * PowerShell:
 *   $sha=[System.Security.Cryptography.SHA256]::Create()
 *   ($sha.ComputeHash([Text.Encoding]::UTF8.GetBytes('MY-NEW-CODE')) |
 *       % { $_.ToString('x2') }) -join ''
 *
 * Without a backend a code can't be tied to one account; hand each person
 * their own code and it's only redeemable once per device.
 */
object PromoCodes {

    /** codes for the dev and friends: unlock everything, no conditions */
    private val vipCodeHashes = setOf(
        "0a55eabd387cd18665f7490c263e159f15c49af7fd86c8e83b419f039a2ec020",
        "4e16fc169cfda6abc16ec990401e7132b0f8bca8c855abf65f6946ec40c4160c",
        "fc7b0f7e8aadf4bed5714f9c7fe2e238b56411bd5dc20a957f1eca630da149c1",
        "eee1decd271a1eada42e6f35ff14013ac1e3a81b3ccdb64a493d4a6f0d026b36",
        "4ec31086c81316f3e88b317697ce16288e5b69602e92be4e70367a98b42f1c94",
        "9c22ba4ed9dae9ae0c3330355fe0af90710709024ca69e8cea0da50cb9398864"
    )

    /** publicly shareable, but only redeems at the Legend rank + max level */
    private val legendCodeHashes = setOf(
        "97f0532f3ed228540f8b3fa2844ab49da562ebb97f0bab0724dbbb45f4ffb055"
    )

    enum class CodeType { VIP, LEGEND, INVALID }

    private const val PREFS = "promo_codes"
    private const val KEY_REDEEMED = "redeemed"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    }

    fun classify(code: String): CodeType = when (hash(code)) {
        in vipCodeHashes -> CodeType.VIP
        in legendCodeHashes -> CodeType.LEGEND
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
