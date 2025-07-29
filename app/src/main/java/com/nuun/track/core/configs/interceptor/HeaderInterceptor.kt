package com.nuun.track.core.configs.interceptor

import com.nuun.track.core.configs.networking.TokenProvider
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor @Inject constructor(
    private val tokenProvider: TokenProvider,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("Accept", "*/*")
            .header("Accept-Encoding", "gzip, deflate, br")
            .header("Connection", "keep-alive")
            .header("Content-Type", "application/json")

        tokenProvider.getToken()?.let {
            request.header("Authorization", it)
        }
        return chain.proceed(request.build())
    }
}