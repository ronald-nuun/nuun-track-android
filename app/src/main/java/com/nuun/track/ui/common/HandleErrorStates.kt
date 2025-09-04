package com.nuun.track.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.nuun.track.core.configs.exception.ErrorApiStateHandler
import com.nuun.track.core.configs.provider.ErrorHandlerProviderImpl
import com.nuun.track.core.configs.state.ResultState
import com.nuun.track.navigation.screens.AuthNavScreen
import com.nuun.track.ui.screens.LocalTokenManager
import com.nuun.track.ui.shared_viewmodel.EncryptedPrefViewModel
import com.nuun.track.ui.shared_viewmodel.PrefDataStoreViewModel
import com.nuun.track.utility.extensions.toastShortExt
import com.nuun.track.utility.utils.logout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@Composable
fun HandleErrorStates(
    prefViewModel: PrefDataStoreViewModel = hiltViewModel(),
    encryptedPrefViewModel: EncryptedPrefViewModel = hiltViewModel(),
    navController: NavController,
    vararg states: ResultState<*>,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val tokenManager = LocalTokenManager.current
    val mainScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    states.forEach { state ->
        (state as? ResultState.Error)?.exception?.let { throwable ->
            ErrorApiStateHandler.handleErrorState(
                throwable = throwable,
                onHandleableException = { exception ->
                    context.toastShortExt("${exception.message}, ${exception.stackTrace}")
                },
                onGeneralException = {
                    ErrorHandlerProviderImpl().generalError(context, lifecycleOwner, throwable)
                },
                onTokenExpired = {
                    mainScope.launch {
                        logout(
                            prefViewModel = prefViewModel,
                            encryptedPrefViewModel = encryptedPrefViewModel,
                            tokenManager = tokenManager,
                            navController = navController
                        )
                        navController.navigate(AuthNavScreen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                },
            )
        }
    }
}