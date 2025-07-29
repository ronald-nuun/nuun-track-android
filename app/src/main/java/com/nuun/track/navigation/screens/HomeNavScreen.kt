package com.nuun.track.navigation.screens

sealed class HomeNavScreen(val route: String) {
    object Homepage : HomeNavScreen("Homepage")
    object OrderDetail: HomeNavScreen("OrderDetail")
    object FormAttachment: HomeNavScreen("FormAttachment")
}