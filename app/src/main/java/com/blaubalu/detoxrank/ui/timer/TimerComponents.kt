package com.blaubalu.detoxrank.ui.timer

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.blaubalu.detoxrank.R
import com.blaubalu.detoxrank.data.TimerDifficulty
import com.blaubalu.detoxrank.service.ServiceHelper
import com.blaubalu.detoxrank.service.TimerService
import com.blaubalu.detoxrank.service.TimerState
import com.blaubalu.detoxrank.ui.DetoxRankUiState
import com.blaubalu.detoxrank.ui.DetoxRankViewModel
import com.blaubalu.detoxrank.ui.rank.AchievementViewModel
import com.blaubalu.detoxrank.ui.theme.LocalThemeStyle
import com.blaubalu.detoxrank.ui.theme.rank_color
import com.blaubalu.detoxrank.ui.theme.LocalThemeStyle
import com.blaubalu.detoxrank.ui.theme.rank_color_ultra_dark
import com.blaubalu.detoxrank.ui.utils.Constants
import com.blaubalu.detoxrank.ui.utils.Constants.ALL_THEMES_UNLOCKED_FOR_TESTING
import com.blaubalu.detoxrank.ui.utils.Constants.ID_START_TIMER
import com.blaubalu.detoxrank.ui.utils.calculateTimerFloatAddition
import com.blaubalu.detoxrank.ui.utils.calculateTimerRPGain
import com.blaubalu.detoxrank.ui.utils.getParamDependingOnScreenSizeDp
import com.blaubalu.detoxrank.ui.utils.getParamDependingOnScreenSizeSp
import com.blaubalu.detoxrank.ui.utils.toastLong
import com.blaubalu.detoxrank.ui.utils.toastShort
import com.hitanshudhawan.circularprogressbar.CircularProgressBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@ExperimentalAnimationApi
@Composable
fun TimerClock(
  timerService: TimerService,
  modifier: Modifier = Modifier
) {
  val progressSeconds by animateFloatAsState(
    targetValue = timerService.seconds.value.toFloat() * calculateTimerFloatAddition(50f, 60),
    label = ""
  )
  val progressMinutes by animateFloatAsState(
    targetValue = timerService.minutes.value.toFloat() * calculateTimerFloatAddition(39f, 60),
    label = ""
  )
  val progressHours by animateFloatAsState(
    targetValue = timerService.hours.value.toFloat() * calculateTimerFloatAddition(19.44f, 24),
    label = ""
  )

  val timerWidthDecrement = getParamDependingOnScreenSizeDp(50.dp, 40.dp, 30.dp, 20.dp, 0.dp)
  Box(contentAlignment = Alignment.Center) {
    Box(
      modifier = modifier
        .fillMaxWidth()
    ) {
      CircularProgressBar(
        modifier = Modifier
          .width(328.dp - timerWidthDecrement)
          .align(Alignment.Center),
        progress = progressSeconds,
        progressMax = 100f,
        progressBarColor =
        MaterialTheme.colorScheme.primary,
        progressBarWidth = 18.dp,
        backgroundProgressBarColor = Color.Transparent,
        backgroundProgressBarWidth = 1.dp,
        roundBorder = true,
        startAngle = 270f
      )
      CircularProgressBar(
        modifier = Modifier
          .width(314.dp - timerWidthDecrement)
          .align(Alignment.Center),
        progress = 50f,
        progressMax = 100f,
        progressBarColor =
        MaterialTheme.colorScheme.primary,
        progressBarWidth = 4.dp,
        backgroundProgressBarColor = Color.Transparent,
        backgroundProgressBarWidth = 1.dp,
        roundBorder = true,
        startAngle = 270f
      )
      CircularProgressBar(
        modifier = Modifier
          .width(285.dp - timerWidthDecrement)
          .align(Alignment.Center),
        progress = progressMinutes,
        progressMax = 100f,
        progressBarColor =
        MaterialTheme.colorScheme.secondary,
        progressBarWidth = 20.dp,
        backgroundProgressBarColor = Color.Transparent,
        backgroundProgressBarWidth = 1.dp,
        roundBorder = true,
        startAngle = 290f
      )
      CircularProgressBar(
        modifier = Modifier
          .width(269.dp - timerWidthDecrement)
          .align(Alignment.Center),
        progress = 39f,
        progressMax = 100f,
        progressBarColor =
        MaterialTheme.colorScheme.secondary,
        progressBarWidth = 4.dp,
        backgroundProgressBarColor = Color.Transparent,
        backgroundProgressBarWidth = 1.dp,
        roundBorder = true,
        startAngle = 290f
      )

      CircularProgressBar(
        modifier = Modifier
          .width(240.dp - timerWidthDecrement)
          .align(Alignment.Center),
        progress = progressHours,
        progressMax = 100f,
        progressBarColor =
        MaterialTheme.colorScheme.tertiary,
        progressBarWidth = 25.dp,
        backgroundProgressBarColor = Color.Transparent,
        backgroundProgressBarWidth = 1.dp,
        roundBorder = true,
        startAngle = 325f
      )

      CircularProgressBar(
        modifier = Modifier
          .width(220.dp - timerWidthDecrement)
          .align(Alignment.Center),
        progress = 19.44f,
        progressMax = 100f,
        progressBarColor =
        MaterialTheme.colorScheme.tertiary,
        progressBarWidth = 4.dp,
        backgroundProgressBarColor = Color.Transparent,
        backgroundProgressBarWidth = 1.dp,
        roundBorder = true,
        startAngle = 325f
      )
    }
    TimerTimeInNumbers(
      timerService = timerService
    )
  }
}

@ExperimentalAnimationApi
@Composable
fun TimerTimeUnitDigitAnimatedPair(timeUnit: String, color: Color, label: String = "") {
  AnimatedContent(
    targetState = timeUnit,
    transitionSpec = {
      addAnimation().using(SizeTransform(clip = false))
    }, label = label
  ) {
    // digits follow the theme's display typeface; scaled per theme so wide
    // fonts (e.g. the pixel or serif ones) still fit three digit groups
    Text(
      text = it,
      style = MaterialTheme.typography.headlineLarge.copy(
        fontSize = MaterialTheme.typography.headlineLarge.fontSize *
                (55f / 40f) * LocalThemeStyle.current.timerDigitScale,
        color = color,
        // tabular figures: every digit gets the same advance width, so the
        // group doesn't slide sideways as the digits tick over
        fontFeatureSettings = "tnum"
      ),
      maxLines = 1,
      modifier = Modifier.padding(end = 15.dp)
    )
  }
}

@ExperimentalAnimationApi
@Composable
fun TimerTimeInNumbers(
  timerService: TimerService
) {
  val hours by timerService.hours
  val minutes by timerService.minutes
  val seconds by timerService.seconds

  val context = LocalContext.current
  LaunchedEffect(Unit) {
    timerService.updateTimerTimeLaunchedEffect(context)
  }
  Row {
    TimerTimeUnitDigitAnimatedPair(hours, MaterialTheme.colorScheme.tertiary)
    TimerTimeUnitDigitAnimatedPair(minutes, MaterialTheme.colorScheme.secondary)
    TimerTimeUnitDigitAnimatedPair(seconds, MaterialTheme.colorScheme.primary)
  }
}

@ExperimentalAnimationApi
@Composable
fun TimerStartStopButton(
  timerService: TimerService,
  detoxRankViewModel: DetoxRankViewModel,
  achievementViewModel: AchievementViewModel,
  modifier: Modifier = Modifier
) {
  val timerRpGain = calculateTimerRPGain(detoxRankViewModel, timerService)
  val currentState by timerService.currentState
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()
  var wasButtonClicked by remember { mutableStateOf(false) }

  fun stopTimerService() {
    if (!ServiceHelper.triggerForegroundService(
        context = context,
        action = Constants.ACTION_SERVICE_CANCEL
      )
    ) {
      toastLong("You need to allow the permission to start the timer", context)
    } else {
      coroutineScope.launch {
        achievementViewModel.achieveTimerAchievements(timerService.days.value.toInt())
        detoxRankViewModel.updateTimerStarted(false)
        detoxRankViewModel.updateLastRpGatherTime()
        detoxRankViewModel.updateUserRankPoints(timerRpGain.toInt())
      }
      wasButtonClicked = false
    }
  }

  fun handleTimerStopButtonPress() {
    if (!wasButtonClicked) {
      toastShort("Double tap to end the timer", context)
      wasButtonClicked = true
      coroutineScope.launch {
        delay(2000)
        wasButtonClicked = false
      }
    } else {
      stopTimerService()
    }
  }

  Box(modifier = modifier.fillMaxWidth()) {
    if (currentState == TimerState.Started) {
      TimerStopButton { handleTimerStopButtonPress() }
    } else {
      TimerStartButton(
        context,
        coroutineScope,
        detoxRankViewModel,
        achievementViewModel,
      )
    }
  }
}

@ExperimentalAnimationApi
@Composable
fun CollectAccumulatedRpButton(
  detoxRankViewModel: DetoxRankViewModel,
  timerService: TimerService,
  modifier: Modifier,
  onCollected: () -> Unit = {}
) {
  val timerRpGain = calculateTimerRPGain(detoxRankViewModel, timerService)
  val coroutineScope = rememberCoroutineScope()
  val scale = remember {
    Animatable(1f)
  }
  // 0 = lid closed, 1 = lid fully open
  val lidOpen = remember { Animatable(0f) }
  val chestEnabled = ALL_THEMES_UNLOCKED_FOR_TESTING || timerRpGain.toInt() > 0
  // breathing glow behind the chest: the "this is tappable loot" signal
  val glowTransition = rememberInfiniteTransition(label = "")
  val glowPulse by glowTransition.animateFloat(
    initialValue = 0.8f,
    targetValue = 1.15f,
    animationSpec = infiniteRepeatable(
      animation = tween(900, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = ""
  )
  val chestColor = if (chestEnabled) {
    MaterialTheme.colorScheme.onPrimaryContainer
  } else {
    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
  }
  val platformColor = if (chestEnabled) {
    MaterialTheme.colorScheme.primaryContainer
  } else {
    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
  }
  val platformStroke = if (chestEnabled) {
    MaterialTheme.colorScheme.primary.copy(alpha = 0.55f)
  } else {
    Color.Transparent
  }
  val glowColor = MaterialTheme.colorScheme.primary
  Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier
      .padding(top = 8.dp)
      .scale(scale.value)
      .size(64.dp)
  ) {
    // circular ripple pad UNDER the drawing, so the tap feedback stays round
    // while the chest itself is never clipped (the open lid swings past it)
    Box(
      modifier = Modifier
        .size(64.dp)
        .clip(CircleShape)
        .clickable(enabled = chestEnabled) {
          onCollected()
          coroutineScope.launch {
            scale.animateTo(
              0.85f,
              animationSpec = tween(200),
            )
            scale.animateTo(
              1f,
              animationSpec = tween(200),
            )

            detoxRankViewModel.updateLastRpGatherTime()
            detoxRankViewModel.updateUserRankPoints(timerRpGain.toInt())
          }
          coroutineScope.launch {
            // lid swings open, waits for the shields to fly in, then snaps shut
            lidOpen.animateTo(1f, tween(220, easing = FastOutSlowInEasing))
            delay(800)
            lidOpen.animateTo(
              0f,
              spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
              )
            )
          }
        }
    )
    Canvas(modifier = Modifier.size(52.dp)) {
      val w = size.width
      val h = size.height
      // pulsing halo while there is something to collect
      if (chestEnabled) {
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(glowColor.copy(alpha = 0.38f * glowPulse), Color.Transparent),
            center = Offset(w * 0.5f, h * 0.45f),
            radius = w * 0.62f * glowPulse
          ),
          radius = w * 0.62f * glowPulse,
          center = Offset(w * 0.5f, h * 0.45f)
        )
      }
      // the dais the chest sits on
      drawOval(
        color = platformColor,
        topLeft = Offset(0f, h * 0.80f),
        size = Size(w, h * 0.18f)
      )
      drawOval(
        color = platformStroke,
        topLeft = Offset(0f, h * 0.80f),
        size = Size(w, h * 0.18f),
        style = Stroke(width = 1.5.dp.toPx())
      )
      // the chest itself, resting slightly into the dais
      inset(left = w * 0.14f, top = h * 0.10f, right = w * 0.14f, bottom = h * 0.14f) {
        drawChest(open = lidOpen.value, chestColor = chestColor)
      }
    }
  }
}

/** Draws the treasure chest; [open] 0 = lid closed, 1 = lid fully open */
private fun DrawScope.drawChest(open: Float, chestColor: Color) {
  val w = size.width
  val h = size.height

  // dark opening revealed while the lid is up
  if (open > 0.05f) {
    drawRoundRect(
      color = Color.Black.copy(alpha = 0.45f * open),
      topLeft = Offset(w * 0.10f, h * 0.40f),
      size = Size(w * 0.80f, h * 0.18f),
      cornerRadius = CornerRadius(w * 0.08f, w * 0.08f)
    )
  }
  // chest base
  drawPath(
    Path().apply {
      addRoundRect(
        RoundRect(
          rect = Rect(w * 0.04f, h * 0.54f, w * 0.96f, h),
          bottomLeft = CornerRadius(w * 0.12f, w * 0.12f),
          bottomRight = CornerRadius(w * 0.12f, w * 0.12f)
        )
      )
    },
    chestColor
  )
  // lid + clasp swing together on a hinge at the lid's bottom-left
  withTransform({
    rotate(degrees = -80f * open, pivot = Offset(w * 0.02f, h * 0.46f))
  }) {
    // domed lid
    drawPath(
      Path().apply {
        addRoundRect(
          RoundRect(
            rect = Rect(0f, 0f, w, h * 0.46f),
            topLeft = CornerRadius(w * 0.30f, h * 0.42f),
            topRight = CornerRadius(w * 0.30f, h * 0.42f)
          )
        )
      },
      chestColor
    )
    // clasp bridging the lid gap, with a dark keyhole
    drawRoundRect(
      color = chestColor,
      topLeft = Offset(w * 0.38f, h * 0.34f),
      size = Size(w * 0.24f, h * 0.38f),
      cornerRadius = CornerRadius(w * 0.07f, w * 0.07f)
    )
    drawCircle(
      color = Color.Black.copy(alpha = 0.5f),
      radius = w * 0.055f,
      center = Offset(w * 0.50f, h * 0.52f)
    )
  }
}

@ExperimentalAnimationApi
@Composable
fun TimerStartButton(
  context: Context,
  coroutineScope: CoroutineScope,
  detoxRankViewModel: DetoxRankViewModel,
  achievementViewModel: AchievementViewModel,
) {
  fun startTimerService() {
    if (!ServiceHelper.triggerForegroundService(
        context = context,
        action = Constants.ACTION_SERVICE_START
      )
    ) {
      toastShort("You need to allow the permission to start the timer", context)
    } else {
      coroutineScope.launch {
        achievementViewModel.achieveAchievement(ID_START_TIMER)
        detoxRankViewModel.updateTimerStartedTimeMillis()
        detoxRankViewModel.updateLastRpGatherTime()
        detoxRankViewModel.updateTimerStarted(true)
      }
    }
  }
  val themeStyle = LocalThemeStyle.current
  Button(
    onClick = { startTimerService() },
    shape = MaterialTheme.shapes.large,
    border = themeStyle.cardBorder,
    modifier = Modifier
      .fillMaxWidth()
      .height(52.dp)
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        imageVector = Icons.Outlined.PlayArrow,
        contentDescription = null,
        modifier = Modifier.padding(end = 5.dp)
      )
      Text(
        text = "Start Detox",
        style = MaterialTheme.typography.bodySmall,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        fontSize = 14.sp
      )
    }
  }
}

@Composable
fun TimerStopButton(handleTimerStopButtonPress: () -> Unit) {
  val themeStyle = LocalThemeStyle.current
  OutlinedIconButton(
    onClick = { handleTimerStopButtonPress() },
    shape = MaterialTheme.shapes.large,
    border = themeStyle.cardBorder ?: IconButtonDefaults.outlinedIconButtonBorder(true),
    modifier = Modifier
      .fillMaxWidth()
      .height(52.dp)
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        imageVector = Icons.Outlined.Stop,
        contentDescription = null,
        modifier = Modifier.padding(end = 5.dp)
      )
      Text(
        text = "Finish",
        style = MaterialTheme.typography.bodySmall,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        fontSize = 14.sp
      )
    }
  }
}


/**
 * Consists of a timer difficulty select button, timer RP gain and day streak UIs (for small screens)
 */
@ExperimentalAnimationApi
@Composable
fun TimerFooter(
  timerService: TimerService,
  detoxRankUiState: DetoxRankUiState,
  detoxRankViewModel: DetoxRankViewModel,
  timerViewModel: TimerViewModel,
  modifier: Modifier = Modifier
) {
  val days by timerService.days
  val currentTimerState by timerService.currentState
  val currentScreenHeight = LocalConfiguration.current.screenHeightDp
  val currentScreenWidth = LocalConfiguration.current.screenWidthDp

  val timerTranslationY =
    if (currentScreenHeight < 600 && currentScreenWidth < 340) -100f
    else if (currentScreenHeight < 700 && currentScreenWidth < 370) -90f
    else if (currentScreenHeight < 800 && currentScreenWidth < 400) -70f
    else if (currentScreenHeight < 900 && currentScreenWidth < 500) -60f
    else if (currentScreenHeight < 1100 && currentScreenWidth < 600) -50f
    else {
      0f
    }

  var collectPulse by remember { mutableStateOf(0) }
  val shieldAnims = remember { List(4) { Animatable(0f) } }
  LaunchedEffect(collectPulse) {
    if (collectPulse > 0) {
      shieldAnims.forEachIndexed { i, anim ->
        launch {
          anim.snapTo(0f)
          delay(i * 70L)
          anim.animateTo(1f, tween(durationMillis = 600, easing = FastOutSlowInEasing))
          anim.snapTo(0f)
        }
      }
    }
  }

  Box(
    modifier = modifier
      .fillMaxWidth()
      .graphicsLayer { translationY = timerTranslationY }
  ) {
  Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    AccumulatedRp(
      detoxRankViewModel = detoxRankViewModel,
      currentScreenHeight = currentScreenHeight,
      timerService = timerService,
      modifier = modifier
    )
    Row(
      horizontalArrangement = Arrangement.SpaceBetween,
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 35.dp, end = 35.dp, bottom = 0.dp)
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
      ) {
        Text(
          text = "DAY STREAK",
          style = MaterialTheme.typography.bodySmall,
          fontSize = getParamDependingOnScreenSizeSp(
            p1 = 10.sp,
            p2 = 12.sp,
            p3 = 14.sp,
            p4 = MaterialTheme.typography.bodySmall.fontSize,
            otherwise = MaterialTheme.typography.bodySmall.fontSize
          )
        )
        Text(
          days,
          style = MaterialTheme.typography.headlineLarge,
          textAlign = TextAlign.Center,
          fontSize = getParamDependingOnScreenSizeSp(
            p1 = 23.sp,
            p2 = 32.sp,
            p3 = 40.sp,
            p4 = 45.sp,
            45.sp
          ),
          modifier = Modifier.padding(top = 15.dp)
        )
      }
      if (currentTimerState == TimerState.Started)
        CollectAccumulatedRpButton(
          detoxRankViewModel, timerService, modifier,
          onCollected = { collectPulse++ }
        )
      Column(
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = stringResource(R.string.difficulty),
          style = MaterialTheme.typography.bodySmall,
          fontSize = getParamDependingOnScreenSizeSp(
            p1 = 10.sp,
            p2 = 12.sp,
            p3 = 14.sp,
            p4 = MaterialTheme.typography.bodySmall.fontSize,
            otherwise = MaterialTheme.typography.bodySmall.fontSize
          )
        )
        DifficultySelect(
          onClick = { timerViewModel.setDifficultySelectShown(true) },
          timerService = timerService,
          detoxRankUiState = detoxRankUiState,
          detoxRankViewModel = detoxRankViewModel
        )
      }
    }
  }

  // shields flying from the accumulated RP down into the collect button
  shieldAnims.forEachIndexed { i, anim ->
    val p = anim.value
    if (p > 0f && p < 1f) {
      Image(
        painter = painterResource(id = R.drawable.rank_points_icon),
        contentDescription = null,
        modifier = Modifier
          .align(Alignment.TopCenter)
          .offset(x = ((i - 1.5f) * 26 * (1f - p)).dp, y = (30 + 105 * p).dp)
          .size(18.dp)
          .alpha(1f - p * p)
      )
    }
  }
  }
}

//// could be added in the future, so that user can decide to delete timer progress
//@Composable
//fun SaveTimerProgressDialog(
//    onConfirm: () -> Unit,
//    onDismiss: () -> Unit
//) {
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        title = { Text(stringResource(R.string.save_timer_progress_dialog_title)) },
//        text = { Text(stringResource(R.string.save_timer_progress_dialog_text)) },
//        confirmButton = {
//            Button(
//                onClick = onConfirm
//            ) {
//                Text(stringResource(R.string.save_timer_progress_dialog_confirm))
//            }
//        },
//        dismissButton = {
//            Button(
//                onClick = onDismiss
//            ) {
//                Text(stringResource(R.string.save_timer_progress_dialog_dismiss))
//            }
//        }
//    )
//}

@ExperimentalAnimationApi
@Composable
fun AccumulatedRp(
  detoxRankViewModel: DetoxRankViewModel,
  currentScreenHeight: Int,
  timerService: TimerService,
  modifier: Modifier
) {
  val points = String.format(
    Locale.US,
    "%.2f",
    maxOf(calculateTimerRPGain(detoxRankViewModel, timerService), 0.0)
  )

  val (integers, decimals) = points.split('.').let { parts ->
    val integerPart = parts.getOrElse(0) { "0" }
    val decimalPart = parts.getOrElse(1) { "00" }
    integerPart to decimalPart
  }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(bottom = if (currentScreenHeight < 800) 0.dp else 50.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      stringResource(R.string.timer_accumulated_points_heading),
      style = MaterialTheme.typography.bodySmall,
      fontSize = getParamDependingOnScreenSizeSp(
        10.sp, 12.sp, 14.sp, MaterialTheme.typography.bodySmall.fontSize,
        otherwise = MaterialTheme.typography.bodySmall.fontSize
      )
    )
    Row(verticalAlignment = Alignment.CenterVertically) {
      AnimatedContent(
        targetState = integers,
        transitionSpec = { expandVertically() + fadeIn() togetherWith fadeOut() },
        label = ""
      ) {
        Text(
          it,
          modifier = Modifier.padding(top = 5.dp, end = 3.dp),
          style = MaterialTheme.typography.headlineLarge,
          letterSpacing = 1.sp,
          fontSize = getParamDependingOnScreenSizeSp(21.sp, 25.sp, 40.sp, 45.sp, 45.sp)
        )
      }
      AnimatedContent(
        targetState = decimals,
        transitionSpec = { expandVertically() + fadeIn() togetherWith fadeOut() },
        label = ""
      ) {
        Text(
          ".$it",
          style = MaterialTheme.typography.headlineSmall,
          fontSize = 12.sp,
          modifier = Modifier.padding(end = 4.dp)
        )
      }

      Image(
        painterResource(id = R.drawable.rank_points_icon),
        contentDescription = null,
        modifier = Modifier
          .size(25.dp)
          .padding(top = 5.dp)
      )

    }
  }
}

@ExperimentalAnimationApi
@Composable
fun DifficultySelect(
  onClick: () -> Unit,
  detoxRankUiState: DetoxRankUiState,
  detoxRankViewModel: DetoxRankViewModel,
  timerService: TimerService,
  modifier: Modifier = Modifier
) {
  LaunchedEffect(Unit) {
    val timerDifficulty = detoxRankViewModel.getUserTimerDifficulty()
    detoxRankViewModel.setCurrentTimerDifficulty(timerDifficulty)
  }
  val currentState by timerService.currentState
  val iconToDisplay = when (detoxRankUiState.currentTimerDifficulty) {
    TimerDifficulty.Easy -> R.drawable.timer_easy_difficulty_icon
    TimerDifficulty.Medium -> R.drawable.timer_medium_difficulty_icon
    TimerDifficulty.Hard -> R.drawable.timer_hard_difficulty_icon
  }

  val difficultyPaddingShrinker = getParamDependingOnScreenSizeDp(10.dp, 8.dp, 4.dp, 0.dp, 0.dp)

  OutlinedIconButton(
    onClick = onClick,
    shape = RoundedCornerShape(14.dp),
    border = if (currentState != TimerState.Started) {
      BorderStroke(
        3.dp, Brush.sweepGradient(
          listOf(
            rank_color,
            rank_color_ultra_dark,
            rank_color,
            rank_color_ultra_dark,
            rank_color
          )
        )
      )
    } else {
      BorderStroke(2.dp, MaterialTheme.colorScheme.surfaceVariant)
    },
    modifier = modifier
      .width(80.dp - difficultyPaddingShrinker)
      .height(60.dp - difficultyPaddingShrinker)
      .padding(top = 10.dp)
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Image(
        painterResource(id = iconToDisplay),
        contentDescription = null,
        modifier = Modifier
          .width(80.dp - difficultyPaddingShrinker)
          .padding(10.dp)
      )
    }
  }
}
