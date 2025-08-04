package com.nuun.track.utility.extensions

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nuun.track.ui.theme.ColorContainer

fun Modifier.shimmerOverlay(
    isShimmering: Boolean = true,
    cornerRadius: Dp = 12.dp
): Modifier = composed {
    if (!isShimmering) return@composed this

    val shimmerColors = listOf(
        ColorContainer.copy(alpha = 0.9f),
        ColorContainer.copy(alpha = 0.5f),
        ColorContainer.copy(alpha = 0.9f)
    )

    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = -300f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim.value, 0f),
        end = Offset(translateAnim.value + 300f, 0f)
    )

    val cornerRadiusPx = with(LocalDensity.current) { cornerRadius.toPx() }

    this.drawWithContent {
        drawContent() // Draw the original content
        drawRoundRect(
            brush = brush,
            cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
        )// Overlay the shimmer effect
    }
}