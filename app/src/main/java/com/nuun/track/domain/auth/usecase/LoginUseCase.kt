package com.nuun.track.domain.auth.usecase

import com.nuun.track.core.configs.base.BaseSuspendUseCase
import com.nuun.track.core.configs.state.ResultState
import com.nuun.track.domain.auth.AuthRepository
import com.nuun.track.domain.auth.request.LoginRequest
import com.nuun.track.domain.auth.response.UserDomain
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) : BaseSuspendUseCase<LoginUseCase.RequestValues, LoginUseCase.ResponseValues>(){

    class RequestValues(val user: LoginRequest) : BaseSuspendUseCase.RequestValues

    class ResponseValues(val result: ResultState<UserDomain?>) : BaseSuspendUseCase.ResponseValues

    override suspend fun execute(requestValues: RequestValues): ResponseValues {
        return try {
            val result = authRepository.postLogin(requestValues.user)
            ResponseValues(
                ResultState.Success(result)
            )
        } catch (e: Exception) {
            ResponseValues(
                ResultState.Error(e)
            )
        }
    }
}