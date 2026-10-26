package com.briefly.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.briefly.R

val newsReader = FontFamily(
    Font(R.font.newsreader_extralight_italic,
        FontWeight.ExtraBold, FontStyle.Italic),
    Font(R.font.newsreader_regular,
        FontWeight.Normal,
        FontStyle.Normal
    ),


)
val DmSans = FontFamily(
    Font(R.font.dm_sans_regular, FontWeight.Normal),
    Font(R.font.dm_sans_italic, FontWeight.Normal, FontStyle.Italic)
)