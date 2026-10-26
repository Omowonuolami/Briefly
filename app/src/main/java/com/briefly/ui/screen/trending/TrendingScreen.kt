package com.briefly.ui.screen.trending

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
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
import com.briefly.ui.theme.ArticleCate
import com.briefly.ui.theme.BodyDark
import com.briefly.ui.theme.BodyGrey
import com.briefly.ui.theme.BrieflyRed
import com.briefly.ui.theme.ChipBg
import com.briefly.ui.theme.ChipBorder
import com.briefly.ui.theme.ChipSelectedB
import com.briefly.ui.theme.Cream
import com.briefly.ui.theme.DividerLight
import com.briefly.ui.theme.DmSans
import com.briefly.ui.theme.LightDivider
import com.briefly.ui.theme.LightInk
import com.briefly.ui.theme.LightMuted
import com.briefly.ui.theme.NumberRed
import com.briefly.ui.theme.newsReader

@Composable
fun TrendingScreen(
    onArticleClick: (Article) -> Unit,
    onNavigate: (String) -> Unit,
    viewModel: TrendingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedNav by remember { mutableStateOf("trending") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── TOP BAR ──────────────────────────────────────────────────
            TrendingTopBar()

            HorizontalDivider(thickness = 0.5.dp, color = LightDivider)

            Box(modifier = Modifier.weight(1f)) {
                when {
                    uiState.isLoading -> TrendingLoadingSkeleton()
                    uiState.errorMessage != null -> TrendingErrorState(
                        message = uiState.errorMessage!!,
                        onRetry = viewModel::onRetry
                    )

                    else -> TrendingContent(
                        uiState = uiState,
                        onChipSelected = viewModel::onChipSelected,
                        onArticleClick = onArticleClick
                    )
                }
            }

            HorizontalDivider(thickness = 0.5.dp, color = LightDivider)

            // ── BOTTOM NAV ───────────────────────────────────────────
            BottomNavBar(
                selectedRoute = selectedNav,
                onItemSelected = { route ->
                    selectedNav = route
                    onNavigate(route)
                }
            )
        }
    }
}

// ── Top Bar ───────────────────────────────────────────────────────────
@Composable
fun TrendingTopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Cream)
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        // Hamburger — left
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable { }
                .padding(4.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            repeat(3) {
                Box(
                    modifier = Modifier
                        .width(22.dp)
                        .height(1.5.dp)
                        .background(LightInk)
                )
            }
        }

        // "BRIEFLY" — centered, all caps
        Text(
            text = "BRIEFLY",
            style = TextStyle(
                fontFamily = newsReader,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontSize = 26.sp,
//                letterSpacing = 0.12.em,
                color = LightInk
            ),
            modifier = Modifier.align(Alignment.Center)
        )

        // Bell icon — right
        Icon(
            imageVector = Icons.Outlined.NotificationsNone,
            contentDescription = "Notifications",
            tint = BodyDark,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(22.dp)
                .clickable { }
        )
    }
}

// ── Main Scrollable Content ───────────────────────────────────────────
@Composable
fun TrendingContent(
    uiState: TrendingUiState,
    onChipSelected: (String) -> Unit,
    onArticleClick: (Article) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {

        // ── HASHTAG CHIPS ─────────────────────────────────────────
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                trendingChips.forEach { chip ->
                    val isSelected = chip == uiState.selectedChip
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(2.dp))
                            .background(
                                if (isSelected) ChipSelectedB else ChipBg
                            )
                            .border(
                                0.5.dp,
                                if (isSelected) ChipSelectedB else ChipBorder,
                                RoundedCornerShape(2.dp)
                            )
                            .clickable { onChipSelected(chip) }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = chip,
                            style = TextStyle(
                                fontFamily = DmSans,
                                fontSize = 10.sp,
                                letterSpacing = 0.08.em,
                                color = if (isSelected) Color.White
                                else BodyGrey
                            )
                        )
                    }
                }
            }
        }

        // ── FEATURED CARD ─────────────────────────────────────────
        uiState.featuredArticle?.let { article ->
            item {
                FeaturedCard(
                    article = article,
                    onClick = { onArticleClick(article) }
                )
            }
        }

        // ── FEATURED ARTICLE TEXT (below image) ───────────────────
        uiState.featuredArticle?.let { article ->
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Cream)
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    // Title
                    Text(
                        text = article.title,
                        style = TextStyle(
                            fontFamily = newsReader,
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp,
                            color = BodyDark,
                            lineHeight = 32.sp
                        )
                    )

                    Spacer(Modifier.height(8.dp))

                    // Excerpt
                    Text(
                        text = article.content,
                        style = TextStyle(
                            fontFamily = DmSans,
                            fontSize = 14.sp,
                            color = BodyGrey,
                            lineHeight = 22.sp
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(Modifier.height(8.dp))

                    // Meta row
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = article.publishedAt.uppercase(),
                            style = TextStyle(
                                fontFamily = DmSans,
                                fontSize = 9.sp,
                                letterSpacing = 0.14.em,
                                color = BrieflyRed
                            )
                        )
                        Box(
                            modifier = Modifier
                                .size(3.dp)
                                .background(BodyGrey, RoundedCornerShape(50))
                        )
                        Text(
                            text = article.readTime.uppercase(),
                            style = TextStyle(
                                fontFamily = DmSans,
                                fontSize = 9.sp,
                                letterSpacing = 0.12.em,
                                color = BodyGrey
                            )
                        )
                    }
                }

                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = DividerLight,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(Modifier.height(24.dp))
            }
        }

        // ── MOST READ TODAY ───────────────────────────────────────
        item {
            Text(
                text = "Most Read Today",
                style = TextStyle(
                    fontFamily = newsReader,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp,
                    color = BodyDark
                ),
                modifier = Modifier.padding(
                    horizontal = 20.dp,
                    vertical = 4.dp
                )
            )
            Spacer(Modifier.height(8.dp))
        }

        itemsIndexed(
            items = uiState.mostReadArticles,
            key = { _, article -> article.id }
        ) { index, article ->
            MostReadItem(
                rank = index + 1,
                article = article,
                onClick = { onArticleClick(article) }
            )
        }

        item { Spacer(Modifier.height(32.dp)) }

        // ── VIRAL IN CULTURE ──────────────────────────────────────
        item {
            Text(
                text = "Viral in Culture",
                style = TextStyle(
                    fontFamily = newsReader,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp,
                    color = BodyDark
                ),
                modifier = Modifier.padding(
                    horizontal = 20.dp,
                    vertical = 4.dp
                )
            )
            Spacer(Modifier.height(16.dp))
        }

        itemsIndexed(
            items = uiState.viralArticles,
            key = { _, article -> article.id }
        ) { _, article ->
            ViralCard(
                article = article,
                onClick = { onArticleClick(article) }
            )
            Spacer(Modifier.height(8.dp))
        }
    }
}

// ── Featured Card — image only ────────────────────────────────────────
@Composable
fun FeaturedCard(
    article: Article,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(start = 16.dp, end = 16.dp)
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = article.imageUrl,
            contentDescription = article.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Dark gradient overlay at bottom
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.5f)
                        ),
                        startY = 100f
                    )
                )
        )

        // "FEATURED" pill — top left
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(BrieflyRed)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "FEATURED",
                style = TextStyle(
                    fontFamily = DmSans,
                    fontSize = 9.sp,
                    letterSpacing = 0.16.em,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}

// ── Most Read Item ────────────────────────────────────────────────────
@Composable
fun MostReadItem(
    rank: Int,
    article: Article,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Large muted rank number
        Text(
            text = "0$rank",
            style = TextStyle(
                fontFamily = newsReader,
                fontWeight = FontWeight.Bold,
                fontSize = 48.sp,
                color = NumberRed.copy(alpha= 0.10f),
                lineHeight = 48.sp
            )
        )

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {

            // Category
            Text(
                text = article.category.uppercase(),
                style = TextStyle(
                    fontFamily = DmSans,
                    fontSize = 9.sp,
                    letterSpacing = 0.16.em,
                    color = ArticleCate
                )
            )

            // Title
            Text(
                text = article.title,
                style = TextStyle(
                    fontFamily = newsReader,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = BodyDark,
                    lineHeight = 24.sp
                )
            )

            // Meta row
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = article.readTime.uppercase(),
                    style = TextStyle(
                        fontFamily = DmSans,
                        fontSize = 9.sp,
                        letterSpacing = 0.1.em,
                        color = BodyGrey
                    )
                )
                Box(
                    modifier = Modifier
                        .size(3.dp)
                        .background(BodyGrey, RoundedCornerShape(50))
                )
                Text(
                    text = article.publishedAt.uppercase(),
                    style = TextStyle(
                        fontFamily = DmSans,
                        fontSize = 9.sp,
                        letterSpacing = 0.1.em,
                        color = BodyGrey
                    )
                )
            }
        }
    }

    HorizontalDivider(
        thickness = 0.5.dp,
        color = DividerLight,
        modifier = Modifier.padding(horizontal = 20.dp)
    )
}

// ── Viral Card ────────────────────────────────────────────────────────
@Composable
fun ViralCard(
    article: Article,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Cream)
            .padding(start = 16.dp, end = 16.dp)
            .clickable { onClick() }
    ) {
        // Full width image
        AsyncImage(
            model = article.imageUrl,
            contentDescription = article.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            // Category
            Text(
                text = article.category.uppercase(),
                style = TextStyle(
                    fontFamily = DmSans,
                    fontSize = 9.sp,
                    letterSpacing = 0.16.em,
                    color = BrieflyRed
                )
            )

            Spacer(Modifier.height(6.dp))

            // Title
            Text(
                text = article.title,
                style = TextStyle(
                    fontFamily = newsReader,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = BodyDark,
                    lineHeight = 28.sp
                )
            )

            Spacer(Modifier.height(8.dp))

            // Excerpt
            Text(
                text = article.content,
                style = TextStyle(
                    fontFamily = DmSans,
                    fontSize = 14.sp,
                    color = BodyGrey,
                    lineHeight = 22.sp
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        HorizontalDivider(
            thickness = 0.5.dp,
            color = DividerLight
        )
    }
}

// ── Loading Skeleton ──────────────────────────────────────────────────
@Composable
fun TrendingLoadingSkeleton() {
    val shimmer = Color(0xFFE8E3DC)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Chips
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(4) {
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(26.dp)
                        .background(shimmer, RoundedCornerShape(2.dp))
                )
            }
        }
        // Featured image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(shimmer, RoundedCornerShape(4.dp))
        )
        // Title lines
        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(28.dp)
                .background(shimmer, RoundedCornerShape(4.dp))
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .height(28.dp)
                .background(shimmer, RoundedCornerShape(4.dp))
        )
        Spacer(Modifier.height(8.dp))
        // Section header
        Box(
            modifier = Modifier
                .width(160.dp)
                .height(26.dp)
                .background(shimmer, RoundedCornerShape(4.dp))
        )
        // List items
        repeat(3) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .height(50.dp)
                        .background(shimmer, RoundedCornerShape(4.dp))
                )
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .height(10.dp)
                            .background(shimmer, RoundedCornerShape(4.dp))
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp)
                            .background(shimmer, RoundedCornerShape(4.dp))
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(16.dp)
                            .background(shimmer, RoundedCornerShape(4.dp))
                    )
                }
            }
            HorizontalDivider(thickness = 0.5.dp, color = DividerLight)
        }
    }
}

// ── Error State ───────────────────────────────────────────────────────
@Composable
fun TrendingErrorState(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream),
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

@Composable
fun BottomNavBar(
    selectedRoute: String,
    onItemSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Cream)
            .navigationBarsPadding()
            .height(56.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        bottomNavItems.forEach { item ->
            val isSelected = item.route == selectedRoute
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onItemSelected(item.route) },
                contentAlignment = Alignment.Center
            ) {
                NavIcon(route = item.route, isSelected = isSelected)
            }
        }
    }
}

@Composable
fun NavIcon(route: String, isSelected: Boolean) {
    val color = if (isSelected) BrieflyRed else LightMuted.copy(alpha = 0.6f)
    val icon = when (route) {
        "home" -> Icons.Outlined.Home
        "search" -> Icons.Outlined.Search
        "saved" -> Icons.Outlined.BookmarkBorder
        "trending" -> Icons.AutoMirrored.Outlined.TrendingUp
        "profile" -> Icons.Outlined.PersonOutline
        else -> Icons.Outlined.Home
    }
    Icon(
        imageVector = icon,
        contentDescription = route,
        tint = color,
        modifier = Modifier.size(18.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun TrendingScreenPreview(){
    TrendingScreen(
        onNavigate = {},
        onArticleClick = {}
    )
}