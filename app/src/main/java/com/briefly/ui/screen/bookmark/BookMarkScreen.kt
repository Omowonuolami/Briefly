package com.briefly.ui.screen.bookmark

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Share
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
import coil.compose.AsyncImage
import com.briefly.core.viewmodels.SharedViewModel
import com.briefly.domain.model.Article
import com.briefly.ui.screen.home.BottomNavBar
import com.briefly.ui.screen.home.TopBar
import com.briefly.ui.theme.BrieflyRed
import com.briefly.ui.theme.Cream
import com.briefly.ui.theme.DmSans
import com.briefly.ui.theme.LightBodyText
import com.briefly.ui.theme.LightDivider
import com.briefly.ui.theme.LightInk
import com.briefly.ui.theme.LightMuted
import com.briefly.ui.theme.LightSurface
import com.briefly.ui.theme.newsReader

@Composable
fun BookMarkScreen(
    onArticleClick: (Article) -> Unit,
    onNavigate: (String) -> Unit,
    onUnSaveClick: () -> Unit,
    onShareClick: () -> Unit,
    sharedViewModel: SharedViewModel = hiltViewModel()
) {
    var selectedNav by remember { mutableStateOf("saved") }
    val bookmarkedArticles by sharedViewModel.bookmarkedArticles.collectAsStateWithLifecycle()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
    ) {

        // ── TOP BAR ──
        TopBar(onHamburgerClick = {})

        HorizontalDivider(thickness = 0.5.dp, color = LightDivider)

        // ── CONTENT ──
        Box(modifier = Modifier.weight(1f)) {
            if (bookmarkedArticles.isEmpty()) {
                EmptyBookmarkState()
            } else {
                SavedContent(
                    articles = bookmarkedArticles, // replace with saved later
                    onArticleClick = onArticleClick,
                    onShareClick = onShareClick,
                    onUnSaveClick = { article ->
                        sharedViewModel.toggleBookmark(article)
                    }
                )
            }
        }

        HorizontalDivider(thickness = 0.5.dp, color = LightDivider)

        // ── BOTTOM NAV ──
        BottomNavBar(
            selectedRoute = selectedNav,
            onItemSelected = {
                selectedNav = it
                onNavigate(it)
            }
        )
    }
}

@Composable
fun EmptyBookmarkState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.BookmarkBorder,
            contentDescription = null,
            tint = LightMuted,
            modifier = Modifier.size(48.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "No saved stories yet.",
            style = TextStyle(
                fontFamily = newsReader,
                fontSize = 22.sp,
                color = LightInk
            )
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Bookmark articles to read them later.",
            style = TextStyle(
                fontFamily = DmSans,
                fontSize = 14.sp,
                color = LightMuted
            )
        )
    }
}

@Composable
fun SavedContent(
    articles: List<Article>,
    onArticleClick: (Article) -> Unit,
    onUnSaveClick: (Article) -> Unit,
    onShareClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {

        item {
            Text(
                text = "Saved Stories",
                style = TextStyle(
                    fontFamily = newsReader,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp,
                    color = BrieflyRed
                ),
                modifier = Modifier.padding(20.dp)
            )
        }

        items(articles) { article ->
            SavedArticleCard(
                article = article,
                onClick = { onArticleClick(article) },
                onShareClick = onShareClick,
                onUnsave = {onUnSaveClick(article)}
            )

            HorizontalDivider(
                thickness = 0.5.dp,
                color = LightDivider,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }

        item {
            Spacer(Modifier.height(24.dp))
            WeeklyBriefingCard()
        }
    }
}

@Composable
fun SavedArticleCard(
    article: Article,
    onClick: () -> Unit,
    onUnsave: () -> Unit,
    onShareClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .clickable { onClick() }
    ) {

        AsyncImage(
            model = article.imageUrl,
            contentDescription = article.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = article.category.uppercase(),
                style = TextStyle(
                    fontFamily = DmSans,
                    fontSize = 9.sp,
                    letterSpacing = 0.16.em,
                    color = BrieflyRed
                )
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = article.title,
                style = TextStyle(
                    fontFamily = newsReader,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = LightInk
                )
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = article.content,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    fontFamily = DmSans,
                    fontSize = 14.sp,
                    color = LightBodyText
                )
            )

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // "■ SAVED" label
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onUnsave() }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Bookmark,
                        contentDescription = "Saved",
                        tint = BrieflyRed,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "SAVED",
                        style = TextStyle(
                            fontFamily = DmSans,
                            fontSize = 9.sp,
                            letterSpacing = 0.16.em,
                            color = BrieflyRed,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                // Share icon
                Icon(
                    imageVector = Icons.Outlined.Share,
                    contentDescription = "Share",
                    tint = LightMuted,
                    modifier = Modifier
                        .size(18.dp)
                        .clickable { onShareClick()}
                )
            }

        }
    }
}

@Composable
fun WeeklyBriefingCard() {
    Column(
        modifier = Modifier
            .padding(20.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(LightSurface)
            .padding(20.dp)
    ) {
        Text(
            text = "Weekly Briefing",
            style = TextStyle(
                fontFamily = newsReader,
                fontStyle = FontStyle.Italic,
                fontSize = 18.sp,
                color = LightInk
            )
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "The essential stories, curated for the intellectual reader.",
            style = TextStyle(
                fontFamily = DmSans,
                fontSize = 14.sp,
                color = LightMuted
            )
        )

        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .background(Color.Black, RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "SUBSCRIBE",
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
fun BookMarkScreenPreview() {
    BookMarkScreen(
        onNavigate = {},
        onArticleClick = {},
        onShareClick = {},
        onUnSaveClick = {}
    )
}