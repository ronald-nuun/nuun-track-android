package com.nuun.track.navigation.screens

sealed class AuthNavScreen(val route: String) {
    object Login : AuthNavScreen("Login")
    object ForgetPassword : AuthNavScreen("ForgetPassword")
}