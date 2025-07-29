package com.nuun.track.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nuun.track.R

@Composable
fun BackgroundBox(
    modifier: Modifier = Modifier,
    imageRes: Int? = R.drawable.background,
    shapeColor: Color = Color.Transparent,
    useShape: Boolean = false,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .then(
                if (useShape) Modifier
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(shapeColor)
                else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        // Background Image
        if (!useShape && imageRes != null) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = stringResource(R.string.desc_image),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Content
        content()
    }
}

@Preview
@Composable
fun PreviewBackgroundShape() {
    Scaffold(
        containerColor = Color.Red,
    ) { paddingValues ->
        BackgroundBox(
            modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
            shapeColor = Color.Gray,
            useShape = true,
            content = {

            }
        )
    }
}

@Preview
@Composable
fun PreviewBackgroundImage() {
    Scaffold(
        containerColor = Color.Red,
    ) { paddingValues ->
        BackgroundBox(
            modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
            content = {

            }
        )
    }
}
