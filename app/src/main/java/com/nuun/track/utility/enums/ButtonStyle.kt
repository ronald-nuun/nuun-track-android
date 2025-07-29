package com.nuun.track.utility.enums

import androidx.compose.ui.graphics.Color
import com.nuun.track.ui.theme.ColorBtnBgOutline
import com.nuun.track.ui.theme.ColorBtnBgPrimary
import com.nuun.track.ui.theme.ColorBtnBgSecondary
import com.nuun.track.ui.theme.ColorBtnBgTertiary
import com.nuun.track.ui.theme.ColorBtnContentOutline
import com.nuun.track.ui.theme.ColorBtnContentPrimary
import com.nuun.track.ui.theme.ColorBtnContentSecondary
import com.nuun.track.ui.theme.ColorBtnContentTertiary
import com.nuun.track.ui.theme.ColorBtnTextOutline
import com.nuun.track.ui.theme.ColorBtnTextPrimary
import com.nuun.track.ui.theme.ColorBtnTextSecondary
import com.nuun.track.ui.theme.ColorBtnTextTertiary

enum class ButtonStyle(
    val textColor: Color,
    val contentColor: Color,
    val containerColor: Color
) {
    PRIMARY(
        ColorBtnTextPrimary,
        ColorBtnContentPrimary,
        ColorBtnBgPrimary
    ),
    SECONDARY(
        ColorBtnTextSecondary,
        ColorBtnContentSecondary,
        ColorBtnBgSecondary
    ),
    TERTIARY(
        ColorBtnTextTertiary,
        ColorBtnContentTertiary,
        ColorBtnBgTertiary
    ),
    OUTLINED(
        ColorBtnTextOutline,
        ColorBtnContentOutline,
        ColorBtnBgOutline
    )
}