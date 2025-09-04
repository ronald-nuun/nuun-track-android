package com.nuun.track.ui.components.media

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nuun.track.R
import com.nuun.track.ui.screens.reservation.detail.SetupMediaCaption

@Composable
fun MediaPreviewWithRemove(
    modifier: Modifier = Modifier.size(90.dp),
    uri: Uri,
    onRemove: (Uri) -> Unit,
    label: String?
) {
    Column {
        Box(modifier = modifier) {
            MediaPreview(
                modifier = Modifier.matchParentSize(),
                uri = uri,
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(20.dp)
                    .background(Color.Red.copy(alpha = 0.7f), shape = CircleShape)
                    .clickable { onRemove(uri) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.desc_remove),
                    tint = Color.White,
                    modifier = Modifier.size(15.dp)
                )
            }
        }

        label?.let { SetupMediaCaption(it, 10.dp) }
    }
}