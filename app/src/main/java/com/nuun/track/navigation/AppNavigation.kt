package com.nuun.track.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nuun.track.navigation.screens.AuthNavScreen
import com.nuun.track.navigation.screens.HomeNavScreen
import com.nuun.track.ui.screens.homepage.HomepageScreen
import com.nuun.track.ui.screens.login.LoginScreen
import com.nuun.track.ui.screens.reservation.detail.ReservationDetailScreen
import com.nuun.track.ui.screens.reservation.form.ReservationFormScreen
import com.nuun.track.ui.screens.splash.SplashScreen
import com.nuun.track.utility.consts.ExtraConst
import com.nuun.track.utility.consts.NuunTag
import com.nuun.track.utility.logger.NavigationLogger

@Composable
fun AppNavigation(
    startDestination: String = NavScreen.Splash.route
) {
    val logger = NavigationLogger()
    val destLogger = NavigationLogger(NuunTag.NAVIGATION_DESTINATION)
    val navController = rememberNavController()
    val visibleEntries by navController.visibleEntries.collectAsState()

    LaunchedEffect(visibleEntries) {
        val stack = visibleEntries.mapNotNull { it.destination.route }
        logger.warning("Full Stack: $stack")
    }

    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { controller, destination, _ ->
            val previous = controller.previousBackStackEntry?.destination?.route
            val current = destination.route
            destLogger.warning("Current route: $previous > to Destination: $current")
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

        composable(
            route = "ReservationDetail?${ExtraConst.EXTRA_RESERVATION}={${ExtraConst.EXTRA_RESERVATION}}&${ExtraConst.EXTRA_REFRESH}={${ExtraConst.EXTRA_REFRESH}}",
            arguments = listOf(
                navArgument(ExtraConst.EXTRA_RESERVATION) { type = NavType.StringType },
                navArgument(ExtraConst.EXTRA_REFRESH) {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) {
            ReservationDetailScreen(
                navController = navController
            )
        }

        composable(HomeNavScreen.ReservationForm.route) {
            ReservationFormScreen(navController)
        }

    }

}