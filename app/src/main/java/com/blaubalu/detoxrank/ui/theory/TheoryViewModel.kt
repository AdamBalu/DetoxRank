package com.blaubalu.detoxrank.ui.theory

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blaubalu.detoxrank.data.chapter.Chapter
import com.blaubalu.detoxrank.data.chapter.ChaptersRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TheoryViewModel(
    private val chaptersRepository: ChaptersRepository
) : ViewModel() {
    val theoryHomeUiState: StateFlow<TheoryHomeUiState> = chaptersRepository
        .getAllChapters()
        .map { TheoryHomeUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = TheoryHomeUiState()
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    var chapterUiState by mutableStateOf(ChapterUiState())
        private set

    fun updateUiState(newChapterUiState: ChapterUiState) {
        chapterUiState = newChapterUiState.copy()
    }

    suspend fun insertChapterToChapterDatabase() {
        chaptersRepository.insertChapter(chapterUiState.toChapter())
    }

    private val _progressBarProgression = mutableStateOf(0f)
    private val _currentChapterScreenNum = mutableStateOf(0)
    val currentChapterScreenNum: MutableState<Int>
        get() = _currentChapterScreenNum

    private val _currentChapterName = mutableStateOf("")
    val currentChapterName: MutableState<String>
        get() = _currentChapterName

    fun setCurrentChapterName(name: String) {
        _currentChapterName.value = name
    }
    fun getChapterByName(name: String) = chaptersRepository.getChapterByName(name)

    suspend fun setCurrentChapterScreenNum() {
        val chapter = getChapterByName(_currentChapterName.value).first()
        _currentChapterScreenNum.value = chapter?.screenNum ?: 0
    }

    fun setChapterCompletionValue(chapter: Chapter?) {
        if (chapter == null)
            return
        viewModelScope.launch {
            chaptersRepository.updateChapter(chapter.copy(wasCompleted = true))
        }
    }

    fun getProgressBarValue(): Float {
        return _progressBarProgression.value
    }

    fun updateProgressBarProgression(valueToAdd: Float) {
        _progressBarProgression.value += valueToAdd
    }

    fun resetProgressBarProgression() {
        _progressBarProgression.value = 0f
    }

    fun calculateProgressBarAddition(screenNum: Int): Float =
        if (screenNum > 1)
            (1 / (screenNum - 1).toFloat())
        else 0f
}

/**
 * Ui State for TasksHomeContent
 */
data class TheoryHomeUiState(val chapterList: List<Chapter> = listOf())