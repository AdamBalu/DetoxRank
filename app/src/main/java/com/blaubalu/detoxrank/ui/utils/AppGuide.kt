package com.blaubalu.detoxrank.ui.utils

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.edit
import com.blaubalu.detoxrank.data.Section
import com.blaubalu.detoxrank.ui.theme.LocalThemeStyle

/**
 * State of the interactive first-run tour. The guide walks the user through
 * the live app: each step switches the section behind the overlay, so what is
 * being explained is what is on screen.
 */
object AppGuideState {
    private const val PREFS = "app_guide"
    private const val KEY_SHOWN = "shown"

    /** current guide step, -1 = hidden */
    val step = mutableStateOf(-1)

    fun start() {
        step.value = 0
    }

    fun wasShown(context: Context): Boolean =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getBoolean(KEY_SHOWN, false)

    fun markShown(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit { putBoolean(KEY_SHOWN, true) }
    }
}

/** one page of the tour; [section] is shown behind the overlay while it's up */
data class GuideStep(
    val section: Section?,
    val title: String,
    val description: String
)

val guideSteps = listOf(
    GuideStep(
        Section.Rank,
        "Welcome to DetoxRank!",
        "Detox from cheap dopamine and level up in real life. " +
                "Here's a quick tour — tap anywhere to continue."
    ),
    GuideStep(
        Section.Rank,
        "Rank",
        "Your progress hub. Earn Rank Points (RP) to climb from Bronze all " +
                "the way to Legend — tap the emblem to browse every rank, and " +
                "open ACHIEVEMENTS below."
    ),
    GuideStep(
        Section.Tasks,
        "Tasks",
        "Complete daily, weekly and monthly tasks for RP and XP. Swipe a " +
                "task sideways to refresh it into a new one — swiping a custom " +
                "task deletes it instead."
    ),
    GuideStep(
        Section.Timer,
        "Detox Timer",
        "Start a detox and keep it running — RP piles up the longer you " +
                "last. Tap the treasure chest to collect your loot!"
    ),
    GuideStep(
        Section.Theory,
        "Theory",
        "Bite-sized chapters on how dopamine works and how to beat the " +
                "scroll. Every finished chapter pays out RP."
    ),
    GuideStep(
        Section.Rank,
        "Make it yours",
        "Level up to unlock new themes, or grab premium ones in the Theme " +
                "Shop — the palette icon up top. Have fun!"
    )
)

/**
 * The tour overlay: a translucent scrim over the live screen with a themed
 * explainer card above the navigation bar. Tapping anywhere advances.
 */
@Composable
fun AppGuideOverlay(
    step: Int,
    onAdvance: () -> Unit,
    onSkip: () -> Unit
) {
    val data = guideSteps.getOrNull(step) ?: return
    Dialog(
        onDismissRequest = onSkip,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false
        )
    ) {
        val themeStyle = LocalThemeStyle.current
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.25f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onAdvance() }
        ) {
            Card(
                shape = themeStyle.cardShape ?: MaterialTheme.shapes.large,
                border = themeStyle.cardBorder,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 96.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = data.title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = data.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    ) {
                        guideSteps.indices.forEach { i ->
                            Box(
                                modifier = Modifier
                                    .padding(end = 5.dp)
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (i == step) {
                                            MaterialTheme.colorScheme.primary
                                        } else {
                                            MaterialTheme.colorScheme.surfaceVariant
                                        }
                                    )
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        if (step < guideSteps.lastIndex) {
                            TextButton(onClick = onSkip) {
                                Text("Skip")
                            }
                        }
                        Button(onClick = onAdvance) {
                            Text(
                                text = if (step >= guideSteps.lastIndex) "Let's go!" else "Next",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
