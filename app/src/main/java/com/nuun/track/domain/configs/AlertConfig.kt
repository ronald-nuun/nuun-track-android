package com.nuun.track.domain.configs

data class AlertConfig(
    val title: String,
    val isVisible: Boolean,
    val showDismissButton: Boolean = true,
    val message: String? = null,
    val onConfirm: (Map<String, String>?) -> Unit,
    val onDismiss: () -> Unit = {},
)