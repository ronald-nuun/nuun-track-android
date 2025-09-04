package com.nuun.track.ui.components.media

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuun.track.R
import com.nuun.track.ui.components.image.ImageWithLoading
import com.nuun.track.ui.screens.reservation.detail.SetupMediaCaption
import com.nuun.track.utility.enums.FileTypes
import com.nuun.track.utility.extensions.getMimeType
import com.nuun.track.utility.extensions.getVideoThumbnailFromUri


@Composable
fun MediaPreview(
    modifier: Modifier? = null,
    uri: Uri,
    type: FileTypes? = null,
    label: String?
) {
    val context = LocalContext.current
    val mimeType = remember(uri) { context.getMimeType(uri) }
    val cornerRadius = 10.dp
    val newModifier = modifier ?: Modifier.size(90.dp)

    when {
        type == FileTypes.PHOTO || mimeType?.startsWith("image") == true -> {
            Column {
                ImageWithLoading(
                    uri = uri,
                    cornerRadius = cornerRadius,
                )

                label?.let {
                    SetupMediaCaption(it, cornerRadius)
                }
            }
        }

        type == FileTypes.VIDEO -> {
            Column {
                Box(
                    modifier = newModifier
                        .clip(
                            RoundedCornerShape(
                                topStart = cornerRadius,
                                topEnd = cornerRadius,
                                bottomEnd = 0.dp,
                                bottomStart = 0.dp
                            )
                        )
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(R.string.label_video), fontSize = 10.sp)
                }

                label?.let {
                    SetupMediaCaption(it, cornerRadius)
                }
            }
        }

        mimeType?.startsWith("video") == true -> {
            val thumbnail = remember(uri) { context.getVideoThumbnailFromUri(uri) }
            if (thumbnail != null) {
                Column {
                    ImageWithLoading(
                        uri = uri,
                        cornerRadius = cornerRadius,
                    )

                    label?.let {
                        SetupMediaCaption(it, cornerRadius)
                    }
                }
            } else {
                Column {
                    Box(
                        modifier = newModifier
                            .clip(RoundedCornerShape(cornerRadius))
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(R.string.label_no_thumbnail), fontSize = 10.sp)
                    }

                    label?.let {
                        SetupMediaCaption(it, cornerRadius)
                    }
                }
            }
        }

        else -> {
            Column {
                Box(
                    modifier = newModifier
                        .clip(RoundedCornerShape(cornerRadius))
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(R.string.label_unsupported_file), fontSize = 10.sp)
                }
                label?.let {
                    SetupMediaCaption(it, cornerRadius)
                }
            }
        }
    }
}