package com.nuun.track.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nuun.track.R
import com.nuun.track.domain.configs.MenuTextConfig
import com.nuun.track.domain.configs.ToolbarConfig
import com.nuun.track.ui.theme.ColorBgToolbar
import com.nuun.track.ui.theme.ColorTextDefault
import com.nuun.track.ui.theme.ColorTextInput

@Composable
fun CustomToolbar(
    config: ToolbarConfig = ToolbarConfig(),
    onBackClick: () -> Unit,
    onMenuClick: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = ColorBgToolbar
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onBackClick.invoke() },
                enabled = config.showBackMenu, // disables click when invisible
                modifier = if (config.showBackMenu) Modifier else Modifier.alpha(0f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back_arrow),
                    tint = ColorTextDefault,
                    contentDescription = stringResource(R.string.desc_back),
                    modifier = Modifier.size(24.dp)
                )
            }
            if(config.showTitle) {
                Text(
                    config.title,
                    color = ColorTextDefault,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 40.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            when {
                config.showLogo -> {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = stringResource(R.string.desc_logo),
                        modifier = Modifier
                            .height(50.dp)
                            .padding(end = 10.dp)
                    )
                }

                config.menuTextConfig != null -> {
                    Text(
                        config.menuTextConfig.text,
                        color = if (config.menuTextConfig.isClickable) ColorTextDefault else ColorTextInput,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .then(
                                if (config.menuTextConfig.isClickable) Modifier.clickable { onMenuClick?.invoke() }
                                else Modifier
                            ),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                config.showMenuIcon -> {
                    Image(
                        painter = painterResource(id = R.drawable.ic_menu),
                        contentDescription = stringResource(R.string.desc_menu),
                        modifier = Modifier
                            .clickable {
                                onMenuClick?.invoke()
                            }
                            .height(24.dp)
                            .padding(end = 10.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDefaultToolbar() {
    CustomToolbar(
        onBackClick = {},
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewToolbarText() {
    CustomToolbar(
        config = ToolbarConfig(
            showLogo = false,
            showTitle = true,
            title = "Nuun"
        ),
        onBackClick = {},
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewFullToolbar() {
    CustomToolbar(
        config = ToolbarConfig(
            showLogo = true,
            showTitle = true,
            title = "Nuun"
        ),
        onBackClick = {},
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewFullToolbarMenu() {
    CustomToolbar(
        config = ToolbarConfig(
            showLogo = false,
            showTitle = true,
            showMenuIcon = true,
            title = "Nuun"
        ),
        onBackClick = {},
        onMenuClick = {},
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewToolbarTextMenu() {
    CustomToolbar(
        config = ToolbarConfig(
            showLogo = false,
            showTitle = true,
            showMenuIcon = false,
            menuTextConfig = MenuTextConfig(
                text = "Edit",
                isClickable = false
            ),
            title = "Nuun"
        ),
        onBackClick = {},
        onMenuClick = {},
    )
}