package com.nuun.track.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.nuun.track.R
import com.nuun.track.core.configs.state.ResultState
import com.nuun.track.domain.auth.response.UserDomain
import com.nuun.track.domain.configs.TextFieldConfig
import com.nuun.track.navigation.screens.AuthNavScreen
import com.nuun.track.navigation.screens.HomeNavScreen
import com.nuun.track.ui.common.HandleErrorStates
import com.nuun.track.ui.components.BackgroundBox
import com.nuun.track.ui.components.button.CustomButton
import com.nuun.track.ui.components.forms.TextFieldWithIcons
import com.nuun.track.ui.shared_viewmodel.EncryptedPrefViewModel
import com.nuun.track.ui.shared_viewmodel.PrefDataStoreViewModel
import com.nuun.track.ui.theme.ColorTextDefault
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = hiltViewModel(),
    encryptedPrefViewModel: EncryptedPrefViewModel = hiltViewModel(),
    prefViewModel: PrefDataStoreViewModel = hiltViewModel(),
) {
    val loginState =
        remember { mutableStateOf<ResultState<UserDomain?>>(ResultState.Success(null)) }

    LaunchedEffect(Unit) {
        loginViewModel.resultLogin.collectLatest { result ->
            loginState.value = result
        }
    }

    HandleLoginSuccess(
        navController = navController,
        encryptedPrefViewModel = encryptedPrefViewModel,
        prefViewModel = prefViewModel,
        loginState = loginState.value,
    )

    HandleErrorStates(
        navController = navController,
        states = arrayOf(
            loginState.value,
        )
    )

    BackgroundBox {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.label_welcome_to_nuun),
                color = ColorTextDefault,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(10.dp))
            EmailTextField(
                loginViewModel = loginViewModel
            )
            Spacer(modifier = Modifier.height(10.dp))
            PasswordTextField(
                loginViewModel = loginViewModel
            )
            Spacer(modifier = Modifier.height(20.dp))
            LoginButton(
                navController = navController,
                loginViewModel = loginViewModel,
                encryptedPrefViewModel = encryptedPrefViewModel
            )
        }
    }
}

@Composable
fun EmailTextField(
    loginViewModel: LoginViewModel
) {
    val email by loginViewModel.email.collectAsState()
    val isErrorEmail by loginViewModel.isErrorEmail.collectAsState()

    val config = TextFieldConfig(
        textPlaceholder = stringResource(id = R.string.hint_enter_your_email),
        leadingIconRes = R.drawable.ic_email,
        errorMessage = stringResource(R.string.warning_email_not_valid),
        isError = isErrorEmail,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
    )

    TextFieldWithIcons(
        value = email,
        onValueChange = {
            loginViewModel.setEmail(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .onFocusChanged { loginViewModel.onEmailFocusChange(it.isFocused) },
        config = config
    )
}

@Composable
fun PasswordTextField(
    loginViewModel: LoginViewModel,
) {
    val password by loginViewModel.password.collectAsState()
    val isErrorPassword by loginViewModel.isErrorPassword.collectAsState()
    val errorMessagePassword = stringResource(R.string.warning_password_empty)

    val config = TextFieldConfig(
        textPlaceholder = stringResource(id = R.string.hint_enter_your_password),
        leadingIconRes = R.drawable.ic_password,
        errorMessage = errorMessagePassword,
        isError = isErrorPassword,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
    )

    TextFieldWithIcons(
        value = password,
        onValueChange = {
            loginViewModel.setPassword(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .onFocusChanged { loginViewModel.onPasswordFocusChange(it.isFocused) },
        config = config
    )
}

@Composable
fun LoginButton(
    navController: NavController,
    loginViewModel: LoginViewModel,
    encryptedPrefViewModel: EncryptedPrefViewModel
) {
    val isLoading by loginViewModel.isLoading.collectAsState()
    val isEnabledLogin by loginViewModel.isEnabledLogin.collectAsState()
    val email by loginViewModel.email.collectAsState()
    val password by loginViewModel.password.collectAsState()

    val showBiometric = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (encryptedPrefViewModel.getRefreshToken() != null) {
            showBiometric.value = true
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomButton(
            modifier = Modifier.weight(1f),
            isEnabled = isEnabledLogin,
            text = stringResource(R.string.label_login),
            isLoadingEnabled = isLoading,
            onClick = {
                // TODO: hit the API
//                loginViewModel.login(email, password)

                // TODO: remove this later
                navController.navigate(HomeNavScreen.Homepage.route) {
//                    popUpTo(AuthNavScreen.Login.route) { inclusive = true }
                }
            },
        )
    }
}

@Composable
fun HandleLoginSuccess(
    navController: NavController,
    encryptedPrefViewModel: EncryptedPrefViewModel,
    prefViewModel: PrefDataStoreViewModel,
    loginState: ResultState<UserDomain?>,
) {
    LaunchedEffect(loginState) {
        (loginState as? ResultState.Success)?.data?.let { data ->
            data.token?.let { at ->
                encryptedPrefViewModel.saveUserCredentials(
                    token = at,
                )
                prefViewModel.storeUser(
                    data.copy(token = null)
                )
                navController.navigate(HomeNavScreen.Homepage.route) {
                    popUpTo(AuthNavScreen.Login.route) { inclusive = true }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(rememberNavController())
}