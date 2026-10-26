package com.briefly.ui.screen.trending

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.briefly.domain.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrendingViewModel @Inject constructor(): ViewModel() {
    private val _uiState = MutableStateFlow(TrendingUiState())
    val uiState: StateFlow<TrendingUiState> = _uiState.asStateFlow()

    init { loadTrending() }

    fun loadTrending() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // Simulate network delay
            delay(800L)

            _uiState.update {
                it.copy(
                    isLoading = false,
                    featuredArticle = fakeFeatured,
                    mostReadArticles = fakeMostRead,
                    viralArticles = fakeViral
                )
            }
        }
    }

    fun onChipSelected(chip: String) {
        _uiState.update { it.copy(selectedChip = chip) }
        loadTrending()
    }

    fun onRetry() { loadTrending() }

}

// ── Fake data ─────────────────────────────────────────────────────────
val fakeFeatured = Article(
    id = "f1",
    title = "The Global Shift to Decentralized Infrastructure",
    content = "As centralized systems face unprecedented stress, a new era of...",
    imageUrl = "https://picsum.photos/seed/featured/600/400",
    category = "Macro Analysis",
    author = "Elena Restrova",
    readTime = "12 min read",
    publishedAt = "MACRO ANALYSIS"
)

val fakeMostRead = listOf(
    Article(
        id = "m1",
        title = "The hidden cost of zero-interest rate withdrawal in emerging markets.",
        content = "",
        imageUrl = "",
        category = "Economics",
        author = "",
        readTime = "8 min read",
        publishedAt = "UPDATED 3H AGO"
    ),
    Article(
        id = "m2",
        title = "Why generative AI is facing a hardware ceiling sooner than expected.",
        content = "",
        imageUrl = "",
        category = "Tech",
        author = "",
        readTime = "15 min read",
        publishedAt = "EDITORIAL PICK"
    ),
    Article(
        id = "m3",
        title = "Breakthrough in fusion containment marks a turning point for energy.",
        content = "",
        imageUrl = "",
        category = "Science",
        author = "",
        readTime = "6 min read",
        publishedAt = "TRENDING IN PHYSICS"
    )
)

val fakeViral = listOf(
    Article(
        id = "v1",
        title = "The resurgence of brutalist digital aesthetics.",
        content = "How designers are rejecting the polished 'SaaS' look for something more human.",
        imageUrl = "https://picsum.photos/seed/brutalist/600/400",
        category = "Art & Design",
        author = "Sara Henley",
        readTime = "5 min read"
    ),
    Article(
        id = "v2",
        title = "Why 'Slow Living' became the ultimate status symbol.",
        content = "In a hyper-connected world, the most luxury item is now uninterrupted time.",
        imageUrl = "https://picsum.photos/seed/slowliving/600/400",
        category = "Society",
        author = "Marcus Bell",
        readTime = "7 min read"
    )
)

data class TrendingUiState(
    val featuredArticle: Article? = null,
    val mostReadArticles: List<Article> = emptyList(),
    val viralArticles: List<Article> = emptyList(),
    val selectedChip: String = "#climatepolicy",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

val trendingChips = listOf(
    "#climatepolicy",
    "#web3",
    "#urbanism",
    "#ai",
    "#finance"
)

data class BottomNavItem(val label: String, val route: String)

val bottomNavItems = listOf(
    BottomNavItem("Home", "home"),
    BottomNavItem("Search", "search"),
    BottomNavItem("Saved", "saved"),
    BottomNavItem("Trending", "trending"),
    BottomNavItem("Profile", "profile")
)