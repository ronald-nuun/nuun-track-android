package com.nuun.track.ui.components.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nuun.track.R
import com.nuun.track.ui.theme.ColorBgToolbar
import com.nuun.track.ui.theme.ColorTextDefault

@Composable
fun DefaultCard(
    modifier: Modifier = Modifier,
    bgColor: Color = ColorBgToolbar,
    label: String? = null,
    content: @Composable () -> Unit
) {
    Column {
        label?.let {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = it,
//                style = MaterialTheme.typography.bodySmall,
                color = ColorTextDefault
            )

            Spacer(Modifier.height(10.dp))
        }

        Card(
            modifier = modifier,
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = bgColor,
            ),
        ) {
            content()
        }
    }
}

@Preview
@Composable
fun PreviewDefaultCard() {
    DefaultCard(
        label = "Card"
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.dummy_text),
                color = ColorTextDefault
            )
        }
    }
}