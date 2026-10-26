package com.briefly.ui.screen.article

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.FormatSize
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.briefly.domain.model.Article
import com.briefly.ui.screen.home.CategoriesDrawer
import com.briefly.ui.theme.BgDark
import com.briefly.ui.theme.BodyDark
import com.briefly.ui.theme.BodyGrey
import com.briefly.ui.theme.BrieflyRed
import com.briefly.ui.theme.Cream
import com.briefly.ui.theme.DividerC
import com.briefly.ui.theme.DmSans
import com.briefly.ui.theme.LightBorder
import com.briefly.ui.theme.LightDivider
import com.briefly.ui.theme.LightDrawerBg
import com.briefly.ui.theme.LightInk
import com.briefly.ui.theme.WarmWhite
import com.briefly.ui.theme.newsReader

@Composable
fun ArticleScreen(
    article: Article,
    isBookmarked: Boolean = false,
    onBookmarkToggle: () -> Unit = {},
    relatedArticle: List<Article> = emptyList(),  // ← add this
    onBack: () -> Unit,
    onArticleClick: (Article) -> Unit,
    onCategoryNavigate: (String) -> Unit,
    viewModel: ArticleViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(article.id) {
        viewModel.loadArticle(article, relatedArticle)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── TOP BAR ──────────────────────────────────────────────
            ArticleTopBar(
                onHamburgerClick = { viewModel.onDrawerOpen() },
                onBack = onBack
            )

            HorizontalDivider(thickness = 0.5.dp, color = DividerC)

            // ── CONTENT ───────────────────────────────────────────────
            when {
                uiState.isLoading -> {
                    ArticleLoadingSkeleton()
                }
                uiState.errorMessage != null -> {
                    ErrorState(
                        message = uiState.errorMessage!!,
                        onRetry = { viewModel.loadArticle(article) }
                    )
                }
                uiState.article != null -> {
                    ArticleContent(
                        article = uiState.article!!,
                        relatedArticles = uiState.relatedArticles,
                        isBookmarked = isBookmarked,
                        onBookmarkToggle = onBookmarkToggle,
                        onArticleClick = onArticleClick
                    )
                }
            }
        }

        // ── CATEGORIES DRAWER ─────────────────────────────────────────
        if (uiState.isDrawerOpen) {
            CategoriesDrawer(
                selectedCategory = uiState.selectedCategory,
                onCategorySelected = { category ->
                    viewModel.onCategorySelected(category)
                    onCategoryNavigate(category)
                },
                onDismiss = viewModel::onDrawerClose
            )
        }
    }
}

// ── Top Bar ───────────────────────────────────────────────────────────
@Composable
fun ArticleTopBar(
    onHamburgerClick: () -> Unit,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Cream)
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        // Hamburger left
        IconButton(
            onClick = onHamburgerClick,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(36.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.padding(4.dp)
            ) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .width(22.dp)
                            .height(1.5.dp)
                            .background(BrieflyRed)
                    )
                }
            }
        }

        // Briefly centered
        Text(
            text = "Briefly",
            style = TextStyle(
                fontFamily = newsReader,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontSize = 26.sp,
                color = LightInk
            ),
            modifier = Modifier.align(Alignment.Center)
        )

        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(32.dp)
                .border(1.5.dp, LightInk, RoundedCornerShape(50))
                .clip(RoundedCornerShape(50))
                .background(Cream)
                .clickable { },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "E",
                style = TextStyle(
                    fontFamily = DmSans,
                    fontSize = 13.sp,
                    color = LightInk,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}

// ── Full Article Content ──────────────────────────────────────────────
@Composable
fun ArticleContent(
    article: Article,
    relatedArticles: List<Article>,
    isBookmarked: Boolean,
    onBookmarkToggle: () -> Unit,
    onArticleClick: (Article) -> Unit
) {
    val paragraphs = remember(article.content) {
        val text = article.content
            .replace("\r\n", "\n")
            .replace(Regex("""\[\+\d+ chars\]"""), "")
            .trim()

        val byDoubleNewline = text.split("\n\n")
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        if (byDoubleNewline.size > 1) {
            byDoubleNewline
        } else {
            // Split into ~400 char chunks at sentence boundaries
            val sentences = text.split(Regex("(?<=[.!?])\\s+"))
            val chunks = mutableListOf<String>()
            val current = StringBuilder()
            sentences.forEach { sentence ->
                if (current.length + sentence.length > 400 && current.isNotEmpty()) {
                    chunks.add(current.toString().trim())
                    current.clear()
                }
                current.append(sentence).append(" ")
            }
            if (current.isNotEmpty()) chunks.add(current.toString().trim())
            chunks.filter { it.isNotEmpty() }
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream),
        contentPadding = PaddingValues(bottom = 40.dp)
    ) {

        item {
            AsyncImage(
                model = article.imageUrl.ifEmpty {
                    "https://picsum.photos/seed/${article.id}/600/400"
                },
                contentDescription = article.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                contentScale = ContentScale.Crop
            )
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {

                Spacer(Modifier.height(20.dp))

                // Breadcrumb — "ECONOMICS · FINANCE"
                Text(
                    text = article.publishedAt.uppercase(),
                    style = TextStyle(
                        fontFamily = DmSans,
                        fontSize = 9.sp,
                        letterSpacing = 0.18.em,
                        color = BodyGrey
                    )
                )

                Spacer(Modifier.height(12.dp))

                // Main headline
                Text(
                    text = article.title,
                    style = TextStyle(
                        fontFamily = newsReader,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        color = BodyDark,
                        lineHeight = 38.sp
                    )
                )

                Spacer(Modifier.height(20.dp))

                HorizontalDivider(thickness = 0.5.dp, color = DividerC)

                Spacer(Modifier.height(16.dp))

                // Author row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Avatar circle
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2A2724)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = article.author.first().toString(),
                            style = TextStyle(
                                fontFamily = DmSans,
                                fontSize = 14.sp,
                                color = WarmWhite,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                    Column {
                        Text(
                            text = article.author,
                            style = TextStyle(
                                fontFamily = DmSans,
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp,
                                color = BodyDark
                            )
                        )
                        Text(
                            text = "Lead Editor",
                            style = TextStyle(
                                fontFamily = DmSans,
                                fontSize = 11.sp,
                                color = BodyGrey
                            )
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Action icons row — share, bookmark, text size, more
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = "Share",
                        tint = BodyGrey,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { }
                    )
                    Icon(
                        imageVector = if (isBookmarked)
                            Icons.Outlined.Bookmark
                        else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (isBookmarked) BrieflyRed else BodyGrey,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { onBookmarkToggle() }
                    )
                    Icon(
                        imageVector = Icons.Outlined.FormatSize,
                        contentDescription = "Text size",
                        tint = BodyGrey,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { }
                    )
                    Icon(
                        imageVector = Icons.Outlined.MoreHoriz,
                        contentDescription = "More",
                        tint = BodyGrey,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { }
                    )
                }

                Spacer(Modifier.height(16.dp))

                HorizontalDivider(thickness = 0.5.dp, color = DividerC)

                Spacer(Modifier.height(20.dp))
            }
        }

        // ── ARTICLE BODY ─────────────────────────────────────────────
        // Split content into paragraphs and render each
//        val paragraphs = article.content.split("\n\n")

        paragraphs.forEachIndexed { index, paragraph ->
            item(key = "paragraph_$index") {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {

                    // Insert pull quote after paragraph 2
                    if (index == 2 && paragraphs.size > 2) {
                        PullQuoteCard(
                            quote = getQuoteForArticle(article)  // ← dynamic quote
                        )
                        Spacer(Modifier.height(20.dp))
                    }

                    // Insert inline image after paragraph 3
                    if (index == 3 && paragraphs.size > 3) {
                        InlineImage(
                            imageUrl = article.imageUrl.ifEmpty {
                                "https://picsum.photos/seed/${article.id}/600/350"
                            },
                            caption = "Photo: ${article.tags.firstOrNull() ?: article.category}"
                        )
                        Spacer(Modifier.height(20.dp))
                    }

                    Text(
                        text = paragraph.trim(),
                        style = TextStyle(
                            fontFamily = DmSans,
                            fontSize = 16.sp,
                            color = BodyDark,
                            lineHeight = 28.sp,
                            textAlign = TextAlign.Start   // ← add this
                        )
                    )

                    Spacer(Modifier.height(20.dp))
                }
            }
        }

        // ── TAGS ─────────────────────────────────────────────────────
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                HorizontalDivider(thickness = 0.5.dp, color = DividerC)
                Spacer(Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    article.tags.forEach { tag ->
                        Box(
                            modifier = Modifier
                                .border(0.5.dp, WarmWhite, RoundedCornerShape(2.dp))
                                .background(LightBorder, RoundedCornerShape(2.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = tag,
                                style = TextStyle(
                                    fontFamily = DmSans,
                                    fontSize = 9.sp,
                                    letterSpacing = 0.14.em,
                                    color = BodyGrey
                                )
                            )
                        }
                    }
                }

                Spacer(Modifier.height(32.dp))
            }
        }

        // ── CONTINUE READING ─────────────────────────────────────────
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "Continue Reading",
                    style = TextStyle(
                        fontFamily = newsReader,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = BodyDark
                    )
                )
                Spacer(Modifier.height(16.dp))
            }
        }

        items(
            items = relatedArticles,
            key = { it.id }
        ) { related ->
            RelatedArticleCard(
                article = related,
                onClick = { onArticleClick(related) }
            )
        }
    }
}

// ── Pull Quote Card ───────────────────────────────────────────────────
@Composable
fun PullQuoteCard(quote: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LightDrawerBg)
    ) {
        // Left red bar
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(80.dp)        // ← fixed height instead of fillMaxHeight
                .background(BrieflyRed)
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = quote,
            style = TextStyle(
                fontFamily = newsReader,
                fontStyle = FontStyle.Italic,
                fontSize = 18.sp,
                color = BodyDark,
                lineHeight = 28.sp
            ),
            modifier = Modifier.padding(vertical = 16.dp)
        )
    }
}

// ── Inline Image with Caption ─────────────────────────────────────────
@Composable
fun InlineImage(imageUrl: String, caption: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        AsyncImage(
            model = imageUrl,
            contentDescription = caption,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = caption,
            style = TextStyle(
                fontFamily = DmSans,
                fontSize = 11.sp,
                color = BodyGrey,
                fontStyle = FontStyle.Italic
            )
        )
    }
}

// ── Related Article Card ──────────────────────────────────────────────
@Composable
fun RelatedArticleCard(
    article: Article,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Thumbnail
        AsyncImage(
            model = article.imageUrl,
            contentDescription = article.title,
            modifier = Modifier
                .width(100.dp)
                .height(80.dp)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop
        )

        // Text block
        Column(
            modifier = Modifier
                .weight(1f)
                .height(80.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Category
            Text(
                text = article.category.uppercase(),
                style = TextStyle(
                    fontFamily = DmSans,
                    fontSize = 9.sp,
                    letterSpacing = 0.14.em,
                    color = BodyGrey
                )
            )
            // Title
            Text(
                text = article.title,
                style = TextStyle(
                    fontFamily = newsReader,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = BodyDark,
                    lineHeight = 20.sp
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }

    HorizontalDivider(
        thickness = 0.5.dp,
        color = DividerC,
        modifier = Modifier.padding(horizontal = 20.dp)
    )
}

// ── Loading Skeleton ──────────────────────────────────────────────────
@Composable
fun ArticleLoadingSkeleton() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Breadcrumb skeleton
        Box(
            modifier = Modifier
                .width(120.dp)
                .height(10.dp)
                .background(Color(0xFFE8E3DC), RoundedCornerShape(4.dp))
        )
        // Title skeleton
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .background(Color(0xFFE8E3DC), RoundedCornerShape(4.dp))
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(36.dp)
                .background(Color(0xFFE8E3DC), RoundedCornerShape(4.dp))
        )
        Spacer(Modifier.height(8.dp))
        // Author skeleton
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8E3DC))
            )
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(12.dp)
                        .background(Color(0xFFE8E3DC), RoundedCornerShape(4.dp))
                )
                Box(
                    modifier = Modifier
                        .width(140.dp)
                        .height(10.dp)
                        .background(Color(0xFFE8E3DC), RoundedCornerShape(4.dp))
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        // Body skeleton lines
        repeat(6) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(if (it == 5) 0.6f else 1f)
                    .height(14.dp)
                    .background(Color(0xFFE8E3DC), RoundedCornerShape(4.dp))
            )
        }
    }
}

// ── Error State ───────────────────────────────────────────────────────
@Composable
fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = TextStyle(
                fontFamily = DmSans,
                fontSize = 14.sp,
                color = BodyGrey
            )
        )
        Spacer(Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(BrieflyRed)
                .clickable { onRetry() }
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text(
                text = "TRY AGAIN",
                style = TextStyle(
                    fontFamily = DmSans,
                    fontSize = 11.sp,
                    letterSpacing = 0.16.em,
                    color = Color.White
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArticleScreenPreview() {
    ArticleScreen(
        onBack = {},
        onArticleClick = {},
        onCategoryNavigate = {},
        article = Article(
            id = "1",
            title = "",
            author = "",
            content = "",
            category = "",
            imageUrl = "",
            publishedAt = "",
            readTime = "",
            tags = emptyList(),
        ),
    )
}