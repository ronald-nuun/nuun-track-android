package com.nuun.track.ui.components.button

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp


@Composable
fun DashedBorderImageVectorButton(
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    val strokeWidth = 2.dp
    val dashWidthDp = 9.dp
    val dashGapDp = 9.dp
    val cornerRadius = 12.dp

    val density = LocalDensity.current
    val dashWidthPx = with(density) { dashWidthDp.toPx() }
    val dashGapPx = with(density) { dashGapDp.toPx() }
    val strokeWidthPx = with(density) { strokeWidth.toPx() }
    val cornerRadiusPx = with(density) { cornerRadius.toPx() }

    Surface(
        onClick = onClick,
        color = Color.Transparent,
        shape = RoundedCornerShape(cornerRadius),
        modifier = Modifier
            .size(90.dp)
            .padding(2.dp)
            .drawBehind {
                val stroke = Stroke(
                    width = strokeWidthPx,
                    pathEffect = PathEffect.dashPathEffect(
                        floatArrayOf(dashWidthPx, dashGapPx),
                        0f
                    )
                )

                val rect = size.toRect().deflate(stroke.width / 2)
                val path = Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = rect,
                            cornerRadius = CornerRadius(cornerRadiusPx)
                        )
                    )
                }

                drawPath(
                    path = path,
                    color = Color.Gray,
                    style = stroke
                )
            }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription,
                tint = Color.Gray
            )
        }
    }
}