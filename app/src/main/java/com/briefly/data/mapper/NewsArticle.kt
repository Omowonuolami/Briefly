package com.briefly.data.mapper


import com.briefly.domain.model.Article
import com.briefly.domain.model.NewsArticle

fun NewsArticle.toDomain(): Article {
    return Article(
        id          = publishedAt + title,
        title       = title,
        description = description?: "",
        content     = content ?: "",
        imageUrl    = urlToImage ?: "",
        author      = author ?: source.name,
        readTime    = estimateReadTime(description ?: content ?: ""),
        category    = "",
        tags        = emptyList(),
        publishedAt = publishedAt.formatDate(),
        url         = url
    )
}

private fun estimateReadTime(text: String): String {
    val words = text.trim().split("\\s+".toRegex()).size
    val minutes = (words / 200).coerceAtLeast(1)  // avg 200 words per minute
    return "$minutes min read"
}

private fun String.formatDate(): String {
    return try {
        val inputFormat = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", java.util.Locale.getDefault())
        val outputFormat = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
        val date = inputFormat.parse(this)
        outputFormat.format(date ?: return this)
    } catch (e: Exception) {
        this
    }
}