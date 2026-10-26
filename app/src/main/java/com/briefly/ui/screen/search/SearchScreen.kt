package com.briefly.ui.screen.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.briefly.core.viewmodels.SharedViewModel
import com.briefly.domain.model.Article
import com.briefly.ui.screen.home.TopBar
import com.briefly.ui.theme.BrieflyRed
import com.briefly.ui.theme.Cream
import com.briefly.ui.theme.DmSans
import com.briefly.ui.theme.LightBodyText
import com.briefly.ui.theme.LightBorder
import com.briefly.ui.theme.LightDivider
import com.briefly.ui.theme.LightFieldBg
import com.briefly.ui.theme.LightInk
import com.briefly.ui.theme.LightMuted
import com.briefly.ui.theme.LightPill
import com.briefly.ui.theme.newsReader

@Composable
fun SearchScreen(
    onArticleClick: (Article) -> Unit,
    onNavigate: (String) -> Unit,
    selectedNav: String = "search",
    viewModel: SearchViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel = hiltViewModel()

){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val allArticles by sharedViewModel.allArticles.collectAsStateWithLifecycle()
    val focusManager  = LocalFocusManager.current

    // Sync articles from SharedViewModel
    LaunchedEffect(allArticles) {
        if (allArticles.isNotEmpty()) {
            viewModel.setArticles(allArticles)
        }
    }

//    val searchResults = remember(query) {
//        if (query.isBlank()) emptyList()
//        else recommendedArticles.filter {
//            it.title.contains(query, ignoreCase = true)   ||
//                    it.content.contains(query, ignoreCase = true) ||
//                    it.category.contains(query, ignoreCase = true)
//        }
//    }

    // Recommended = shuffle all articles, take 5
    val recommended = allArticles.shuffled().take(5)

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Cream)) {
        Column(modifier = Modifier.fillMaxSize()) {

            TopBar(onHamburgerClick = {})
            HorizontalDivider(thickness = 0.5.dp, color = LightDivider)

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item {
                    Text(
                        text = "Search",
                        style = TextStyle(
                            fontFamily = newsReader,
                            fontWeight = FontWeight.Bold,
                            fontStyle  = FontStyle.Italic,
                            fontSize   = 32.sp,
                            color      = LightInk
                        ),
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 16.dp)
                    )
                }

                item {
                    SearchField(
                        query         = uiState.query,
                        isFocused     = false,
                        onQueryChange = {viewModel.onQueryChange(it)},
                        onFocusChange = {},
                        onSearch      = { focusManager.clearFocus() },
                        modifier      = Modifier.padding(horizontal = 20.dp)
                    )
                    Spacer(Modifier.height(24.dp))
                }

                if (uiState.query.isBlank()) {
                    item {
                        SectionHeader(title = "TRENDING TOPICS", actionLabel = "↗", onAction = {})
                        Spacer(Modifier.height(12.dp))
                        TrendingTopicsGrid(
                            topics   = trendingTopics,
                            onClick  = { viewModel.onQueryChange(it) },
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                        Spacer(Modifier.height(28.dp))
                        HorizontalDivider(thickness = 0.5.dp, color = LightDivider,
                            modifier = Modifier.padding(horizontal = 20.dp))
                        Spacer(Modifier.height(24.dp))
                    }

                    item {
                        SectionHeader(title = "RECOMMENDED FOR YOU", actionLabel = null, onAction = {})
                        Spacer(Modifier.height(16.dp))
                    }

                    items(recommended, key = {it.id}) { article ->
                        RecommendedArticleCard(article = article, onClick = { onArticleClick(article) })
                        Spacer(Modifier.height(8.dp))
                    }

                } else {
                    if (uiState.searchResults.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 48.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text  = "No results for \"${uiState.query}\"",
                                        style = TextStyle(fontFamily = newsReader, fontSize = 20.sp, color = LightInk)
                                    )
                                    Spacer(Modifier.height(8.dp))
                                    Text(
                                        text  = "Try a different search term.",
                                        style = TextStyle(fontFamily = DmSans, fontSize = 14.sp, color = LightMuted)
                                    )
                                }
                            }
                        }
                    } else {
                        item {
                            Text(
                                text  = "${uiState.searchResults.size} result${if (uiState.searchResults.size != 1) "s" else ""} for \"${uiState.query}\"",
                                style = TextStyle(fontFamily = DmSans, fontSize = 11.sp, letterSpacing = 0.12.em, color = LightMuted),
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                            )
                            Spacer(Modifier.height(12.dp))
                        }
                        items(uiState.searchResults) { article ->
                            RecommendedArticleCard(article = article, onClick = { onArticleClick(article) })
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }
            }

            HorizontalDivider(thickness = 0.5.dp, color = LightDivider)
            SearchBottomNavBar(selectedRoute = selectedNav, onItemSelected = onNavigate)
        }
    }
}

@Composable
private fun SearchTopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Cream)
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(4.dp)
        ) {
            Box(Modifier
                .width(22.dp)
                .height(1.5.dp)
                .background(LightInk))
            Box(Modifier
                .width(15.dp)
                .height(1.5.dp)
                .background(LightInk))
            Box(Modifier
                .width(22.dp)
                .height(1.5.dp)
                .background(LightInk))
        }
        Text(
            text = "Briefly",
            style = TextStyle(fontFamily = newsReader, fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic, fontSize = 26.sp, color = LightInk),
            modifier = Modifier.align(Alignment.Center)
        )
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(32.dp)
                .border(1.5.dp, LightInk, RoundedCornerShape(50))
                .clip(RoundedCornerShape(50))
                .background(Cream),
            contentAlignment = Alignment.Center
        ) {
            Text("E", style = TextStyle(fontFamily = DmSans, fontSize = 13.sp,
                color = LightInk, fontWeight = FontWeight.Medium))
        }
    }
}

@Composable
private fun SearchField(
    query: String,
    isFocused: Boolean,
    onQueryChange: (String) -> Unit,
    onFocusChange: (Boolean) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = query, onValueChange = onQueryChange, singleLine = true,
        textStyle = TextStyle(fontFamily = DmSans, fontSize = 15.sp, color = LightInk),
        cursorBrush = SolidColor(BrieflyRed),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { onFocusChange(it.isFocused) },
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(LightFieldBg)
                    .border(
                        1.dp,
                        if (isFocused) LightInk.copy(alpha = 0.3f) else LightBorder,
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 14.dp, vertical = 13.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(Icons.Outlined.Search, "Search", tint = LightMuted, modifier = Modifier.size(16.dp))
                Box(modifier = Modifier.weight(1f)) {
                    if (query.isEmpty()) {
                        Text("Explore the day's stories...",
                            style = TextStyle(fontFamily = DmSans, fontSize = 15.sp, color = LightMuted))
                    }
                    innerTextField()
                }
            }
        }
    )
}

@Composable
private fun SectionHeader(title: String, actionLabel: String?, onAction: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = TextStyle(fontFamily = DmSans, fontSize = 10.sp,
            letterSpacing = 0.18.em, fontWeight = FontWeight.Medium, color = LightMuted))
        if (actionLabel != null) {
            Text(actionLabel, style = TextStyle(fontFamily = DmSans, fontSize = 14.sp, color = LightMuted),
                modifier = Modifier.clickable { onAction() })
        }
    }
}

// ── Trending topics — manual 2-column grid, no FlowRow needed ─────────
@Composable
private fun TrendingTopicsGrid(
    topics: List<String>,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val rows = topics.chunked(2)
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        rows.forEach { rowTopics ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                rowTopics.forEach { topic ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(4.dp))
                            .background(LightPill)
                            .border(1.dp, LightBorder, RoundedCornerShape(4.dp))
                            .clickable { onClick(topic) }
                            .padding(horizontal = 14.dp, vertical = 9.dp)
                    ) {
                        Text(topic, style = TextStyle(fontFamily = DmSans, fontSize = 13.sp, color = LightBodyText))
                    }
                }
                if (rowTopics.size < 2) Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun RecommendedArticleCard(article: Article, onClick: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, end = 16.dp)
        .background(Cream)
        .clickable { onClick() }) {
        AsyncImage(
            model = article.imageUrl.ifEmpty {
                "https://picsum.photos/seed/${article.id}/600/400"
            }, contentDescription = article.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp), contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(article.category.uppercase(),
                    style = TextStyle(fontFamily = DmSans, fontSize = 9.sp, letterSpacing = 0.16.em, color = BrieflyRed))
                Box(modifier = Modifier
                    .size(3.dp)
                    .background(LightMuted.copy(alpha = 0.5f), RoundedCornerShape(50)))
                Text(article.readTime.uppercase(),
                    style = TextStyle(fontFamily = DmSans, fontSize = 9.sp, letterSpacing = 0.12.em, color = LightMuted))
            }
            Spacer(Modifier.height(8.dp))
            Text(article.title, style = TextStyle(fontFamily = newsReader, fontWeight = FontWeight.Bold,
                fontSize = 22.sp, color = LightInk, lineHeight = 28.sp))
            Spacer(Modifier.height(6.dp))
            Text(article.content, style = TextStyle(fontFamily = DmSans, fontSize = 14.sp,
                color = LightBodyText, lineHeight = 21.sp), maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
        HorizontalDivider(thickness = 0.5.dp, color = LightDivider, modifier = Modifier.padding(horizontal = 20.dp))
    }
}

@Composable
private fun SearchBottomNavBar(selectedRoute: String, onItemSelected: (String) -> Unit) {
    val items = listOf(
        Triple("home", "HOME", Icons.Outlined.Home),
        Triple("search", "EXPLORE", Icons.Outlined.Search),
        Triple("saved", "SAVED", Icons.Outlined.BookmarkBorder),
        Triple("trending", "FEED", Icons.AutoMirrored.Outlined.TrendingUp),
        Triple("profile", "PROFILE", Icons.Outlined.PersonOutline),
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Cream)
            .navigationBarsPadding()
            .height(56.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { (route, label, icon) ->
            val isSelected = route == selectedRoute
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .clickable { onItemSelected(route) },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(icon, label,
                    tint = if (isSelected) BrieflyRed else LightMuted.copy(alpha = 0.6f),
                    modifier = Modifier.size(18.dp))
//                Spacer(Modifier.height(3.dp))
//                Text(label, style = TextStyle(fontFamily = DmSans, fontSize = 8.sp,
//                    letterSpacing = 0.1.em,
//                    color = if (isSelected) BrieflyRed else LightMuted.copy(alpha = 0.6f)))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    SearchScreen(
        onArticleClick = {},
        onNavigate     = {}
    )
}
