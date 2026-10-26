package com.briefly.domain.model

data class Article(
    val id: String,
    val title: String,
    val content: String,
    val description: String? = null,
    val imageUrl: String,
    val author: String,
    val readTime: String,
    val category: String = "",
    val tags: List<String> = emptyList(),
    val publishedAt: String = "",
    val url: String = ""           // ← add this
)