package com.blaubalu.detoxrank.ui.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.blaubalu.detoxrank.data.user.UiTheme
import com.blaubalu.detoxrank.ui.theme.DetoxRankTheme
import com.blaubalu.detoxrank.ui.theme.LocalThemeIsDark
import com.blaubalu.detoxrank.ui.theme.themeTexture
import kotlinx.coroutines.delay

/**
 * Displays the current popup from the queue, if any
 */
@Composable
fun PopupQueueDisplay(theme: UiTheme = UiTheme.Default) {
    // captured in the activity's window: dialog windows report zero insets
    // to their own content, so the overlay can't measure these itself
    val systemBarPadding = WindowInsets.systemBars.asPaddingValues()
    PopupManager.currentPopup?.let { popup ->
        CelebrationOverlay(
            popup = popup,
            theme = theme,
            systemBarPadding = systemBarPadding,
            onDismiss = { PopupManager.dismiss() }
        )
    }
}

/**
 * Full-screen celebration: the badge is the hero, blooming into view with a
 * bouncy overshoot; a tap anywhere dismisses it. No buttons, no card.
 * [systemBarPadding] must come from the activity's composition scope.
 */
@Composable
fun CelebrationOverlay(
    popup: PopupData,
    theme: UiTheme,
    systemBarPadding: PaddingValues,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        DetoxRankTheme(theme = theme) {
            val accent = if (popup.type == PopupType.RANK_UP) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.secondary
            }

            // keyed on the popup so every queued celebration replays the entrance
            val appear = remember(popup) {
                MutableTransitionState(false).apply { targetState = true }
            }
            AnimatedVisibility(
                visibleState = appear,
                enter = scaleIn(
                    initialScale = 0.7f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                ) + fadeIn(animationSpec = tween(durationMillis = 250))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        // the celebration owns the screen: solid themed backdrop + texture
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.96f))
                        .themeTexture(theme)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onDismiss() }
                        .padding(systemBarPadding)
                        .padding(horizontal = 32.dp, vertical = 16.dp)
                ) {
                    // slightly above center so the bottom hint keeps breathing room
                    Spacer(modifier = Modifier.weight(0.75f))

                    // the badge is the star of the show
                    val badgeScale = remember(popup) { Animatable(0f) }
                    LaunchedEffect(popup) {
                        delay(150)
                        badgeScale.animateTo(
                            1f,
                            spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                    }
                    // sized to the screen so the hero never crowds small displays
                    val configuration = LocalConfiguration.current
                    val badgeSize = minOf(
                        configuration.screenWidthDp * 0.78f,
                        configuration.screenHeightDp * 0.38f
                    ).dp.coerceAtMost(320.dp)
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(badgeSize)
                            .scale(badgeScale.value)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        accent.copy(alpha = 0.35f),
                                        accent.copy(alpha = 0.12f),
                                        Color.Transparent
                                    )
                                ),
                                shape = CircleShape
                            )
                    ) {
                        when {
                            popup.iconRes != null -> Image(
                                painter = painterResource(id = popup.iconRes),
                                contentDescription = null,
                                modifier = Modifier.size(badgeSize * 0.75f)
                            )

                            popup.achievementId != null -> Image(
                                painter = painterResource(
                                    id = getAchievementDrawableFromId(
                                        popup.achievementId,
                                        LocalThemeIsDark.current
                                    )
                                ),
                                contentDescription = null,
                                modifier = Modifier.size(badgeSize * 0.69f)
                            )

                            else -> Icon(
                                imageVector = Icons.Filled.EmojiEvents,
                                contentDescription = null,
                                tint = accent,
                                modifier = Modifier.size(badgeSize * 0.56f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                    Text(
                        text = popup.title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = accent,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = popup.description,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    if (popup.secondaryText.isNotBlank()) {
                        Text(
                            text = popup.secondaryText,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "tap anywhere to continue",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp)
                    )
                }
            }
        }
    }
}
