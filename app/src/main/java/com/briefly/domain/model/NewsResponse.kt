package com.briefly.domain.model

import androidx.annotation.Keep


@Keep
data class NewsResponse (
    val status: String,
    val totalResults: Int,
    val articles: List<NewsArticle>
)

@Keep
data class NewsArticle(
    val source: Source,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?
)

@Keep
data class Source(
    val id: String?,
    val name: String
)

