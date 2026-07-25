package com.blaubalu.detoxrank.ui.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.blaubalu.detoxrank.ui.theme.rank_color

/**
 * Formatted RP gain. It has following format: + {value} RP {RP icon}
 */
@Composable
fun RankPointsGain(
    rankPointsGain: Int,
    plusIconSize: Dp,
    shieldIconSize: Dp,
    fontSize: TextUnit,
    horizontalArrangement: Arrangement.Horizontal
) {
  // rank points gain
  Row(
      horizontalArrangement = horizontalArrangement,
      verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
        imageVector = Icons.Filled.Add,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier
            .size(plusIconSize)
    )
    Text(
        text = "$rankPointsGain RP",
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
        fontSize = fontSize
    )
    Icon(
        imageVector = Icons.Filled.Shield,
        contentDescription = null,
        tint = rank_color,
        modifier = Modifier
            .padding(start = 3.dp)
            .size(shieldIconSize)
    )
  }
}

/**
 * Replays a subtle fade + slide whenever the wrapped content re-enters
 * composition, i.e. every time a lazy-list item scrolls back into view.
 * Draw-layer only (alpha + translation), so it never touches layout or the
 * list's scroll math — size-based enters like expandHorizontally caused the
 * jumpy, inconsistent list animation this replaces.
 */
@Composable
fun ScrollReEntry(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
  val progress = remember { Animatable(0f) }
  LaunchedEffect(Unit) {
    progress.animateTo(1f, tween(340, easing = FastOutSlowInEasing))
  }
  Box(
      modifier = modifier.graphicsLayer {
        alpha = progress.value
        translationX = -(1f - progress.value) * size.width * 0.10f
      }
  ) { content() }
}

/**
 * Animates enter and exit transitions of a Composable object
 * @param content Composable function to be animated
 */
@Composable
fun <T> T.AnimationBox(
    enter: EnterTransition = expandHorizontally() + fadeIn(),
    exit: ExitTransition = fadeOut() + slideOutHorizontally(),
    animateOnAppear: Boolean = true,
    content: @Composable T.() -> Unit
) {
  // With animateOnAppear the content starts hidden and animates in; without it,
  // the content must start fully visible (currentState == targetState == true),
  // otherwise AnimatedVisibility still plays the enter transition on first frame.
  val state = remember {
    MutableTransitionState(initialState = !animateOnAppear).apply {
      targetState = true
    }
  }

  AnimatedVisibility(
      visibleState = state,
      enter = enter,
      exit = exit
  ) { content() }
}
