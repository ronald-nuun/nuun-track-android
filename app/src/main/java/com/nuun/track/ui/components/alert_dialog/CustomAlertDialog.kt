package com.nuun.track.ui.components.alert_dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nuun.track.R
import com.nuun.track.domain.configs.AlertConfig
import com.nuun.track.domain.configs.TextFieldConfig
import com.nuun.track.ui.components.button.CustomButton
import com.nuun.track.ui.components.forms.TextFieldWithIcons
import com.nuun.track.ui.theme.ColorBgNav
import com.nuun.track.ui.theme.ColorTextDefault
import com.nuun.track.utility.enums.ButtonStyle
import com.nuun.track.utility.logger.ConsoleLogger

@Composable
fun CustomAlertDialog(
    inputFields: List<Pair<String, TextFieldConfig>>? = null,
    alertConfig: AlertConfig,
    confirmText: String = stringResource(R.string.label_setting),
    dismissText: String = stringResource(R.string.label_later),
) {
    if (alertConfig.isVisible) {

        val inputStates = remember { mutableStateMapOf<String, MutableState<TextFieldConfig>>() }
        val userInputs = remember { mutableStateMapOf<String, String>() }
        val errorRequired = stringResource(R.string.warning_field_required)

        LaunchedEffect(inputFields) {
            inputFields?.forEach { (label, fieldConfig) ->
                val errorMsg =
                    if (fieldConfig.isRequired && fieldConfig.state?.value?.text.isNullOrBlank()) {
                        errorRequired
                    } else {
                        fieldConfig.validate(fieldConfig.state?.value?.text ?: "")
                    }

                inputStates[label] = mutableStateOf(
                    fieldConfig.copy(
                        isError = errorMsg != null,
                        errorMessage = errorMsg
                    )
                )
            }
        }

        AlertDialog(
            containerColor = ColorBgNav,
            onDismissRequest = alertConfig.onDismiss,
            title = {
                TitleSection(alertConfig.title)
            },
            text = {
                Column {
                    alertConfig.message?.let {
                        MessageSection(it)
                    }
                    InputSection(
                        inputStates = inputStates,
                        userInputs = userInputs,
                    )
                }
            },
            confirmButton = {
                ConfirmSection(
                    alertConfig = alertConfig,
                    dismissText = dismissText,
                    confirmText = confirmText,
                    userInputs = userInputs,
                    inputStates = inputStates
                )
            }
        )
    }
}

@Composable
fun TitleSection(title: String) {
    Text(
        text = title,
        modifier = Modifier.fillMaxWidth(),
        color = ColorTextDefault,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center
    )
}

@Composable
fun MessageSection(title: String) {
    Text(
        text = title,
        color = ColorTextDefault,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun FieldLabel(label: String) {
    Text(
        text = label,
        color = ColorTextDefault,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(bottom = 5.dp)
    )
}

@Composable
fun InputSection(
    inputStates: SnapshotStateMap<String, MutableState<TextFieldConfig>>,
    userInputs: SnapshotStateMap<String, String>
) {
    inputStates.forEach { (label, config) ->
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            if (label.isNotEmpty()) {
                FieldLabel(label)
            }
            TextFieldWithIcons(
                value = userInputs[label] ?: "",
                onValueChange = { newValue ->
                    userInputs[label] = newValue
                    val configValue = config.value
                    val errorMsg = when {
                        configValue.isRequired && newValue.isBlank() -> "This field is required"
                        else -> configValue.validate(newValue)
                    }

                    inputStates[label]?.value = configValue.copy(
                        isError = !errorMsg.isNullOrEmpty(),
                        errorMessage = errorMsg
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                config = config.value
            )
        }
    }
}

@Composable
fun ConfirmSection(
    alertConfig: AlertConfig,
    dismissText: String,
    confirmText: String,
    userInputs: SnapshotStateMap<String, String>,
    inputStates: SnapshotStateMap<String, MutableState<TextFieldConfig>>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End)
    ) {
        if (alertConfig.showDismissButton) {
            CustomButton(
                text = dismissText,
                onClick = alertConfig.onDismiss,
                style = ButtonStyle.OUTLINED,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            )
        }
        CustomButton(
            text = confirmText,
            onClick = {
                alertConfig.onConfirm(userInputs)
            },
            isEnabled = inputStates.isEmpty() || inputStates.all { (label, state) ->
                val config = state.value
                !config.isError && (!config.isRequired || userInputs[label]?.isNotBlank() == true)
            },
            style = ButtonStyle.PRIMARY,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        )
    }
}

@Preview
@Composable
fun PreviewSimpleDialog() {
    var isDialogVisible by remember { mutableStateOf(true) }

    val config = AlertConfig(
        title = stringResource(R.string.dummy_text),
        message = "Are you sure you want to proceed?",
        isVisible = isDialogVisible,
        onConfirm = { /* Handle Confirm */ },
        onDismiss = { isDialogVisible = false },
    )

    CustomAlertDialog(
        alertConfig = config
    )
}

@Preview
@Composable
fun PreviewDialogWithOneInput() {
    val logger = ConsoleLogger()
    var isDialogVisible by remember { mutableStateOf(true) }
    val deviceNameState = remember { mutableStateOf(TextFieldValue("Camera main indoor")) }

    val deviceNameConfig = TextFieldConfig(
        state = deviceNameState,
        textPlaceholder = "Enter device name",
        leadingIconRes = R.drawable.ic_email,
        keyboardOptions = KeyboardOptions.Default
    )

    val config = AlertConfig(
        title = stringResource(R.string.dummy_text),
        isVisible = isDialogVisible,
        onConfirm = { logger.debug("New Name: ${deviceNameState.value.text}") },
        onDismiss = { isDialogVisible = false },
    )

    CustomAlertDialog(
        inputFields = listOf(stringResource(R.string.dummy_text) to deviceNameConfig),
        alertConfig = config
    )

}


@Preview
@Composable
fun PreviewDialogWithManyInput() {
    val logger = ConsoleLogger()
    var isDialogVisible by remember { mutableStateOf(true) }
    val firstInputState = remember { mutableStateOf(TextFieldValue("")) }
    val secondInputState = remember { mutableStateOf(TextFieldValue("")) }

    val firstInputConfig = TextFieldConfig(
        state = firstInputState,
        textPlaceholder = "Name",
        isError = firstInputState.value.text.isEmpty(),
        errorMessage = "Field cannot be empty"
    )

    val secondInputConfig = TextFieldConfig(
        state = secondInputState,
        textPlaceholder = "Phone",
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        isRequired = true,
        validate = {
            if (it.length < 6) {
                "Min number is 6"
            } else {
                null
            }
        }
    )

    val config = AlertConfig(
        title = "Device Settings",
        isVisible = isDialogVisible,
        onConfirm = { result -> // this return Map<String, String>
            result?.entries?.forEach {
                when (it.key) {
                    "Name" -> logger.debug("Name: ${it.value}")
                    "Phone" -> logger.debug("Phone: ${it.value}")
                }
            }
        },
        onDismiss = { isDialogVisible = false },
    )

    CustomAlertDialog(
        inputFields = listOf(
            "Name" to firstInputConfig,
            "Phone" to secondInputConfig
        ),
        alertConfig = config
    )
}