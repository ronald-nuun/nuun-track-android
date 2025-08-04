package com.nuun.track.utility.utils

import androidx.navigation.NavController
import com.nuun.track.core.configs.networking.TokenManager
import com.nuun.track.navigation.screens.AuthNavScreen
import com.nuun.track.ui.shared_viewmodel.EncryptedPrefViewModel
import com.nuun.track.ui.shared_viewmodel.PrefDataStoreViewModel

fun logout(
    prefViewModel: PrefDataStoreViewModel,
    encryptedPrefViewModel: EncryptedPrefViewModel,
    tokenManager: TokenManager,
    navController: NavController
) {
    tokenManager.logout()
    prefViewModel.clearDataStore()
    encryptedPrefViewModel.clearCredentials()
    navController.navigate(AuthNavScreen.Login.route) {
        popUpTo(0) { inclusive = true }
    }
}
