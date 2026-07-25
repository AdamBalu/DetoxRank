package com.blaubalu.detoxrank.ui.tasks.task

import com.blaubalu.detoxrank.data.task.Task
import com.blaubalu.detoxrank.data.task.TaskDurationCategory
import com.blaubalu.detoxrank.data.task.TaskIconCategory

data class TaskUiState(
    val id: Int = 0,
    val description: String = "",
    val completed: Boolean = false,
    val durationCategory: TaskDurationCategory = TaskDurationCategory.Uncategorized,
    val iconCategory: TaskIconCategory = TaskIconCategory.Other,
    val selectedAsCurrentTask: Boolean = false,
    val language: String = "EN",
    val specialTaskID: Int = 0,
    // carried through so a ui-state round trip never wipes them in the db
    val wasSelectedLastTime: Boolean = false,
    val lastSelectedTime: Long = 0,
    val sortOrder: Int = 0
)

fun TaskUiState.toTask(): Task = Task(
    id = id,
    description = description,
    completed = completed,
    durationCategory = durationCategory,
    iconCategory = iconCategory,
    selectedAsCurrentTask = selectedAsCurrentTask,
    language = language,
    specialTaskID = specialTaskID,
    wasSelectedLastTime = wasSelectedLastTime,
    lastSelectedTime = lastSelectedTime,
    sortOrder = sortOrder
)

fun Task.toTaskUiState(): TaskUiState = TaskUiState(
    id = id,
    description = description,
    completed = completed,
    durationCategory = durationCategory,
    iconCategory = iconCategory,
    selectedAsCurrentTask = selectedAsCurrentTask,
    language = language,
    specialTaskID = specialTaskID,
    wasSelectedLastTime = wasSelectedLastTime,
    lastSelectedTime = lastSelectedTime,
    sortOrder = sortOrder
)

fun TaskUiState.isValid(): Boolean = description.isNotBlank()