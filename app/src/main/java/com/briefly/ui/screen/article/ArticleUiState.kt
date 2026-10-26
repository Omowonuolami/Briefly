package com.briefly.ui.screen.article

import com.briefly.domain.model.Article
import kotlin.math.abs

data class ArticleUiState(
    val isLoading: Boolean = false,
    val article: Article? = null,
    val relatedArticles: List<Article> = emptyList(),
    val isBookmarked: Boolean = false,
    val isDrawerOpen: Boolean = false,
    val selectedCategory: String = "All",
    val errorMessage: String? = null
)
// ── Quotes by category ────────────────────────────────────────────────
private val technologyQuotes = listOf(
    "\"Technology is best when it brings people together.\"",
    "\"The advance of technology is based on making it fit in so that you don't really even notice it.\"",
    "\"Innovation distinguishes between a leader and a follower.\"",
    "\"The science of today is the technology of tomorrow.\""
)

private val businessQuotes = listOf(
    "\"The secret of getting ahead is getting started.\"",
    "\"Success is not final; failure is not fatal: it is the courage to continue that counts.\"",
    "\"The best investment you can make is in yourself.\"",
    "\"Your most unhappy customers are your greatest source of learning.\""
)

private val generalQuotes = listOf(
    "\"Journalism is the first rough draft of history.\"",
    "\"The role of a great journalist is to give voice to the voiceless.\"",
    "\"News is what somebody somewhere wants to suppress; all the rest is advertising.\"",
    "\"The truth is rarely pure and never simple.\""
)

fun getQuoteForArticle(article: Article): String {
    val quotes = when (article.category.lowercase()) {
        "technology" -> technologyQuotes
        "business"   -> businessQuotes
        else         -> generalQuotes
    }
    // Use article id hash to pick consistently but differently per article
    val index = abs(article.id.hashCode()) % quotes.size
    return quotes[index]
}
