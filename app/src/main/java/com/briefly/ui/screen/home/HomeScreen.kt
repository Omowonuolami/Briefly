package com.briefly.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.briefly.ui.theme.BrieflyRed
import com.briefly.ui.theme.Cream
import com.briefly.ui.theme.DmSans
import com.briefly.ui.theme.LightBodyText
import com.briefly.ui.theme.LightBorder
import com.briefly.ui.theme.LightCategoryBg
import com.briefly.ui.theme.LightDivider
import com.briefly.ui.theme.LightDrawerBg
import com.briefly.ui.theme.LightInk
import com.briefly.ui.theme.LightMuted
import com.briefly.ui.theme.LightSurface
import com.briefly.ui.theme.newsReader


@Composable
fun HomeScreen(
    onArticleClick: (Article) -> Unit,
    onNavigate: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDrawer by remember { mutableStateOf(false) }
    var selectedNav by remember { mutableStateOf("home") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── TOP BAR ──────────────────────────────────────────────
            TopBar(onHamburgerClick = { showDrawer = true })

            HorizontalDivider(thickness = 0.5.dp, color = LightDivider)

            // ── FEED CONTENT ─────────────────────────────────────────
            Box(modifier = Modifier.weight(1f)) {
                when {
                    uiState.isLoading          -> LoadingFeed()
                    uiState.errorMessage != null -> ErrorState(
                        message = uiState.errorMessage!!,
                        onRetry = viewModel::onRetry
                    )
                    uiState.articles.isEmpty() -> EmptyState()
                    else -> ArticleFeed(
                        articles = uiState.articles,
                        onArticleClick = {article->
                            onArticleClick(article)
                        }
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

        // ── CATEGORIES DRAWER ────────────────────────────────────────
        if (showDrawer) {
            CategoriesDrawer(
                selectedCategory = uiState.selectedCategory,
                onCategorySelected = { category ->
                    viewModel.onCategorySelected(category)
                    showDrawer = false
                },
                onDismiss = { showDrawer = false }
            )
        }
    }
}

// ── Top Bar ───────────────────────────────────────────────────────────
@Composable
fun TopBar(onHamburgerClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Cream)
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        // Hamburger — left
        IconButton(
            onClick = onHamburgerClick,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(36.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(22.dp)
                        .height(1.5.dp)
                        .background(LightInk)
                )
                Box(
                    modifier = Modifier
                        .width(15.dp)        // shorter middle line — editorial detail
                        .height(1.5.dp)
                        .background(LightInk)
                )
                Box(
                    modifier = Modifier
                        .width(22.dp)
                        .height(1.5.dp)
                        .background(LightInk)
                )
            }
        }

        // "Briefly" — centered
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

        // Profile avatar — right
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


// ── Article Feed ──────────────────────────────────────────────────────
@Composable
fun ArticleFeed(
    articles: List<Article>,
    onArticleClick: (Article) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        items(
            items = articles,
            key   = { it.id }
        ) { article ->
            ArticleCard(
                article = article,
                onClick = { onArticleClick(article) }
            )
            HorizontalDivider(
                thickness = 0.5.dp,
                color     = LightDivider,
                modifier  = Modifier.padding(horizontal = 20.dp)
            )
        }

        // Editorial quote card after the list
        item { EditorialQuoteCard() }
    }
}

// ── Article Card ──────────────────────────────────────────────────────
@Composable
fun ArticleCard(
    article: Article,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .background(Cream)
            .clickable { onClick() }
            .padding(bottom = 8.dp)
    ) {
        // Full-width hero image
        AsyncImage(
            model              = article.imageUrl,
            contentDescription = article.title,
            modifier           = Modifier
                .fillMaxWidth()
                .height(220.dp),
            contentScale       = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            // Category pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(2.dp))
                    .background(LightCategoryBg)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text  = article.category.uppercase(),
                    style = TextStyle(
                        fontFamily    = DmSans,
                        fontSize      = 9.sp,
                        letterSpacing = 0.16.em,
                        color         = LightMuted
                    )
                )
            }

            Spacer(Modifier.height(10.dp))

            // Title
            Text(
                text  = article.title,
                style = TextStyle(
                    fontFamily = newsReader,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 24.sp,
                    color      = LightInk,
                    lineHeight = 30.sp
                )
            )

            Spacer(Modifier.height(8.dp))

            // Excerpt — 2 lines max
            Text(
                text      = article.description?: "",
                style     = TextStyle(
                    fontFamily = DmSans,
                    fontSize   = 14.sp,
                    color      = LightBodyText,
                    lineHeight = 22.sp
                ),
//                maxLines  = 3,
                overflow  = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(10.dp))

            // Author · read time
            Row(
                verticalAlignment   = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text  = "BY ${article.author.uppercase()}",
                    style = TextStyle(
                        fontFamily    = DmSans,
                        fontSize      = 9.sp,
                        letterSpacing = 0.12.em,
                        color         = LightMuted
                    )
                )
                Box(
                    modifier = Modifier
                        .size(3.dp)
                        .background(LightMuted.copy(alpha = 0.5f), RoundedCornerShape(50))
                )
                Text(
                    text  = article.readTime.uppercase(),
                    style = TextStyle(
                        fontFamily    = DmSans,
                        fontSize      = 9.sp,
                        letterSpacing = 0.12.em,
                        color         = LightMuted
                    )
                )
            }
        }
    }
}

// ── Editorial Quote Card ──────────────────────────────────────────────
@Composable
fun EditorialQuoteCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 24.dp)
            .border(
                width  = 1.dp,
                color  = LightBorder,
                shape  = RoundedCornerShape(8.dp)
            )
            .background(LightSurface, RoundedCornerShape(8.dp))
            .padding(24.dp)
    ) {
        Text(
            text  = "THE WEEKEND EDITION",
            style = TextStyle(
                fontFamily    = DmSans,
                fontSize      = 9.sp,
                letterSpacing = 0.2.em,
                color         = BrieflyRed
            )
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text  = "\"Journalism is the first rough draft of history. Briefly is the curated final edit.\"",
            style = TextStyle(
                fontFamily = newsReader,
                fontStyle  = FontStyle.Italic,
                fontSize   = 20.sp,
                color      = LightInk,
                lineHeight = 30.sp
            )
        )
    }
}

// ── Categories Drawer ─────────────────────────────────────────────────
@Composable
fun CategoriesDrawer(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        // Scrim
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LightInk.copy(alpha = 0.35f))
                .clickable { onDismiss() }
        )

        // Drawer panel
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(280.dp)
                .align(Alignment.CenterStart)
                .background(LightDrawerBg)
                .statusBarsPadding()
                .padding(vertical = 24.dp)
        ) {
            // Header label
            Text(
                text  = "Sections",
                style = TextStyle(
                    fontFamily    = DmSans,
                    fontSize      = 11.sp,
                    letterSpacing = 0.18.em,
                    color         = LightMuted
                ),
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(Modifier.height(20.dp))
            HorizontalDivider(thickness = 0.5.dp, color = LightDivider)
            Spacer(Modifier.height(8.dp))

            categories.forEach { category ->
                val isSelected = category == selectedCategory
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCategorySelected(category) }
                        .padding(horizontal = 24.dp, vertical = 14.dp),
                    verticalAlignment   = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Active indicator dot
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(
                                if (isSelected) BrieflyRed else Color.Transparent,
                                RoundedCornerShape(50)
                            )
                            .then(
                                if (!isSelected) Modifier.border(
                                    0.5.dp,
                                    LightMuted.copy(alpha = 0.4f),
                                    RoundedCornerShape(50)
                                ) else Modifier
                            )
                    )
                    Text(
                        text  = category,
                        style = TextStyle(
                            fontFamily = newsReader,
                            fontSize   = 18.sp,
                            color      = if (isSelected) LightInk else LightBodyText,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    )
                }
            }
        }
    }
}

// ── Bottom Nav Bar ────────────────────────────────────────────────────
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
    val icon  = when (route) {
        "home"     -> Icons.Outlined.Home
        "search"   -> Icons.Outlined.Search
        "saved"    -> Icons.Outlined.BookmarkBorder
        "trending" -> Icons.AutoMirrored.Outlined.TrendingUp
        "profile"  -> Icons.Outlined.PersonOutline
        else       -> Icons.Outlined.Home
    }
    Icon(
        imageVector    = icon,
        contentDescription = route,
        tint           = color,
        modifier       = Modifier.size(18.dp)
    )
}

// ── Loading skeleton ──────────────────────────────────────────────────
@Composable
fun LoadingFeed() {
    val shimmer = Color(0xFFE8E2D8)  // light warm shimmer tone
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(3) {
            Column(modifier = Modifier.padding(bottom = 8.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .background(shimmer)
                )
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)) {
                    Box(
                        modifier = Modifier
                            .width(80.dp).height(20.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(shimmer)
                    )
                    Spacer(Modifier.height(10.dp))
                    Box(Modifier.fillMaxWidth(0.85f).height(26.dp).background(shimmer))
                    Spacer(Modifier.height(6.dp))
                    Box(Modifier.fillMaxWidth(0.6f).height(26.dp).background(shimmer))
                    Spacer(Modifier.height(10.dp))
                    Box(Modifier.fillMaxWidth().height(14.dp).background(shimmer))
                    Spacer(Modifier.height(5.dp))
                    Box(Modifier.fillMaxWidth(0.7f).height(14.dp).background(shimmer))
                }
            }
            HorizontalDivider(thickness = 0.5.dp, color = LightDivider, modifier = Modifier.padding(horizontal = 20.dp))
        }
    }
}

// ── Error state ───────────────────────────────────────────────────────
@Composable
fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(
        modifier              = Modifier.fillMaxSize(),
        horizontalAlignment   = Alignment.CenterHorizontally,
        verticalArrangement   = Arrangement.Center
    ) {
        Text(
            text  = message,
            style = TextStyle(
                fontFamily = DmSans,
                fontSize   = 14.sp,
                color      = LightBodyText,
                textAlign  = TextAlign.Center
            ),
            modifier = Modifier.padding(horizontal = 32.dp)
        )
        Spacer(Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(BrieflyRed)
                .clickable { onRetry() }
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text(
                text  = "TRY AGAIN",
                style = TextStyle(
                    fontFamily    = DmSans,
                    fontSize      = 11.sp,
                    letterSpacing = 0.16.em,
                    color         = Color.White
                )
            )
        }
    }
}

// ── Empty state ───────────────────────────────────────────────────────
@Composable
fun EmptyState() {
    Column(
        modifier              = Modifier.fillMaxSize(),
        horizontalAlignment   = Alignment.CenterHorizontally,
        verticalArrangement   = Arrangement.Center
    ) {
        Text(
            text  = "No stories yet.",
            style = TextStyle(
                fontFamily = newsReader,
                fontSize   = 24.sp,
                color      = LightInk
            )
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text  = "Check back soon.",
            style = TextStyle(
                fontFamily = DmSans,
                fontSize   = 14.sp,
                color      = LightBodyText
            )
        )
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        onArticleClick = {},
        onNavigate     = {}
    )
}