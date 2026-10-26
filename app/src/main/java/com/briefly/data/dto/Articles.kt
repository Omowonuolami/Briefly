package com.briefly.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val content: String,
    val imageUrl: String,
    val author: String,
    val readTime: String,
    val category: String,
    val tags: String,           // store as comma-separated string
    val publishedAt: String,
    val url: String
)