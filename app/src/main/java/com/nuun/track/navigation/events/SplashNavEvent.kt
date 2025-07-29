package com.nuun.track.navigation.events

sealed class SplashNavEvent {
    object ToHomepage : SplashNavEvent()
    object ToLogin : SplashNavEvent()
}