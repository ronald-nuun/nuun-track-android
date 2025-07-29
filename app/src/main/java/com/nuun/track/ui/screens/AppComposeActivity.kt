package com.nuun.track.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import com.nuun.track.core.configs.networking.TokenManager
import com.nuun.track.domain.provider.ErrorHandlerProvider
import com.nuun.track.navigation.AppNavigation
import com.nuun.track.ui.theme.NuunTrackTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

val LocalErrorHandlerProvider = compositionLocalOf<ErrorHandlerProvider> {
    error("ErrorHandlerProvider not provided")
}

val LocalTokenManager = compositionLocalOf<TokenManager> {
    error("TokenProvider not provided")
}

@AndroidEntryPoint
class AppComposeActivity : ComponentActivity() {

    @Inject
    lateinit var errorHandlerProvider: ErrorHandlerProvider

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompositionLocalProvider(
                LocalErrorHandlerProvider provides errorHandlerProvider,
                LocalTokenManager provides tokenManager,
            ) {
                NuunTrackTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        Column(
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            AppNavigation()
                        }
                    }
                }
            }
        }
    }
}