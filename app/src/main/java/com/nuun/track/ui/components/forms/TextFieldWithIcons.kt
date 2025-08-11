package com.nuun.track.ui.components.forms

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nuun.track.R
import com.nuun.track.domain.configs.TextFieldConfig
import com.nuun.track.ui.theme.ColorTextDefault
import com.nuun.track.ui.theme.ColorTextError
import com.nuun.track.ui.theme.ColorTextInput
import com.nuun.track.ui.theme.ColorTextInputDisabled
import com.nuun.track.ui.theme.Neutral100
import com.nuun.track.ui.theme.Neutral1100

@Composable
fun TextFieldWithIcons(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    config: TextFieldConfig,
) {
    val textColor = if (config.isEnabled) {
        config.textColor
    } else {
        config.textPlaceholderColor ?: ColorTextInputDisabled
    }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .background(
                    color = config.backgroundColor,
                    shape = RoundedCornerShape(
                        if (config.lineCount > 1) 5.dp else config.roundedSize
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Leading icon image
                if(config.leadingIconRes != null) CustomIcon(config.leadingIconRes)

                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    label = null,
                    isError = config.isError,
                    leadingIcon = null, // No Compose icon, handled with Image above
                    trailingIcon = null, // No Compose icon, handled with Image below
                    keyboardOptions = if (config.lineCount > 1) KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Default // Enables multi-line input
                    ) else config.keyboardOptions,
                    keyboardActions = config.keyboardActions,
                    singleLine = config.lineCount == 1,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = textColor
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = (config.lineCount * 24).dp)
                        .align(Alignment.CenterVertically),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent,
                        errorBorderColor = Color.Transparent,
                        cursorColor = config.cursorColor,
                        selectionColors = TextSelectionColors(
                            handleColor = config.cursorColor,  // color of the circle handle
                            backgroundColor = config.cursorColor.copy(alpha = 0.4f)  // selection highlight color
                        )
                    ),
                    minLines = 1,
                    maxLines = config.lineCount,
                    enabled = config.isEnabled,
                    placeholder = {
                        PlaceholderText(
                            placeholder = config.textPlaceholder,
                            textColor = ColorTextInput
                        )
                    },
                    visualTransformation = getVisualTransformation(
                        keyboardOptions = config.keyboardOptions,
                        passwordVisible = passwordVisible
                    ),
                )

                // Trailing icon image
                if (config.keyboardOptions.keyboardType == KeyboardType.Password) {
                    val icon =
                        if (passwordVisible) painterResource(R.drawable.ic_password_show) else painterResource(
                            R.drawable.ic_password_hide
                        )
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Image(painter = icon, contentDescription = "Toggle Password Visibility")
                    }
                } else {
                    if (config.autoShowClear && value.isNotEmpty()) {
                        CustomIcon(
                            imageVector = Icons.Default.Clear,
                            onClick = { isClear ->
                                if (isClear) {
                                    onValueChange("")
                                }
                            }
                        )
                    } else {
                        if (config.trailingIconRes != null) CustomIcon(iconRes = config.trailingIconRes)
                    }
                }
            }
        }

        if (config.isError && !config.errorMessage.isNullOrEmpty()) {
            Text(
                text = config.errorMessage,
                color = ColorTextError,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun CustomIcon(
    iconRes: Int? = null,
    imageVector: ImageVector? = null,
    onClick: ((Boolean) -> Unit)? = null
) {
    iconRes?.let {
        Box(
            modifier = Modifier
        ) {
            Image(
                painter = painterResource(id = it),
                contentDescription = stringResource(R.string.desc_image),
                modifier = Modifier.size(20.dp)
            )
        }
    }

    imageVector?.let {
        Box(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .padding(end = 8.dp)
                .then(Modifier.background(ColorTextDefault, RoundedCornerShape(20.dp)))
                .clickable {
                    if (onClick != null) {
                        onClick(true)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = it,
                contentDescription = stringResource(R.string.desc_image),
                modifier = Modifier.size(15.dp)
            )
        }
    }
}

@Composable
fun PlaceholderText(placeholder: String?, textColor: Color) {
    placeholder?.let {
        Text(
            text = it,
            style = MaterialTheme.typography.bodyMedium.copy(color = textColor)
        )
    }
}

fun getVisualTransformation(
    keyboardOptions: KeyboardOptions,
    passwordVisible: Boolean
): VisualTransformation {
    return if (keyboardOptions.keyboardType == KeyboardType.Password && !passwordVisible)
        PasswordVisualTransformation() else
        VisualTransformation.None
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomTextFieldWithIconsDisabled() {
    var region by remember { mutableStateOf("") }
    val isError by remember { mutableStateOf(false) }
    val errorMessage by remember { mutableStateOf("Please enter a valid email") }
    val config = TextFieldConfig(
        leadingIconRes = R.drawable.ic_password_show,
        trailingIconRes = R.drawable.ic_password_show,
        errorMessage = errorMessage,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email
        ),
        isError = isError,
        isEnabled = false,
    )

    TextFieldWithIcons(
        value = stringResource(id = R.string.dummy_text),
        onValueChange = { region = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        config = config
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomTextFieldMultiline() {
    var region by remember { mutableStateOf("") }
    val isError by remember { mutableStateOf(false) }
    val errorMessage by remember { mutableStateOf("Please enter a valid email") }
    val config = TextFieldConfig(
        errorMessage = errorMessage,
        isError = isError,
        lineCount = 3,
        autoShowClear = true
    )

    TextFieldWithIcons(
        value = stringResource(id = R.string.dummy_text),
        onValueChange = { region = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        config = config
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomTextFieldWithIconsEnabled() {
    var region by remember { mutableStateOf("") }
    val isError by remember { mutableStateOf(false) }
    val errorMessage by remember { mutableStateOf("Please enter a valid email") }

    val config = TextFieldConfig(
        leadingIconRes = R.drawable.ic_password_show,
        trailingIconRes = R.drawable.ic_password_hide,
        errorMessage = errorMessage,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email
        ),
        isError = isError,
        autoShowClear = true
    )

    TextFieldWithIcons(
        value = stringResource(id = R.string.dummy_text),
        onValueChange = { region = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        config = config
    )
}



@Preview(showBackground = true)
@Composable
fun PreviewCustomTextFieldWithIcons() {
    var query by remember { mutableStateOf("") }

    val config = TextFieldConfig(
        textPlaceholder = stringResource(id = R.string.hint_enter_transaction_id),
        errorMessage = stringResource(R.string.warning_email_not_valid),
        isError = false,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done
        ),
        backgroundColor = Neutral100,
        cursorColor = Neutral1100,
        textColor = Neutral1100,
        roundedSize = 12.dp,
        keyboardActions = KeyboardActions(
            onDone = {
            }
        ),
    )

    TextFieldWithIcons(
        value = query,
        onValueChange = {
            query = it
        },
        modifier = Modifier.fillMaxWidth(),
        config = config
    )
}
