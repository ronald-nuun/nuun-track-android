package com.nuun.track.ui.components.texts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuun.track.R
import com.nuun.track.domain.configs.TextIconConfig
import com.nuun.track.ui.components.divider.Divider
import com.nuun.track.ui.theme.ColorTextDefault
import com.nuun.track.ui.theme.ColorTextInput

@Composable
fun TextIcon(
    config: TextIconConfig,
    onClick: (() -> Unit)? = null,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp)
                .then(
                    onClick?.let { Modifier.clickable { it() } } ?: run { Modifier }
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Background with Shape (handle indentation logic)
            if (config.useIndentation) {
                IconSection(config)
                Spacer(modifier = Modifier.width(12.dp))
            } else {
                Box(Modifier.height(24.dp))
            }

            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = config.title,
                        fontSize = 12.sp,
                        color = ColorTextDefault,
                        overflow = if (config.useEllipsis) TextOverflow.Ellipsis else TextOverflow.Clip,
                        maxLines = if (config.useEllipsis) 1 else Int.MAX_VALUE,
                    )
                    config.desc?.let { helper ->
                        Text(
                            text = helper,
                            fontSize = 12.sp,
                            color = ColorTextInput,
                            modifier = Modifier
                                .padding(top = 4.dp)
                        )
                    }
                }
                config.message?.let { message ->
                    Text(
                        text = message,
                        fontSize = 12.sp,
                        color = ColorTextInput,
                    )
                }
            }

            TrailingIconSection(config)
        }

        if (config.showDivider)
            Divider(
                config.fullDivider,
                config.useIndentation,
                config.dividerColor
            )
    }
}

@Composable
fun IconSection(
    config: TextIconConfig
) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .then(
                if (config.showBackgroundShape)
                    Modifier.background(config.backgroundColor, RoundedCornerShape(10.dp))
                else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        when {
            config.vector != null -> {
                Icon(
                    imageVector = config.vector,
                    contentDescription = stringResource(R.string.desc_image),
                    tint = config.drawableTint,
                    modifier = Modifier.size(config.iconSize)
                )
            }

            config.drawableRes != null -> {
                Image(
                    painter = painterResource(id = config.drawableRes),
                    contentDescription = stringResource(R.string.desc_image),
                    modifier = Modifier.size(config.iconSize)
                )
            }
        }
    }
}

@Composable
fun TrailingIconSection(
    config: TextIconConfig
) {
    if (config.showTrailingIcon) {
        when {
            config.useTrailingVector -> {
                Icon(
                    imageVector = config.trailingVectorOrXml,
                    contentDescription = stringResource(R.string.desc_image),
                    tint = ColorTextDefault,
                    modifier = Modifier.size(config.trailingSize)
                )
            }

            else -> {
                Image(
                    painter = painterResource(id = config.trailingRes),
                    contentDescription = stringResource(R.string.desc_image),
                    modifier = Modifier.size(config.trailingSize)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewCustomIcon() {
    Column {
        TextIcon(
            TextIconConfig(
                title = "Account and Security",
                vector = Icons.Default.Person,
                showDivider = true,
                fullDivider = false
            )
        )

        TextIcon(
            TextIconConfig(
                title = "Privacy Settings",
                vector = Icons.Default.Lock,
                showDivider = true,
                fullDivider = false,
                showBackgroundShape = true,
                showTrailingIcon = true,
            )
        )

        TextIcon(
            TextIconConfig(
                title = "Version 1.0.0",
                vector = Icons.Default.Info,
                showDivider = true,
            )
        )

        TextIcon(
            TextIconConfig(
                title = "Version 1.0",
                message = "Latest",
                useIndentation = false,
                showDivider = true,
                fullDivider = false
            )
        )

        TextIcon(
            TextIconConfig(
                title = "Version 1.00",
                desc = "version code",
                message = "Latest",
                showTrailingIcon = true,
                useIndentation = false,
                showDivider = true,
                fullDivider = false
            )
        )

        TextIcon(
            TextIconConfig(
                title = "akdjshakjdshajkdhasjkdaskldjlkasdjklasjdkasljdklasjskal",
                drawableRes = R.drawable.logo,
                useIndentation = true,
                showTrailingIcon = true,
                useTrailingVector = false,
                trailingRes = R.drawable.ic_back_arrow,
                useEllipsis = true
            ),
        )
    }
}