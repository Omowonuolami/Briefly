package com.briefly.ui.screen.splashscreen

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.briefly.ui.theme.BodyDark
import com.briefly.ui.theme.BrieflyRed
import com.briefly.ui.theme.Cream
import com.briefly.ui.theme.DmSans
import com.briefly.ui.theme.ProgressBg
import com.briefly.ui.theme.TaglineGrey
import com.briefly.ui.theme.WarmTan
import com.briefly.ui.theme.newsReader
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ── Colors matching your design exactly ─────────────────────────────
// ── Fonts ────────────────────────────────────────────────────────────
// res/font/newspaper.ttf        ← your newspaper font (the bold italic serif)
// res/font/dm_sans_medium.ttf   ← for spaced caps tagline & metadata

//private val NewspaperFont = FontFamily(
//    Font(R.font.newspaper, FontWeight.Bold, FontStyle.Italic)
//)
//private val DmSansMedium = FontFamily(
//    Font(R.font.dm_sans_medium, FontWeight.Medium)
//)
//// For "News, summarized." — use newspaper font in italic or a separate serif italic
//private val SerifItalic = FontFamily(
//    Font(R.font.newspaper, FontWeight.Normal, FontStyle.Italic)
//)

// ────────────────────────────────────────────────────────────────────

@Composable
fun BrieflySplashScreen(
    onNavigateToAuth: () -> Unit,
    loadData: suspend () -> Unit = { delay(2500L) }
) {
    // Progress bar animatable (0f → 1f)
    val progress = remember { Animatable(0f) }

    // Kick off loading + progress fill simultaneously
    LaunchedEffect(Unit) {
        // Run both in parallel — navigate when BOTH are done
        val progressJob = launch {
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 2500,
                    easing = LinearEasing
                )
            )
        }
        loadData()       // your real loading work
        progressJob.join()
        delay(200L)      // brief pause at full bar
        onNavigateToAuth()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
            .systemBarsPadding()
    ) {

        // ── TOP HALF: Logo block centered in upper 60% ───────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.60f)          // upper 60% of screen
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Short rule line above wordmark
            Box(
                modifier = Modifier
                    .width(36.dp)
                    .height(1.5.dp)
                    .background(WarmTan)
            )

            Spacer(Modifier.height(18.dp))

            // "Briefly" — newspaper font, bold italic, large red
            Text(
                text = "Briefly",
                style = TextStyle(
                    fontFamily = newsReader,
                    fontWeight = FontWeight.ExtraBold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 80.sp,           // large — matches the oversized wordmark
                    color = BrieflyRed,
                    textAlign = TextAlign.Center,
                    lineHeight = 80.sp
                )
            )

            Spacer(Modifier.height(12.dp))

            // "THE DIGITAL CURATOR" — spaced caps
            Text(
                text = "THE DIGITAL CURATOR",
                style = TextStyle(
                    fontFamily = DmSans,
                    fontWeight = FontWeight.Medium,
                    fontSize = 10.sp,
                    letterSpacing = 0.28.em,    // wide tracking matches your design
                    color = TaglineGrey,
                    textAlign = TextAlign.Center
                )
            )
        }

        // ── BOTTOM HALF: tagline + progress + metadata ───────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // "News, summarized." — italic serif
            Text(
                text = "News, summarized.",
                style = TextStyle(
                    fontFamily = newsReader,
                    fontStyle = FontStyle.Italic,
                    fontSize = 22.sp,
                    color = BodyDark,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(Modifier.height(18.dp))

            // Progress bar — short, centered, red fills left to right
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(2.dp)
                    .background(ProgressBg)     // faint cream track
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress.value)  // fills based on progress
                        .background(BrieflyRed)
                )
            }

            Spacer(Modifier.height(28.dp))

            // "ISSUE NO. 001 · EST. 2024"
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ISSUE NO. 001",
                    style = TextStyle(
                        fontFamily = DmSans,
                        fontSize = 9.sp,
                        letterSpacing = 0.18.em,
                        color = TaglineGrey
                    )
                )
                // Center dot
                Box(
                    modifier = Modifier
                        .size(3.dp)
                        .background(
                            color = TaglineGrey,
                            shape = CircleShape
                        )
                )
                Text(
                    text = "EST. 2024",
                    style = TextStyle(
                        fontFamily = DmSans,
                        fontSize = 9.sp,
                        letterSpacing = 0.18.em,
                        color = TaglineGrey
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun SplashScreenPreview(){
    BrieflySplashScreen(
        onNavigateToAuth = {},
        loadData = {}
    )
}