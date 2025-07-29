package com.nuun.track.ui.screens.homepage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.nuun.track.R
import com.nuun.track.domain.configs.TextFieldConfig
import com.nuun.track.ui.components.forms.TextFieldWithIcons
import com.nuun.track.ui.theme.ColorTextDefault
import com.nuun.track.ui.theme.Neutral100
import com.nuun.track.ui.theme.Neutral1000
import com.nuun.track.ui.theme.Neutral1100

@Composable
fun HomepageScreen(
    navController: NavController,
    homepageViewModel: HomepageViewModel = hiltViewModel(),
) {
    Scaffold(
        containerColor = Neutral1000,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
                .padding(10.dp)
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(id = R.string.label_find_transaction),
                color = ColorTextDefault,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            SearchTextField(
                homepageViewModel
            )
            Spacer(modifier = Modifier.height(18.dp))
            // space antar item dalam list 10

        }
    }
}


@Composable
fun SearchTextField(
    homepageViewModel: HomepageViewModel
) {
    val query by homepageViewModel.query.collectAsState()

    val config = TextFieldConfig(
        textPlaceholder = stringResource(id = R.string.hint_enter_transaction_id),
        errorMessage = stringResource(R.string.warning_email_not_valid),
        isError = false,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done
        ),
        backgroundColor = Neutral100,
        textColor = Neutral1100,
        roundedSize = 12.dp,
        keyboardActions = KeyboardActions(
            onDone = {
                // TODO: change this to hit the Recent Orders API
            }
        ),
    )

    TextFieldWithIcons(
        value = query,
        onValueChange = {
            homepageViewModel.setQuery(query)
        },
        modifier = Modifier.fillMaxWidth(),
        config = config
    )
}


@Preview(showSystemUi = true)
@Composable
fun PreviewHomepageScreen() {
    HomepageScreen(rememberNavController())
}