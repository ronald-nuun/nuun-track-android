package com.nuun.track.domain.configs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nuun.track.ui.theme.ColorBgIcon
import com.nuun.track.ui.theme.ColorDisabled
import com.nuun.track.ui.theme.ColorTextDefault

data class TextIconConfig(
    val title: String,
    val desc: String? = null,
    val message: String? = null,
    val vector: ImageVector? = null,
    val drawableRes: Int? = null,
    val iconSize: Dp = 18.dp,
    val drawableTint: Color = ColorTextDefault,
    val backgroundColor: Color = ColorBgIcon,
    val dividerColor: Color = ColorDisabled,
    val showTrailingIcon: Boolean = false,
    val useTrailingVector: Boolean = true,
    val trailingVectorOrXml: ImageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
    val trailingRes: Int = 0,
    val trailingSize: Dp = 18.dp,
    val useIndentation: Boolean = true, // Controls indentation & empty icon space
    val showDivider: Boolean = false,
    val fullDivider: Boolean = true,
    val showBackgroundShape: Boolean = false,
    val enableSwipeDelete: Boolean = false,
    val useEllipsis: Boolean = false
)