package com.blaubalu.detoxrank.ui.tasks.task

import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.blaubalu.detoxrank.ui.theme.LocalThemeStyle
import com.blaubalu.detoxrank.ui.theme.epic_purple
import com.blaubalu.detoxrank.ui.theme.epic_purple_toned_down
import com.blaubalu.detoxrank.ui.theme.rank_color_ultra_dark
import com.blaubalu.detoxrank.ui.theme.rank_color_ultra_light

class TaskColors {
  @Composable
  fun completedTaskColors(): CardColors {
    return CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.tertiary
    )
  }

  @Composable
  fun defaultTaskColors(): CardColors {
    return CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)
  }

  @Composable
  fun dailyTaskColors(darkTheme: Boolean): CardColors {
    return if (darkTheme) {
      CardDefaults.cardColors(MaterialTheme.colorScheme.onSecondary)
    } else {
      CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer)
    }
  }

  @Composable
  fun weeklyTaskColors(darkTheme: Boolean): CardColors {
    return if (darkTheme) {
      CardDefaults.cardColors(MaterialTheme.colorScheme.onTertiary)
    } else {
      CardDefaults.cardColors(MaterialTheme.colorScheme.tertiaryContainer)
    }
  }

  @Composable
  fun monthlyTaskColors(darkTheme: Boolean): CardColors {
    return if (darkTheme) {
      CardDefaults.cardColors(MaterialTheme.colorScheme.onError)
    } else {
      CardDefaults.cardColors(MaterialTheme.colorScheme.errorContainer)
    }
  }

  @Composable
  fun uncategorizedTaskColors(
      taskToBeDeleted: Boolean,
      darkTheme: Boolean
  ): CardColors {
    if (taskToBeDeleted) {
      return CardDefaults.cardColors(MaterialTheme.colorScheme.errorContainer)
    }
    // themed styles define their own custom-task color; the base themes keep
    // the signature gold
    val themedCustomColor = LocalThemeStyle.current.customTaskColor
    return when {
      themedCustomColor != null -> CardDefaults.cardColors(themedCustomColor)
      darkTheme -> CardDefaults.cardColors(rank_color_ultra_dark)
      else -> CardDefaults.cardColors(rank_color_ultra_light)
    }
  }

  @Composable
  fun specialTaskColors(darkTheme: Boolean): CardColors {
    return if (darkTheme) {
      CardDefaults.cardColors(epic_purple)
    } else {
      CardDefaults.cardColors(epic_purple_toned_down)
    }
  }
}