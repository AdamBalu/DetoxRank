package com.blaubalu.detoxrank.ui.timer

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blaubalu.detoxrank.R
import com.blaubalu.detoxrank.data.TimerDifficulty
import com.blaubalu.detoxrank.data.TimerDifficultyCard
import com.blaubalu.detoxrank.service.TimerService
import com.blaubalu.detoxrank.service.TimerState
import com.blaubalu.detoxrank.ui.DetoxRankViewModel
import com.blaubalu.detoxrank.ui.theme.common_green
import com.blaubalu.detoxrank.ui.theme.md_theme_light_error
import com.blaubalu.detoxrank.ui.theme.rare_blue
import com.blaubalu.detoxrank.ui.utils.Constants.RP_PERCENTAGE_GAIN_TIMER_EASY_DIFFICULTY
import com.blaubalu.detoxrank.ui.utils.Constants.RP_PERCENTAGE_GAIN_TIMER_HARD_DIFFICULTY
import com.blaubalu.detoxrank.ui.utils.Constants.RP_PERCENTAGE_GAIN_TIMER_MEDIUM_DIFFICULTY
import com.blaubalu.detoxrank.ui.utils.toastShort

@ExperimentalAnimationApi
@Composable
fun TimerDifficultySelectScreen(
    timerViewModel: TimerViewModel,
    timerService: TimerService,
    detoxRankViewModel: DetoxRankViewModel,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        timerViewModel.difficultySelectShown,
        enter = slideInVertically(animationSpec = tween(durationMillis = 500)) { height -> height } + fadeIn(
            animationSpec = tween(durationMillis = 500)
        ),
        exit = slideOutVertically(animationSpec = tween(durationMillis = 500)) { height -> height }
    ) {
        BackHandler {
            timerViewModel.setDifficultySelectShown(false)
        }
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            item {
                DifficultySelectHeader(
                    onClose = { timerViewModel.setDifficultySelectShown(false) }
                )
            }
            items(timerViewModel.difficultyList) { card ->
                DifficultyCard(
                    card = card,
                    timerViewModel = timerViewModel,
                    timerService = timerService,
                    detoxRankViewModel = detoxRankViewModel,
                    modifier = Modifier.padding(start = 20.dp, top = 6.dp, bottom = 6.dp, end = 20.dp)
                )
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun DifficultySelectHeader(
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 16.dp, end = 8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text(
                stringResource(R.string.difficulty_select_heading),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                stringResource(R.string.difficulty_select_subheading),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 6.dp)
            )
        }
        IconButton(
            onClick = onClose,
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = stringResource(R.string.close),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@ExperimentalAnimationApi
@Composable
fun DifficultyCard(
    card: TimerDifficultyCard,
    timerViewModel: TimerViewModel,
    timerService: TimerService,
    detoxRankViewModel: DetoxRankViewModel,
    modifier: Modifier = Modifier
) {
    val currentState by timerService.currentState
    val context = LocalContext.current

    val difficultyColor = when (card.difficulty) {
        TimerDifficulty.Easy -> common_green
        TimerDifficulty.Medium -> rare_blue
        TimerDifficulty.Hard -> md_theme_light_error
    }

    val difficultyTitle = when (card.difficulty) {
        TimerDifficulty.Easy -> stringResource(R.string.difficulty_easy_title)
        TimerDifficulty.Medium -> stringResource(R.string.difficulty_medium_title)
        TimerDifficulty.Hard -> stringResource(R.string.difficulty_hard_title)
    }

    val rpBonus = when (card.difficulty) {
        TimerDifficulty.Easy -> RP_PERCENTAGE_GAIN_TIMER_EASY_DIFFICULTY
        TimerDifficulty.Medium -> RP_PERCENTAGE_GAIN_TIMER_MEDIUM_DIFFICULTY
        TimerDifficulty.Hard -> RP_PERCENTAGE_GAIN_TIMER_HARD_DIFFICULTY
    }

    val currentDifficulty = detoxRankViewModel.uiState.collectAsState().value.currentTimerDifficulty
    val isSelected = currentDifficulty == card.difficulty
    val cardEnabled = currentState != TimerState.Started

    Card(
        border = BorderStroke(
            width = if (isSelected) 4.dp else 2.dp,
            color = if (isSelected) difficultyColor else MaterialTheme.colorScheme.outline
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .fillMaxWidth()
            .alpha(if (cardEnabled || isSelected) 1f else 0.55f)
            .clickable {
                if (cardEnabled) {
                    timerViewModel.setDifficultySelectShown(false)
                    detoxRankViewModel.setTimerDifficultyUiState(card.difficulty)
                    detoxRankViewModel.setTimerDifficultyDatabase(card.difficulty)
                } else {
                    toastShort(context.getString(R.string.difficulty_locked_while_running), context)
                }
            }
    ) {
        Box {
            Image(
                painter = painterResource(id = card.backgroundImageRes),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
            // scrim for text readability over the background art
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.30f),
                                Color.Black.copy(alpha = 0.65f)
                            )
                        )
                    )
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = difficultyTitle,
                            style = MaterialTheme.typography.headlineMedium,
                            color = difficultyColor
                        )
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = stringResource(R.string.difficulty_selected),
                                tint = difficultyColor,
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .size(22.dp)
                            )
                        }
                    }
                    RpBonusBadge(rpBonus = rpBonus, color = difficultyColor)
                }

                Text(
                    text = stringResource(R.string.difficulty_avoid_label),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.padding(top = 12.dp, bottom = 6.dp)
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    card.avoidList.forEach { avoidItem ->
                        AvoidChip(text = stringResource(avoidItem))
                    }
                }
            }
        }
    }
}

/**
 * Small pill showing the RP percentage bonus of a difficulty
 */
@Composable
private fun RpBonusBadge(
    rpBonus: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(50),
        color = Color.Black.copy(alpha = 0.55f),
        border = BorderStroke(2.dp, color),
        modifier = modifier
    ) {
        Text(
            text = "+$rpBonus% RP",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = color,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}

/**
 * A single item of the avoid list, rendered as a chip
 */
@Composable
private fun AvoidChip(
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(50),
        color = Color.Black.copy(alpha = 0.45f),
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 13.sp,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}
