package com.briefly.ui.screen.search

import androidx.lifecycle.ViewModel
import com.briefly.domain.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(): ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun setArticles(articles: List<Article>) {
        _uiState.update { it.copy(allArticles = articles) }
    }

    fun onQueryChange(query: String) {
        _uiState.update {
            it.copy(
                query = query,
                searchResults = if (query.isBlank()) emptyList()
                else it.allArticles.filter { article ->
                    article.title.contains(query, ignoreCase = true) ||
                            article.content.contains(query, ignoreCase = true) ||
                            article.category.contains(query, ignoreCase = true) ||
                            article.author.contains(query, ignoreCase = true)
                }
            )
        }
    }
}
data class SearchUiState(
    val query: String = "",
    val allArticles: List<Article> = emptyList(),
    val searchResults: List<Article> = emptyList()
)

// ── Trending topics ───────────────────────────────────────────────────
val trendingTopics = listOf(
    "Tesla", "SpaceX",
    "AI Ethics",      "Venice Biennale",
    "Monaco GP",      "Quantum Computing"
)

// ── Recommended articles (swap with real data / ViewModel) ────────────
val recommendedArticles = listOf(
    Article(
        id = "r1",
        title = "The Silent Architecture of Global Connectivity",
        content = "How subsea cables and orbital arrays are quietly reshaping the geography of information in the...",
        imageUrl = "https://picsum.photos/seed/connectivity/600/400",
        category = "Technology",
        author = "Dana Osei",
        readTime = "5 min read"
    ),
    Article(
        id = "r2",
        title = "Concrete Dreams: The New Wave of Mediterranean Minimalism",
        content = "Exploring the resurgence of brutalist philosophy in modern coastal residential design and...",
        imageUrl = "https://picsum.photos/seed/minimalism/600/400",
        category = "Culture",
        author = "Lea Fontaine",
        readTime = "6 min read"
    ),
    Article(
        id = "r3",
        title = "Beyond Gold: The Psychology of Alternative Value",
        content = "As traditional markets fluctuate, we examine why investors are turning toward historical assets a...",
        imageUrl = "https://picsum.photos/seed/finance/600/400",
        category = "Economics",
        author = "James Obi",
        readTime = "12 min read"
    )
)