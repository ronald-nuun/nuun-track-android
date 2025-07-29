package com.nuun.track.data.auth

import com.nuun.track.core.configs.networking.ApiHandler
import com.nuun.track.core.configs.networking.TokenProvider
import com.nuun.track.domain.auth.AuthRepository
import com.nuun.track.domain.auth.request.LoginRequest
import com.nuun.track.domain.auth.request.TokenRequest
import com.nuun.track.domain.auth.response.UserDomain
import com.squareup.moshi.Moshi

class AuthRepositoryImpl(
    moshi: Moshi,
    private val authApi: AuthApi,
    private val token: TokenProvider
) : AuthRepository {

    private val tokenAdapter = moshi.adapter(TokenRequest::class.java)
    private val loginAdapter = moshi.adapter(LoginRequest::class.java)

    override suspend fun postLogin(loginRequest: LoginRequest): UserDomain? = ApiHandler.handleApi {
        authApi.postLogin(loginRequest)
    }?.data?.toDomain()

}