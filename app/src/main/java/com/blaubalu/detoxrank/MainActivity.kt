/**
 * Detox Rank
 *
 * @author Adam Balušeskul
 */

package com.blaubalu.detoxrank

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.blaubalu.detoxrank.data.ads.AdsConsentManager
import com.blaubalu.detoxrank.data.ads.RewardedAdManager
import com.blaubalu.detoxrank.service.TimerService
import com.blaubalu.detoxrank.ui.DetoxRankAppContent
import com.blaubalu.detoxrank.ui.theme.DetoxRankTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var timerService by mutableStateOf<TimerService?>(null)

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as TimerService.StopwatchBinder
            timerService = binder.getService()
        }
        override fun onServiceDisconnected(arg0: ComponentName?) {
            // keep the last instance so the UI stays up; the binding auto-reconnects
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {}

    override fun onStart() {
        super.onStart()
        Intent(this, TimerService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    @ExperimentalMaterial3WindowSizeClassApi
    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DetoxRankTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val windowSize = calculateWindowSizeClass(activity = this)
                    timerService?.let { service ->
                        DetoxRankAppContent(
                            windowSize = windowSize.widthSizeClass,
                            timerService = service
                        )
                    }
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
        }

        // GDPR: silently refresh consent info; the form itself only appears
        // when the user first taps "watch ad". Ads start now if already
        // allowed. Posted after the first frame — UMP can answer from cache
        // synchronously, and the ad SDK must never initialize inside
        // onCreate before setContent (it corrupts the activity window).
        window.decorView.post {
            AdsConsentManager.refreshConsentInfo(this) {
                RewardedAdManager.startAds(this)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun DetoxRankPreview() {
    DetoxRankTheme {
        DetoxRankAppContent(
            windowSize = WindowWidthSizeClass.Compact,
            timerService = TimerService()
        )
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Preview(showBackground = true, widthDp = 700)
@Composable
fun DetoxRankMediumPreview() {
    DetoxRankTheme {
        DetoxRankAppContent(
            windowSize = WindowWidthSizeClass.Medium,
            timerService = TimerService()
        )
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Preview(showBackground = true, widthDp = 1000)
@Composable
fun DetoxRankExpandedPreview() {
    DetoxRankTheme {
        DetoxRankAppContent(
            windowSize = WindowWidthSizeClass.Expanded,
            timerService = TimerService()
        )
    }
}
