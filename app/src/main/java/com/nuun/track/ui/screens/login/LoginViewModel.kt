package com.nuun.track.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nuun.track.core.configs.state.ResultState
import com.nuun.track.domain.auth.request.LoginRequest
import com.nuun.track.domain.auth.response.UserDomain
import com.nuun.track.domain.auth.usecase.LoginUseCase
import com.nuun.track.utility.extensions.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    private val _resultLogin by lazy { MutableSharedFlow<ResultState<UserDomain?>>() }
    val resultLogin: SharedFlow<ResultState<UserDomain?>> get() = _resultLogin

    private val _isLoading by lazy { MutableStateFlow(false) }
    val isLoading: StateFlow<Boolean> get() = _isLoading.asStateFlow()

    private val _email by lazy { MutableStateFlow("") }
    val email: StateFlow<String> get() = _email.asStateFlow()

    private val _hasInteractedWithEmail by lazy { MutableStateFlow(false) }
    private val hasInteractedWithEmail: StateFlow<Boolean> get() = _hasInteractedWithEmail.asStateFlow()

    val isErrorEmail = combine(hasInteractedWithEmail, email) { interacted, email ->
        interacted && !email.isValidEmail()
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    private val _password by lazy { MutableStateFlow("") }
    val password: StateFlow<String> get() = _password.asStateFlow()

    private val _hasInteractedWithPassword by lazy { MutableStateFlow(false) }
    private val hasInteractedWithPassword: StateFlow<Boolean> get() = _hasInteractedWithPassword.asStateFlow()

    val isErrorPassword = combine(hasInteractedWithPassword, password) { interacted, password ->
        interacted && password.isEmpty()
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    val isEnabledLogin = combine(hasInteractedWithEmail, isErrorEmail, hasInteractedWithPassword, isErrorPassword) { interactedEmail, errorEmail, interactedPassword, errorPassword ->
        !errorEmail && !errorPassword && interactedEmail && interactedPassword
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    fun setEmail(email: String) {
        _email.value = email
    }

    fun onEmailFocusChange(isFocus: Boolean) {
        if(isFocus && !hasInteractedWithEmail.value) {
            _hasInteractedWithEmail.value = true
        }
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun onPasswordFocusChange(isFocus: Boolean) {
        if(isFocus && !hasInteractedWithPassword.value) {
            _hasInteractedWithPassword.value = true
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val request = LoginRequest(
                email = email,
                password = password,
            )
            _isLoading.value = true
            loginUseCase.execute(LoginUseCase.RequestValues(request)).result.let {
                _isLoading.value = false
                _resultLogin.emit(it)
            }
        }
    }

}