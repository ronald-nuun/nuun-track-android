package com.nuun.track.ui.components.button

import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.nuun.track.R
import com.nuun.track.utility.enums.ButtonStyle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color? = null,
    onClick: () -> Unit,
    style: ButtonStyle = ButtonStyle.PRIMARY,
    isLoadingEnabled: Boolean = false,
    isEnabled: Boolean = true
) {
    val buttonEnabled = !isLoadingEnabled
    val keyboardController = LocalSoftwareKeyboardController.current

    when (style) {
        ButtonStyle.OUTLINED -> {
            OutlinedButton(
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = style.contentColor,
                    disabledContentColor = style.textColor.copy(alpha = 0.6f)
                ),
                onClick = {
                    if (buttonEnabled) {
                        keyboardController?.hide()
                        onClick.invoke()
                    }
                },
                modifier = modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                enabled = isEnabled && buttonEnabled,
            ) {
                ButtonContent(
                    isLoadingEnabled,
                    text,
                    color = textColor ?: style.textColor
                )
            }
        }

        else -> {
            Button(
                colors = ButtonColors(
                    containerColor = style.containerColor,
                    contentColor = style.contentColor,
                    disabledContainerColor = style.containerColor.copy(alpha = 0.6f),
                    disabledContentColor = style.contentColor.copy(alpha = 0.6f)
                ),
                onClick = {
                    if (buttonEnabled) {
                        keyboardController?.hide()
                        onClick.invoke()
                    }
                },
                modifier = modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                enabled = isEnabled && buttonEnabled
            ) {
                ButtonContent(
                    isLoadingEnabled,
                    text,
                    textColor ?: style.textColor
                )
            }
        }
    }
}

@Composable
fun ButtonContent(isLoadingEnabled: Boolean, text: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (isLoadingEnabled) {
            Image(
                painter = rememberDrawablePainter(
                    AppCompatResources.getDrawable(
                        LocalContext.current,
                        R.drawable.loading
                    )
                ),
                contentDescription = stringResource(R.string.desc_loading),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewButtonPrimary() {
    var isLoading by remember { mutableStateOf(false) }

    CustomButton(
        style = ButtonStyle.PRIMARY,
        text = stringResource(R.string.dummy_text),
        onClick = {
            isLoading = true
            CoroutineScope(Dispatchers.Main).launch {
                delay(2000)
                isLoading = false
            }
        },
        isLoadingEnabled = isLoading
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewButtonPrimaryDisabled() {
    var isLoading by remember { mutableStateOf(false) }

    CustomButton(
        style = ButtonStyle.PRIMARY,
        text = stringResource(R.string.dummy_text),
        onClick = {
            isLoading = true
            CoroutineScope(Dispatchers.Main).launch {
                delay(2000)
                isLoading = false
            }
        },
        isEnabled = false,
        isLoadingEnabled = isLoading
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewButtonSecondary() {
    var isLoading by remember { mutableStateOf(false) }

    CustomButton(
        style = ButtonStyle.SECONDARY,
        text = stringResource(R.string.dummy_text),
        onClick = {
            isLoading = true
            CoroutineScope(Dispatchers.Main).launch {
                delay(2000)
                isLoading = false
            }
        },
        isLoadingEnabled = isLoading
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewButtonSecondaryDisabled() {
    var isLoading by remember { mutableStateOf(false) }

    CustomButton(
        style = ButtonStyle.SECONDARY,
        text = stringResource(R.string.dummy_text),
        onClick = {
            isLoading = true
            CoroutineScope(Dispatchers.Main).launch {
                delay(2000)
                isLoading = false
            }
        },
        isEnabled = false,
        isLoadingEnabled = isLoading
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewButtonTertiary() {
    var isLoading by remember { mutableStateOf(false) }

    CustomButton(
        style = ButtonStyle.TERTIARY,
        text = stringResource(R.string.dummy_text),
        onClick = {
            isLoading = true
            CoroutineScope(Dispatchers.Main).launch {
                delay(2000)
                isLoading = false
            }
        },
        isLoadingEnabled = isLoading
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewButtonTertiaryDisabled() {
    var isLoading by remember { mutableStateOf(false) }

    CustomButton(
        style = ButtonStyle.TERTIARY,
        text = stringResource(R.string.dummy_text),
        onClick = {
            isLoading = true
            CoroutineScope(Dispatchers.Main).launch {
                delay(2000)
                isLoading = false
            }
        },
        isEnabled = false,
        isLoadingEnabled = isLoading
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewButtonOutline() {
    var isLoading by remember { mutableStateOf(false) }

    CustomButton(
        style = ButtonStyle.OUTLINED,
        text = stringResource(R.string.dummy_text),
        onClick = {
            isLoading = true
            CoroutineScope(Dispatchers.Main).launch {
                delay(2000)
                isLoading = false
            }
        },
        isLoadingEnabled = isLoading
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewButtonOutlineDisabled() {
    var isLoading by remember { mutableStateOf(false) }

    CustomButton(
        style = ButtonStyle.OUTLINED,
        text = stringResource(R.string.dummy_text),
        onClick = {
            isLoading = true
            CoroutineScope(Dispatchers.Main).launch {
                delay(2000)
                isLoading = false
            }
        },
        isEnabled = false,
        isLoadingEnabled = isLoading
    )
}