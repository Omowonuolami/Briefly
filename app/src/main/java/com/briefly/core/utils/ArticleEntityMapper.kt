package com.briefly.core.utils

import com.briefly.data.dto.ArticleEntity
import com.briefly.domain.model.Article

fun ArticleEntity.toDomain(): Article {
    return Article(
        id = id,
        title = title,
        description = description,
        content = content,
        imageUrl = imageUrl,
        author = author,
        readTime = readTime,
        category = category,
        tags = tags.split(",").filter { it.isNotEmpty() },
        publishedAt = publishedAt,
        url = url
    )
}

fun Article.toEntity(): ArticleEntity {
    return ArticleEntity(
        id          = id,
        title       = title,
        description = description?: "",
        content     = content,
        imageUrl    = imageUrl,
        author      = author,
        readTime    = readTime,
        category    = category,
        tags        = tags.joinToString(","),
        publishedAt = publishedAt,
        url         = url
    )
}