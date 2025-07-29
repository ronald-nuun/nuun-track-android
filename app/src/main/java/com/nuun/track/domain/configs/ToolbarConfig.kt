package com.nuun.track.domain.configs

data class ToolbarConfig(
    val title: String = "",
    val showLogo: Boolean = true,
    val showTitle: Boolean = false,
    val menuTextConfig: MenuTextConfig? = null,
    val showMenuIcon: Boolean = false,
    val showBackMenu: Boolean = true,
)