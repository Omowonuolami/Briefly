package com.briefly.ui.screen.profile

data class ProfileUiState(
    val name: String = "Elena Vance",
    val bio: String = "Senior Policy Analyst • Curator of Minimalist Design",
    val memberSince: String = "2023",
    val avatarUrl: String? = null,          // null = show initials
    val articlesRead: String = "1,248",
    val timeSpent: String = "42h",
    val streak: String = "14 Days",
    val isPro: Boolean = true,
    val interests: List<String> = listOf(
        "Global Economics",
        "Modern Architecture",
        "Sustainability",
        "Tech Policy",
        "Classical Music"
    ),
    val readingHabits: List<ReadingHabit> = listOf(
        ReadingHabit("Technology", 0.45f, HabitColor.RED),
        ReadingHabit("Economics", 0.30f, HabitColor.GOLD),
        ReadingHabit("Culture", 0.25f, HabitColor.BLUE)
    ),
    val isDarkMode: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class ReadingHabit(
    val category: String,
    val progress: Float,        // 0f to 1f
    val color: HabitColor
)

enum class HabitColor { RED, GOLD, BLUE }