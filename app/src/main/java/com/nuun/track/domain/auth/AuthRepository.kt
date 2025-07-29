package com.nuun.track.domain.auth

import com.nuun.track.domain.auth.request.LoginRequest
import com.nuun.track.domain.auth.response.UserDomain

interface AuthRepository {

    suspend fun postLogin(loginRequest: LoginRequest): UserDomain?

}