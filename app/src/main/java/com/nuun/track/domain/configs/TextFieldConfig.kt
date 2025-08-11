package com.nuun.track.domain.configs

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nuun.track.ui.theme.ColorInputDefault
import com.nuun.track.ui.theme.ColorTextDefault

data class TextFieldConfig(
    val state: MutableState<TextFieldValue>? = null,
    val leadingIconRes: Int? = null,
    val trailingIconRes: Int? = null,
    val errorMessage: String? = null,
    val isError: Boolean = false,
    val keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    val isEnabled: Boolean = true,
    val textPlaceholder: String? = null,
    val textPlaceholderColor: Color? = null,
    val isRequired: Boolean = false,
    val validate: (String) -> String? = { null },
    val autoShowClear: Boolean = false,
    val lineCount: Int = 1,
    val backgroundColor: Color = ColorInputDefault,
    val textColor: Color = ColorTextDefault,
    val cursorColor: Color = ColorTextDefault,
    val roundedSize: Dp = 50.dp,
    val keyboardActions: KeyboardActions = KeyboardActions.Default,
)