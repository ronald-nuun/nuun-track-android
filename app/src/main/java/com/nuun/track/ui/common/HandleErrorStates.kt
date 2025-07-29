package com.nuun.track.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.nuun.track.core.configs.exception.ErrorApiStateHandler
import com.nuun.track.core.configs.provider.ErrorHandlerProviderImpl
import com.nuun.track.core.configs.state.ResultState
import com.nuun.track.navigation.screens.AuthNavScreen
import com.nuun.track.ui.screens.LocalTokenManager
import com.nuun.track.utility.extensions.toastShortExt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Composable
fun HandleErrorStates(
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
                    context.toastShortExt(exception.message)
                },
                onGeneralException = {
                    ErrorHandlerProviderImpl().generalError(context, lifecycleOwner, throwable)
                },
                onTokenExpired = {
                    navController.navigate(AuthNavScreen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
            )
        }
    }
}