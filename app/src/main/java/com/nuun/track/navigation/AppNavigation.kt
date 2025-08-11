package com.nuun.track.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nuun.track.navigation.screens.AuthNavScreen
import com.nuun.track.navigation.screens.HomeNavScreen
import com.nuun.track.ui.screens.homepage.HomepageScreen
import com.nuun.track.ui.screens.login.LoginScreen
import com.nuun.track.ui.screens.reservation.detail.ReservationDetailScreen
import com.nuun.track.ui.screens.splash.SplashScreen
import com.nuun.track.utility.logger.ConsoleLogger

@Composable
fun AppNavigation(
    startDestination: String = NavScreen.Splash.route
) {
    val logger = ConsoleLogger()
    val navController = rememberNavController()
    val backStackEntries by navController.currentBackStack.collectAsState()


    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, _, _ ->
            logger.warning("Current Stack: ${backStackEntries.map { it.destination.route }}")
        }
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable(NavScreen.Splash.route) {
            SplashScreen(navController)
        }

        composable(AuthNavScreen.Login.route) {
            LoginScreen(navController)
        }

        composable(HomeNavScreen.Homepage.route) {
            HomepageScreen(navController)
        }

        composable(HomeNavScreen.ReservationDetail.route) {
            ReservationDetailScreen(navController)
        }

    }

}