package com.briefly.ui.screen.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.briefly.domain.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init { loadProfile() }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            kotlinx.coroutines.delay(600L)
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun onAvatarSelected(uri: Uri?) {
        _uiState.update { it.copy(avatarUrl = uri?.toString()) }
    }

    fun onToggleDarkMode(enabled: Boolean) {
        _uiState.update { it.copy(isDarkMode = enabled) }
    }

    fun onAddInterest(topic: String) {
        if (topic.isBlank()) return
        _uiState.update { it.copy(interests = it.interests + topic) }
    }

    fun onRemoveInterest(topic: String) {
        _uiState.update {
            it.copy(interests = it.interests.filter { t -> t != topic })
        }
    }

    fun onSignOut(onComplete: () -> Unit) {
        viewModelScope.launch {
            onComplete()
        }
    }

    // Called from NavGraph with real bookmarked articles
    fun updateStatsFromArticles(
        bookmarkedArticles: List<Article>,
        allArticles: List<Article>
    ) {
        if (allArticles.isEmpty()) return

        // Calculate reading habits from all articles
        val categoryCounts = allArticles
            .groupBy { it.category }
            .mapValues { it.value.size }

        val total = categoryCounts.values.sum().toFloat()

        val habits = categoryCounts.entries
            .sortedByDescending { it.value }
            .take(3)
            .mapIndexed { index, entry ->
                ReadingHabit(
                    category = entry.key,
                    progress = if (total > 0) entry.value / total else 0f,
                    color = when (index) {
                        0    -> HabitColor.RED
                        1    -> HabitColor.GOLD
                        else -> HabitColor.BLUE
                    }
                )
            }

        _uiState.update {
            it.copy(
                articlesRead = allArticles.size.toString(),
                streak = "${bookmarkedArticles.size} Saved",
                readingHabits = habits.ifEmpty { it.readingHabits }
            )
        }
    }
}