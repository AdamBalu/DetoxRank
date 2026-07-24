package com.blaubalu.detoxrank.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.content.ContextCompat
import com.blaubalu.detoxrank.MainActivity
import com.blaubalu.detoxrank.ui.utils.Constants.ACTION_SERVICE_START
import com.blaubalu.detoxrank.ui.utils.Constants.CANCEL_REQUEST_CODE
import com.blaubalu.detoxrank.ui.utils.Constants.CLICK_REQUEST_CODE
import com.blaubalu.detoxrank.ui.utils.Constants.TIMER_STATE

/**
 * Helper object for timer service
 */
@ExperimentalAnimationApi
object ServiceHelper {

    private val flag =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            PendingIntent.FLAG_IMMUTABLE
        else
            0

    /**
     * Click pending intent for timer service
     */
    fun clickPendingIntent(context: Context): PendingIntent {
        val clickIntent = Intent(context, MainActivity::class.java).apply {
            putExtra(TIMER_STATE, TimerState.Started.name)
        }
        return PendingIntent.getActivity(
            context, CLICK_REQUEST_CODE, clickIntent, flag
        )
    }

    /**
     * Cancel pending intent for timer service
     */
    fun cancelPendingIntent(context: Context): PendingIntent {
        val cancelIntent = Intent(context, TimerService::class.java).apply {
            putExtra(TIMER_STATE, TimerState.Canceled.name)
        }
        return PendingIntent.getService(
            context, CANCEL_REQUEST_CODE, cancelIntent, flag
        )
    }

    fun triggerForegroundService(context: Context, action: String): Boolean {
        if (action == ACTION_SERVICE_START && !getNeededPermissions(context)) return false
        val intent = Intent(context, TimerService::class.java).apply {
            this.action = action
        }
        if (action == ACTION_SERVICE_START) {
            // required on API 26+ when the app may not be in the foreground
            ContextCompat.startForegroundService(context, intent)
        } else {
            context.startService(intent)
        }
        return true
    }

    /**
     * Handles needed user permissions
     *
     * The timer runs as a foreground service of type "systemExempted", which on
     * Android 14+ may only be started while the exact-alarm permission is granted —
     * without this gate the service start throws a SecurityException.
     * @return true if the permissions are set correctly in advance
     */
    private fun getNeededPermissions(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager =
                ContextCompat.getSystemService(context, AlarmManager::class.java)
            if (alarmManager?.canScheduleExactAlarms() == false) {
                Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).also { intent ->
                    intent.data = Uri.fromParts("package", context.packageName, null)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }
                return false
            }
        }
        return true
    }
}