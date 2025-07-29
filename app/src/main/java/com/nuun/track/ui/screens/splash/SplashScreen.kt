package com.nuun.track.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.nuun.track.R
import com.nuun.track.navigation.NavScreen
import com.nuun.track.navigation.events.SplashNavEvent
import com.nuun.track.navigation.screens.AuthNavScreen
import com.nuun.track.navigation.screens.HomeNavScreen
import com.nuun.track.ui.components.BackgroundBox

@Composable
fun SplashScreen(
    navController: NavController,
    splashViewModel: SplashScreenViewModel = hiltViewModel(),
) {

    val navigationEvent by splashViewModel.navigationEvent.collectAsState()

    LaunchedEffect(true) {
        splashViewModel.decideNavigation()
    }

    LaunchedEffect(navigationEvent) {
        when(navigationEvent) {
            is SplashNavEvent.ToHomepage -> {
                navController.navigate(HomeNavScreen.Homepage.route) {
                    popUpTo(NavScreen.Splash.route) { inclusive = true }
                }
            }

            else -> {
                navController.navigate(AuthNavScreen.Login.route) {
                    popUpTo(NavScreen.Splash.route) { inclusive = true }
                }
            }
        }
    }

    BackgroundBox {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = stringResource(R.string.desc_logo),
            modifier = Modifier.size(150.dp)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewSplashScreen() {
    SplashScreen(
        navController = rememberNavController(),
        splashViewModel = hiltViewModel(),
    )
}