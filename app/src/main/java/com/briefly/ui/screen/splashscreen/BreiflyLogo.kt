package com.briefly.ui.screen.splashscreen


import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp

@Composable
fun NewspaperLogo(
    modifier: Modifier = Modifier,
    size: Dp = 96.dp,
    isAnimating: Boolean = true,
    onAnimationComplete: () -> Unit = {}
) {
    // Controls the "unfold" — goes from 0f (flat) to 1f (fully open)
    val unfoldProgress = remember { Animatable(0f) }

    LaunchedEffect(isAnimating) {
        if (isAnimating) {
            unfoldProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 600,
                    easing = FastOutSlowInEasing
                )
            )
            onAnimationComplete()
        }
    }

    val progress = unfoldProgress.value

    Canvas(modifier = modifier) {
        val canvasSize = this.size.minDimension
        val unit = canvasSize / 10f   // base unit for proportions

        drawNewspaper(
            progress = progress,
            unit = unit,
            canvasSize = canvasSize
        )
    }
}

private fun DrawScope.drawNewspaper(
    progress: Float,
    unit: Float,
    canvasSize: Float
) {
    val white = Color(0xFFF5F0E8)
    val offWhite = Color(0xFFDDD8CE)
    val lineColor = Color(0xFFB0AA9E)
    val accentRed = Color(0xFFE8262D)
    val center = Offset(canvasSize / 2f, canvasSize / 2f)

    // ── BACK PAGE (rotates in from flat) ─────────────────────────
    // Starts at -80 degrees (folded), ends at -8 degrees (slightly behind)
    val backPageAngle = -80f + (72f * progress)

    rotate(degrees = backPageAngle, pivot = center) {
        drawRoundRect(
            color = offWhite,
            topLeft = Offset(center.x - unit * 3.2f, center.y - unit * 4f),
            size = Size(unit * 6.4f, unit * 8f),
            cornerRadius = CornerRadius(unit * 0.5f),
        )
    }

    // ── FRONT PAGE (scales up from flat) ─────────────────────────
    // Starts squished (scaleX 0.1), expands to full width
    val frontScaleX = 0.1f + (0.9f * progress)

    val frontLeft = center.x - (unit * 3f * frontScaleX)
    val frontWidth = unit * 6f * frontScaleX
    val frontTop = center.y - unit * 4.2f
    val frontHeight = unit * 8.4f

    drawRoundRect(
        color = white,
        topLeft = Offset(frontLeft, frontTop),
        size = Size(frontWidth, frontHeight),
        cornerRadius = CornerRadius(unit * 0.5f)
    )

    // Only draw detail lines once mostly unfolded
    if (progress > 0.6f) {
        val detailAlpha = ((progress - 0.6f) / 0.4f).coerceIn(0f, 1f)

        // Red header bar at top of newspaper
        drawRoundRect(
            color = accentRed.copy(alpha = detailAlpha),
            topLeft = Offset(frontLeft + unit * 0.4f, frontTop + unit * 0.5f),
            size = Size(frontWidth - unit * 0.8f, unit * 1.2f),
            cornerRadius = CornerRadius(unit * 0.2f)
        )

        // Headline lines
        val lineY1 = frontTop + unit * 2.2f
        val lineY2 = frontTop + unit * 3.0f
        val lineY3 = frontTop + unit * 3.7f

        listOf(
            Pair(unit * 0.9f, frontWidth - unit * 0.6f),  // full width line
            Pair(unit * 0.9f, frontWidth - unit * 1.8f),  // slightly shorter
            Pair(unit * 0.9f, frontWidth - unit * 2.4f),  // shorter still
        ).forEachIndexed { index, (startX, endX) ->
            drawLine(
                color = lineColor.copy(alpha = detailAlpha * 0.8f),
                start = Offset(frontLeft + startX, lineY1 + (index * unit * 0.8f)),
                end = Offset(frontLeft + endX, lineY1 + (index * unit * 0.8f)),
                strokeWidth = unit * 0.25f
            )
        }

        // Body text lines (smaller)
        val bodyStartY = frontTop + unit * 4.6f
        repeat(4) { i ->
            val lineWidth = if (i == 3) frontWidth * 0.55f else frontWidth - unit * 0.8f
            drawLine(
                color = lineColor.copy(alpha = detailAlpha * 0.5f),
                start = Offset(frontLeft + unit * 0.5f, bodyStartY + i * unit * 0.7f),
                end = Offset(frontLeft + unit * 0.5f + lineWidth - unit * 0.8f, bodyStartY + i * unit * 0.7f),
                strokeWidth = unit * 0.18f
            )
        }
    }
}

@Preview
@Composable
fun LogoPreview(){
    NewspaperLogo()
}