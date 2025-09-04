package com.nuun.track.core.configs.permissions

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.nuun.track.R
import com.nuun.track.domain.configs.AlertConfig
import com.nuun.track.ui.components.alert_dialog.CustomAlertDialog
import com.nuun.track.utility.extensions.openCameraPermissionSettings

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPermission(
    onPermissionGranted: (Boolean) -> Unit
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val permissionRequestAttempted = remember { mutableStateOf(false) }
    val isDialogVisible = remember { mutableStateOf(false) }
    val permissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA
    )

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            if (!permissionState.status.isGranted && permissionRequestAttempted.value) {
                isDialogVisible.value = true
            }
        }
    }

    LaunchedEffect(permissionState) {
        if (!permissionState.status.isGranted && !permissionRequestAttempted.value) {
            permissionRequestAttempted.value = true
            permissionState.launchPermissionRequest()
        }
    }

    if (isDialogVisible.value) {
        CustomAlertDialog(
            alertConfig = AlertConfig(
                title = stringResource(R.string.title_camera_permission),
                message = stringResource(R.string.label_camera_permission_permanently_denied),
                isVisible = isDialogVisible.value,
                onConfirm = {
                    context.openCameraPermissionSettings()
                    isDialogVisible.value = false
                },
                showDismissButton = false,
                onDismiss = {
                    isDialogVisible.value = true
                }
            ),
        )
    }

    if (permissionState.status.isGranted) {
        onPermissionGranted(true)
    }
}