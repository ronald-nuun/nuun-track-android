package com.nuun.track.navigation

sealed class NavScreen(val route: String) {

    object Splash : NavScreen("Splash")

}