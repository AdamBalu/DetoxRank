package com.blaubalu.detoxrank.ui.utils

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.blaubalu.detoxrank.data.user.Rank

/**
 * Types of popups that can be shown
 */
enum class PopupType {
    RANK_UP,
    ACHIEVEMENT
}

/**
 * Data class representing a popup to be shown
 */
data class PopupData(
    val type: PopupType,
    val title: String,
    val description: String = "",
    val iconRes: Int? = null,
    val achievementId: Int? = null,
    val rank: Rank? = null
)


/**
 * Singleton manager for queuing and displaying popups.
 * Popups are shown one at a time in order.
 */
object PopupManager {
    private val _popupQueue: SnapshotStateList<PopupData> = mutableStateListOf()
    
    /**
     * The current popup to display, or null if queue is empty
     */
    val currentPopup: PopupData?
        get() = _popupQueue.firstOrNull()
    
    /**
     * Whether there's a popup to show
     */
    val hasPopup: Boolean
        get() = _popupQueue.isNotEmpty()
    
    /**
     * Add a popup to the queue
     */
    fun enqueue(popup: PopupData) {
        _popupQueue.add(popup)
    }
    
    /**
     * Dismiss the current popup and show the next one
     */
    fun dismiss() {
        if (_popupQueue.isNotEmpty()) {
            _popupQueue.removeAt(0)
        }
    }
    
    /**
     * Clear all popups
     */
    fun clearAll() {
        _popupQueue.clear()
    }
    
    /**
     * Convenience method to show a rank-up popup
     */
    fun showRankUp(newRank: Rank, iconRes: Int) {
        enqueue(
            PopupData(
                type = PopupType.RANK_UP,
                title = "Rank Up!",
                description = "Congratulations! You've reached ${newRank.rankName}!",
                iconRes = iconRes,
                rank = newRank
            )
        )
    }
    
    /**
     * Convenience method to show an achievement popup
     */
    fun showAchievement(achievementName: String, achievementDescription: String = "", achievementId: Int? = null) {
        enqueue(
            PopupData(
                type = PopupType.ACHIEVEMENT,
                title = "Achievement Unlocked!",
                description = achievementName,
                achievementId = achievementId
            )
        )
    }

}
