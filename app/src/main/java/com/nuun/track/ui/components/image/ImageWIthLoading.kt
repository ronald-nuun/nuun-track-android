package com.nuun.track.ui.components.image

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter

@Composable
fun ImageWithLoading(
    uri: Uri,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 10.dp,
    imageSize: Dp = 90.dp,
) {
    val painter = rememberAsyncImagePainter(uri)
    val painterState = painter.state

    Box(modifier = modifier.size(imageSize)) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.size(imageSize).clip(RoundedCornerShape(cornerRadius)),
            contentScale = ContentScale.Crop
        )

        if (painterState is AsyncImagePainter.State.Loading) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier
                    .size(imageSize)
                    .align(Alignment.Center)
            )
        }
    }
}