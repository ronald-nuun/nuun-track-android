package com.nuun.track.ui.components.network_error

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nuun.track.R
import com.nuun.track.ui.theme.ColorTextCaption
import com.nuun.track.ui.theme.ColorTextDefault

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetworkError(
    isRefreshing: Boolean,
    refreshState: PullToRefreshState,
    onRefresh: () -> Unit
) {
    PullToRefreshBox(
        state = refreshState,
        onRefresh = onRefresh,
        isRefreshing = isRefreshing,
    ) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = stringResource(R.string.desc_no_data),
                modifier = Modifier
                    .size(100.dp)
            )

            Text(
                text = stringResource(R.string.error_network),
                style = MaterialTheme.typography.titleSmall,
                color = ColorTextDefault
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.error_network_desc),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = ColorTextCaption
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PreviewNetworkError() {
    NetworkError(false, rememberPullToRefreshState(), {})
}