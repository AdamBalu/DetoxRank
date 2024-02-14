package com.blaubalu.detoxrank.ui.tasks.task

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.blaubalu.detoxrank.R
import com.blaubalu.detoxrank.data.TimerDifficulty
import com.blaubalu.detoxrank.data.task.Task
import com.blaubalu.detoxrank.data.task.TaskDurationCategory
import com.blaubalu.detoxrank.service.TimerService
import com.blaubalu.detoxrank.ui.DetoxRankUiState
import com.blaubalu.detoxrank.ui.DetoxRankViewModel
import com.blaubalu.detoxrank.ui.DetoxRankViewModelProvider
import com.blaubalu.detoxrank.ui.rank.AchievementViewModel
import com.blaubalu.detoxrank.ui.tasks.home.TasksHeading
import com.blaubalu.detoxrank.ui.theme.Typography
import com.blaubalu.detoxrank.ui.utils.AnimationBox
import com.blaubalu.detoxrank.ui.utils.Constants.DAILY_TASK_RP_GAIN
import com.blaubalu.detoxrank.ui.utils.Constants.MONTHLY_TASK_RP_GAIN
import com.blaubalu.detoxrank.ui.utils.Constants.RP_PERCENTAGE_GAIN_TIMER_EASY_DIFFICULTY
import com.blaubalu.detoxrank.ui.utils.Constants.RP_PERCENTAGE_GAIN_TIMER_HARD_DIFFICULTY
import com.blaubalu.detoxrank.ui.utils.Constants.RP_PERCENTAGE_GAIN_TIMER_MEDIUM_DIFFICULTY
import com.blaubalu.detoxrank.ui.utils.Constants.SPECIAL_TASK_RP_GAIN
import com.blaubalu.detoxrank.ui.utils.Constants.UNCATEGORIZED_TASK_RP_GAIN
import com.blaubalu.detoxrank.ui.utils.Constants.WEEKLY_TASK_RP_GAIN
import com.blaubalu.detoxrank.ui.utils.RankPointsGain
import com.blaubalu.detoxrank.ui.utils.getIcon
import com.blaubalu.detoxrank.ui.utils.toastShort
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@Composable
fun TaskList(
    timerService: TimerService,
    taskList: List<Task>,
    detoxRankViewModel: DetoxRankViewModel,
    achievementViewModel: AchievementViewModel,
    modifier: Modifier = Modifier,
    taskViewModel: TaskViewModel = viewModel(factory = DetoxRankViewModelProvider.Factory)
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
    ) {
        renderTasksByCategory(
            taskList = taskList,
            timerService = timerService,
            detoxRankViewModel = detoxRankViewModel,
            achievementViewModel = achievementViewModel,
            taskViewModel = taskViewModel
        )
        item {
            Spacer(modifier = Modifier.padding(bottom = 75.dp))
        }
    }
}

@ExperimentalAnimationApi
private fun LazyListScope.renderTasksByCategory(
    taskList: List<Task>,
    timerService: TimerService,
    detoxRankViewModel: DetoxRankViewModel,
    achievementViewModel: AchievementViewModel,
    taskViewModel: TaskViewModel
) {
    TaskDurationCategory.entries.forEach { category ->
        val filteredTasks = taskList.filter { it.durationCategory == category }
        if (filteredTasks.isNotEmpty()) {
            getHeading(timerService, category)
            getItems(
                filteredTaskList = filteredTasks,
                detoxRankViewModel = detoxRankViewModel,
                achievementViewModel = achievementViewModel,
                taskViewModel = taskViewModel
            )
        }
    }
}

@ExperimentalAnimationApi
private fun LazyListScope.getHeading(timerService: TimerService, category: TaskDurationCategory) {
    val headingRes = when (category) {
        TaskDurationCategory.Uncategorized -> R.string.tasklist_heading_custom
        TaskDurationCategory.Daily -> R.string.tasklist_heading_daily
        TaskDurationCategory.Weekly -> R.string.tasklist_heading_weekly
        TaskDurationCategory.Monthly -> R.string.tasklist_heading_monthly
        TaskDurationCategory.Special -> R.string.tasklist_heading_special
    }

    return item {
        TasksHeading(
            headingRes = headingRes,
            timerService = timerService,
            category = category,
            iconImageVector = getCategoryIcon(category)
        )
    }
}

private fun getCategoryIcon(category: TaskDurationCategory): ImageVector {
    return when (category) {
        TaskDurationCategory.Uncategorized -> Icons.Filled.Face
        TaskDurationCategory.Daily -> Icons.Filled.Today
        TaskDurationCategory.Weekly -> Icons.Filled.DateRange
        TaskDurationCategory.Monthly -> Icons.Filled.CalendarMonth
        TaskDurationCategory.Special -> Icons.Filled.ElectricBolt
    }
}

private fun LazyListScope.getItems(
    filteredTaskList: List<Task>,
    detoxRankViewModel: DetoxRankViewModel,
    achievementViewModel: AchievementViewModel,
    taskViewModel: TaskViewModel,
) {
    return items(filteredTaskList) { task ->
        AnimationBox(
            enter = expandHorizontally() + fadeIn(),
            exit = fadeOut()
        ) {
            Task(
                task = task,
                detoxRankViewModel = detoxRankViewModel,
                achievementViewModel = achievementViewModel,
                taskViewModel = taskViewModel
            )
        }
    }
}

private fun getTaskMultiplier(uiState: DetoxRankUiState): Double = if (uiState.isTimerStarted) {
    when (uiState.currentTimerDifficulty) {
        TimerDifficulty.Easy -> RP_PERCENTAGE_GAIN_TIMER_EASY_DIFFICULTY
        TimerDifficulty.Medium -> RP_PERCENTAGE_GAIN_TIMER_MEDIUM_DIFFICULTY
        TimerDifficulty.Hard -> RP_PERCENTAGE_GAIN_TIMER_HARD_DIFFICULTY
    } / 100.0
} else {
    0.0
}

private fun getRPGain(task: Task, multiplier: Double): Int {
    val rpGainMap = mapOf(
        TaskDurationCategory.Daily to DAILY_TASK_RP_GAIN,
        TaskDurationCategory.Weekly to WEEKLY_TASK_RP_GAIN,
        TaskDurationCategory.Monthly to MONTHLY_TASK_RP_GAIN,
        TaskDurationCategory.Uncategorized to UNCATEGORIZED_TASK_RP_GAIN,
        TaskDurationCategory.Special to SPECIAL_TASK_RP_GAIN
    )

    return rpGainMap[task.durationCategory]?.let {
        (it + it * multiplier).toInt()
    } ?: 0
}

private suspend fun initializeUserTimerState(detoxRankViewModel: DetoxRankViewModel) {
    val timerDifficultyFromDb = detoxRankViewModel.getUserTimerDifficulty()
    detoxRankViewModel.setCurrentTimerDifficulty(timerDifficultyFromDb)
    val isTimerStartedFromDb = detoxRankViewModel.getUserTimerStarted()
    detoxRankViewModel.setTimerStarted(isTimerStartedFromDb)
}

private fun Modifier.buildTaskModifier(
    task: Task,
    coroutineScope: CoroutineScope,
    taskViewModel: TaskViewModel,
    achievementViewModel: AchievementViewModel,
    detoxRankViewModel: DetoxRankViewModel,
    rankPointsGain: Int,
    taskToBeRefreshed: MutableState<Boolean>,
    taskToBeDeleted: MutableState<Boolean>,
    context: Context
): Modifier {
    return this
        .padding(vertical = 4.dp, horizontal = 16.dp)
        .animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        )
        .height(if (task.completed || taskToBeDeleted.value) IntrinsicSize.Min else IntrinsicSize.Max)
        .pointerInput(task) {
            if (task.durationCategory == TaskDurationCategory.Uncategorized ||
                task.durationCategory == TaskDurationCategory.Special
            ) {
                detectTapGestures(
                    onTap = { handleSpecialCategoryTap(taskToBeDeleted, context) },
                    onLongPress = { taskToBeDeleted.value = !taskToBeDeleted.value },
                    onDoubleTap = {
                        handleSpecialCategoryDoubleTap(
                            task,
                            coroutineScope,
                            taskViewModel,
                            achievementViewModel,
                            detoxRankViewModel,
                            rankPointsGain
                        )
                    }
                )
            } else {
                detectTapGestures(
                    onTap = {
                        handleRegularCategoryTap(
                            task,
                            taskToBeRefreshed,
                            taskViewModel,
                            coroutineScope
                        )
                    },
                    onLongPress = { handleRegularCategoryLongPress(task, taskToBeRefreshed) }
                )
            }
        }
}

private fun handleSpecialCategoryTap(
    userTaskToBeDeleted: MutableState<Boolean>,
    context: Context,
) {
    if (userTaskToBeDeleted.value) {
        userTaskToBeDeleted.value = false
    } else {
        toastShort("Double tap to complete!", context)
    }
}

private fun handleSpecialCategoryDoubleTap(
    task: Task,
    coroutineScope: CoroutineScope,
    taskViewModel: TaskViewModel,
    achievementViewModel: AchievementViewModel,
    detoxRankViewModel: DetoxRankViewModel,
    rankPointsGain: Int
) {
    coroutineScope.launch {
        if (task.durationCategory == TaskDurationCategory.Special) {
            taskViewModel.updateUiState(
                task
                    .copy(completed = true, selectedAsCurrentTask = false)
                    .toTaskUiState()
            )
            achievementViewModel.achieveAchievement(task.specialTaskID)
            taskViewModel.updateTask()
        } else {
            taskViewModel.updateUiState(
                task
                    .copy(completed = true)
                    .toTaskUiState()
            )
            taskViewModel.updateTask()
            taskViewModel.deleteTask(task)
        }
        detoxRankViewModel.updateUserRankPoints(rankPointsGain)
    }
}

private fun handleRegularCategoryTap(
    task: Task,
    taskToBeRefreshed: MutableState<Boolean>,
    taskViewModel: TaskViewModel,
    coroutineScope: CoroutineScope
) {
    if (!taskToBeRefreshed.value) {
        taskViewModel.updateUiState(
            task
                .copy(completed = !task.completed)
                .toTaskUiState()
        )
        coroutineScope.launch {
            taskViewModel.updateTask()
        }
    } else {
        taskToBeRefreshed.value = !taskToBeRefreshed.value
    }
}

private fun handleRegularCategoryLongPress(
    task: Task,
    userTaskToBeRefreshed: MutableState<Boolean>
) {
    if (!task.completed)
        userTaskToBeRefreshed.value = !userTaskToBeRefreshed.value
}

@Composable
fun Task(
    task: Task,
    taskViewModel: TaskViewModel,
    detoxRankViewModel: DetoxRankViewModel,
    achievementViewModel: AchievementViewModel,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        initializeUserTimerState(detoxRankViewModel)
    }

    val uiState = detoxRankViewModel.uiState.collectAsState().value
    val multiplier = getTaskMultiplier(uiState)
    val rankPointsGain = getRPGain(task, multiplier)
    val darkTheme = isSystemInDarkTheme()
    val userTaskToBeDeleted = remember { mutableStateOf(false) }
    val taskToBeRefreshed = remember { mutableStateOf(false) }
    val taskColors = TaskColors()

    Card(
        modifier = modifier.buildTaskModifier(
            task,
            coroutineScope,
            taskViewModel,
            achievementViewModel,
            detoxRankViewModel,
            rankPointsGain,
            taskToBeRefreshed,
            userTaskToBeDeleted,
            context
        ),
        colors = TaskColors(
            task = task,
            taskColors = taskColors,
            taskToBeRefreshed = taskToBeRefreshed,
            userTaskToBeDeleted = userTaskToBeDeleted,
            darkTheme = darkTheme
        )
    ) {
        TaskContents(
            task = task,
            taskViewModel = taskViewModel,
            detoxRankViewModel = detoxRankViewModel,
            userTaskToBeDeleted = userTaskToBeDeleted,
            taskToBeRefreshed = taskToBeRefreshed,
            rankPointsGain = rankPointsGain,
            coroutineScope = coroutineScope,
            context = context,
            modifier = modifier
        )
    }
}

@Composable
fun TaskColors(
    task: Task,
    taskColors: TaskColors,
    taskToBeRefreshed: MutableState<Boolean>,
    userTaskToBeDeleted: MutableState<Boolean>,
    darkTheme: Boolean
): CardColors {
    if (task.completed) {
        return taskColors.completedTaskColors()
    }

    if (taskToBeRefreshed.value) {
        return taskColors.defaultTaskColors()
    }

    return when (task.durationCategory) {
        TaskDurationCategory.Daily -> taskColors.dailyTaskColors(darkTheme)
        TaskDurationCategory.Weekly -> taskColors.weeklyTaskColors(darkTheme)
        TaskDurationCategory.Monthly -> taskColors.monthlyTaskColors(darkTheme)
        TaskDurationCategory.Uncategorized -> taskColors.uncategorizedTaskColors(
            userTaskToBeDeleted.value,
            darkTheme
        )

        TaskDurationCategory.Special -> taskColors.specialTaskColors(darkTheme)
    }
}


