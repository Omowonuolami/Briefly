package com.briefly.core.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import javax.inject.Inject

class ArticleScraper @Inject constructor() {

    suspend fun scrapeFullContent(url: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(10_000)
                    .maxBodySize(0)
                    .get()

                // Try common article content selectors
                val content = doc.selectFirst("article")
                    ?: doc.selectFirst("[class*='article-body']")
                    ?: doc.selectFirst("[class*='article-content']")
                    ?: doc.selectFirst("[class*='story-body']")
                    ?: doc.selectFirst("[class*='post-content']")
                    ?: doc.selectFirst("main")

                // Remove noise elements
                content?.select(
                    "script, style, nav, header, footer, iframe, " +
                            ".ad, .advertisement, .social-share, .related, " +
                            ".newsletter, .comments, figure"
                )?.remove()

                content?.text()?.takeIf { it.isNotBlank() }

            } catch (e: Exception) {
                null  // fail silently, fall back to description
            }
        }
    }
}