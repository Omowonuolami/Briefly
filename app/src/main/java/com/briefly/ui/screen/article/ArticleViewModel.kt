package com.briefly.ui.screen.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.briefly.core.utils.ArticleScraper
import com.briefly.domain.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val articleScraper: ArticleScraper
) : ViewModel() {
    private val _uiState = MutableStateFlow(ArticleUiState())
    val uiState: StateFlow<ArticleUiState> = _uiState.asStateFlow()

    private var lastScrapedUrl: String = ""  // ← guard

    fun loadArticle(article: Article, relatedArticles: List<Article> = emptyList()) {
        _uiState.update {
            it.copy(
                isLoading = false,
                article = article,
                relatedArticles = relatedArticles
            )
        }

        // Don't re-scrape same URL
        if (article.url.isEmpty() || article.url == lastScrapedUrl) return

        viewModelScope.launch {
            lastScrapedUrl = article.url
            val fullContent = articleScraper.scrapeFullContent(article.url)

            if (fullContent != null) {
                _uiState.update {
                    it.copy(
                        article = article.copy(
                            content = fullContent
                                .replace(Regex("""\[\+\d+ chars\]"""), "")
                                .trim()
                        )
                    )
                }
            }
        }
    }

    fun onBookmarkToggle() {
        _uiState.update { it.copy(isBookmarked = !it.isBookmarked) }
    }

    fun onDrawerOpen() {
        _uiState.update { it.copy(isDrawerOpen = true) }
    }

    fun onDrawerClose() {
        _uiState.update { it.copy(isDrawerOpen = false) }
    }

    fun onCategorySelected(category: String) {
        _uiState.update {
            it.copy(
                selectedCategory = category,
                isDrawerOpen = false
            )
        }
    }
}

// ── Fake data for testing ─────────────────────────────────────────────
val fakeArticleDetail = Article(
    id = "1",
    title = "The Silent Shift: How Digital Currencies are Redefining Sovereignty in the 21st Century.",
    content = """
        As traditional financial systems grapple with the velocity of global change, a new architecture of value is emerging from the periphery of the established order.
        
        The transition began not with a grand announcement, but with a series of quiet experiments in decentralized protocols. What was once dismissed as a niche pursuit for cryptographers has matured into a foundational challenge to how we define national boundaries in a borderless digital landscape.
        
        At the heart of this transformation lies the concept of Tonal Layering—the idea that value, like design, can exist in multiple dimensions simultaneously. Central banks are no longer the sole arbiters of trust; instead, that trust is being distributed across immutable ledgers.
        
        Consider the implications for emerging markets. In regions where local currencies have historically suffered from volatility, digital assets provide a "floor and ink" philosophy of stability—a tactile sense of security in a virtual world. This isn't merely utility; it's a form of economic journalism, chronicling the rise of the individual over the institution.
        
        In the coming decade, the distinction between "The Story" and "The System" will blur. We will read news that directly influences the smart contracts we hold. We will participate in a curated economy where the primary currency is attention and the secondary accent is trust—the muted gold of the digital era.
    """.trimIndent(),
    imageUrl = "https://picsum.photos/seed/digital/600/400",
    category = "Economics",
    author = "Elena Restrova",
    readTime = "8 min read",
    tags = listOf("BLOCKCHAIN", "GLOBAL POLICY", "FUTURE TRENDS"),
    publishedAt = "ECONOMICS · FINANCE"
)

val fakeRelatedArticles = listOf(
    Article(
        id = "r1",
        title = "The Green Algorithm: Reducing the Carbon Footprint of AI",
        content = "How the tech industry is reckoning with its environmental cost.",
        imageUrl = "https://picsum.photos/seed/green/600/400",
        category = "Technology",
        author = "James Obi",
        readTime = "5 min read"
    ),
    Article(
        id = "r2",
        title = "The Return of the Physical: Why Gen Z is Embracing Analog Media",
        content = "Vinyl, film cameras, and handwritten letters are making a comeback.",
        imageUrl = "https://picsum.photos/seed/analog/600/400",
        category = "Culture",
        author = "Sara Henley",
        readTime = "4 min read"
    ),
    Article(
        id = "r3",
        title = "Infrastructure as Art: The New Aesthetics of Public Transit",
        content = "Cities are reimagining transit spaces as civic monuments.",
        imageUrl = "https://picsum.photos/seed/transit/600/400",
        category = "Economics",
        author = "Marcus Bell",
        readTime = "6 min read"
    )
)
