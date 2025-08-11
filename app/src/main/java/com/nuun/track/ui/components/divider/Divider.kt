package com.nuun.track.ui.components.divider

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nuun.track.ui.theme.ColorDisabled

@Composable
fun Divider(
    fullDivider: Boolean = true,
    useIndentation: Boolean = true,
    dividerColor: Color = ColorDisabled,
) {
    val modifier = Modifier
    HorizontalDivider(
        modifier = Modifier
            .then(
                when {
                    fullDivider -> modifier.fillMaxWidth()
                    !useIndentation -> modifier.padding(start = 28.dp)
                    else -> modifier.padding(start = 52.dp)
                }
            ),
        thickness = 1.dp,
        color = dividerColor
    )
}