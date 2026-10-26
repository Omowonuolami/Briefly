package com.briefly.ui.screen.home

import androidx.lifecycle.ViewModel
import com.briefly.domain.model.Article
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import androidx.lifecycle.viewModelScope
import com.briefly.core.viewmodels.SharedViewModel
import com.briefly.domain.model.NewsResponse
import com.briefly.domain.usecases.homeUsecases.GetAppleNewsUseCase
import com.briefly.domain.usecases.homeUsecases.GetArticlesUseCase
import com.briefly.domain.usecases.homeUsecases.GetBusinessNewsUseCase
import com.briefly.domain.usecases.homeUsecases.GetTechCrunchNewsUseCase
import com.briefly.domain.usecases.homeUsecases.GetTeslaUseCase
import com.briefly.domain.usecases.homeUsecases.GetWallStreetJournalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getArticlesUseCase: GetArticlesUseCase,
    private val getTeslaUseCase: GetTeslaUseCase,
    private val getAppleNewsUseCase: GetAppleNewsUseCase,
    private val getBusinessNewsUseCase: GetBusinessNewsUseCase,
    private val getTechCrunchNewsUseCase: GetTechCrunchNewsUseCase,
    private val getWallStreetJournalUseCase: GetWallStreetJournalUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // All articles cached so filtering doesn't re-fetch
    private var allArticles: List<Article> = emptyList()

    init {
        observeArticles()
        loadArticles()
    }

    // Observe DB — updates UI whenever DB changes
    private fun observeArticles() {
        viewModelScope.launch {
            getArticlesUseCase().collect { articles ->
                allArticles = interleave(articles)
                _uiState.update {
                    it.copy(
                        articles = filterByCategory(allArticles, it.selectedCategory),
                        isLoading = false
                    )
                }
            }
        }
    }


    fun loadArticles() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val results = listOf(
                getTeslaUseCase()             to "Technology",
                getAppleNewsUseCase()         to "Technology",
                getBusinessNewsUseCase()      to "Business",
                getTechCrunchNewsUseCase()    to "Technology",
                getWallStreetJournalUseCase() to "Business"
            )

            val technologyArticles = mutableListOf<Article>()
            val businessArticles = mutableListOf<Article>()

            results
                .filter { (result, _) -> result.isSuccess }
                .forEach { (result, category) ->
                    val articles = result.getOrDefault(emptyList()).map {
                        it.copy(category = category)
                    }
                    when (category) {
                        "Technology" -> technologyArticles.addAll(articles)
                        "Business"   -> businessArticles.addAll(articles)
                    }
                }

            val interleaved = mutableListOf<Article>()
            val lists = listOf(technologyArticles, businessArticles)
            var hasMore = true
            while (hasMore) {
                hasMore = false
                lists.forEach { list ->
                    if (list.isNotEmpty()) {
                        interleaved.add(list.removeFirst())
                        hasMore = true
                    }
                }
            }

            val error = if (interleaved.isEmpty()) {
                results.firstOrNull { (result, _) -> result.isFailure }
                    ?.first?.exceptionOrNull()?.message ?: "Failed to load articles"
            } else null

            allArticles = interleaved

            _uiState.update {
                it.copy(
                    isLoading = false,
                    articles = filterByCategory(allArticles, it.selectedCategory),
                    errorMessage = error
                )
            }
        }
    }


    fun onCategorySelected(category: String) {
        _uiState.update {
            it.copy(
                selectedCategory = category,
                articles = filterByCategory(allArticles, category)
            )
        }
    }

    fun onRetry() {
        loadArticles()
    }

    private fun filterByCategory(articles: List<Article>, category: String): List<Article> {
        return if (category == "All") articles
        else articles.filter { it.category == category }
    }

    private fun interleave(articles: List<Article>): List<Article> {
        val technologyArticles = articles.filter { it.category == "Technology" }.toMutableList()
        val businessArticles = articles.filter { it.category == "Business" }.toMutableList()
        val interleaved = mutableListOf<Article>()
        val lists = listOf(technologyArticles, businessArticles)
        var hasMore = true
        while (hasMore) {
            hasMore = false
            lists.forEach { list ->
                if (list.isNotEmpty()) {
                    interleaved.add(list.removeFirst())
                    hasMore = true
                }
            }
        }
        return interleaved
    }
}

// ── Fake data for testing ─────────────────────────────────────────────
val fakeFeed = listOf(
    Article(
        id = "1",
        title = "The Silent Shift in Global Diplomacy",
        content = "As borders soften and digital alliances strengthen, a new era of international relations emerges from the shadows of traditional statecraft.",
        imageUrl = "https://picsum.photos/seed/diplomacy/600/400",
        category = "World News",
        author = "Eliza Vance",
        readTime = "8 min read"
    ),
    Article(
        id = "2",
        title = "Reviving the Lost Art of Long-Form Conversation",
        content = "In a world dominated by byte-sized interactions, a new movement is reclaiming the depth of slow, intentional dialogue.",
        imageUrl = "https://picsum.photos/seed/culture/600/400",
        category = "Culture",
        author = "Marcus Bell",
        readTime = "5 min read"
    ),
    Article(
        id = "3",
        title = "When Algorithms Dream: The Ethics of Generative AI",
        content = "The boundaries between human creativity and machine intelligence are blurring faster than our legal frameworks can adapt.",
        imageUrl = "https://picsum.photos/seed/ai/600/400",
        category = "Technology",
        author = "Priya Lal",
        readTime = "6 min read"
    ),
    Article(
        id = "4",
        title = "The Green Standard: New Metrics for Success",
        content = "Global markets are pivoting toward a future where environmental impact is as crucial as the bottom line.",
        imageUrl = "https://picsum.photos/seed/economics/600/400",
        category = "Economics",
        author = "James Obi",
        readTime = "4 min read"
    ),
    Article(
        id = "5",
        title = "The New Vanguard of Local Governance",
        content = "Small-town mayors are solving global problems with hyper-local solutions that bypass national gridlock.",
        imageUrl = "https://picsum.photos/seed/politics/600/400",
        category = "Politics",
        author = "Sara Henley",
        readTime = "7 min read"
    )
)
//data class BottomNavItem(val label: String, val route: String)
//
//val bottomNavItems = listOf(
//    BottomNavItem("Home", "home"),
//    BottomNavItem("Search", "search"),
//    BottomNavItem("Saved", "saved"),
//    BottomNavItem("Trending", "trending"),
//    BottomNavItem("Profile", "profile")
//)