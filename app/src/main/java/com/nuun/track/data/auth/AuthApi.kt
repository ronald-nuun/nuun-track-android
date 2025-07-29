package com.nuun.track.data.auth

import com.nuun.track.core.configs.base.dto.BaseResponseWrapper
import com.nuun.track.data.auth.dto.UserDto
import com.nuun.track.domain.auth.request.LoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("api/login")
    suspend fun postLogin(
        @Body request: LoginRequest
    ): Response<BaseResponseWrapper<UserDto>>
}