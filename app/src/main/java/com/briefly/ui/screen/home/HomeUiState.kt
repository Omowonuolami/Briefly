package com.briefly.ui.screen.home

import com.briefly.domain.model.Article

data class HomeUiState(
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedCategory: String = "All"
)

val categories = listOf(
    "All",
    "Technology",
    "Business"
)

data class BottomNavItem(val label: String, val route: String)

val bottomNavItems = listOf(
    BottomNavItem("Home", "home"),
    BottomNavItem("Search", "search"),
    BottomNavItem("Saved", "saved"),
    BottomNavItem("Trending", "trending"),
    BottomNavItem("Profile", "profile")
)