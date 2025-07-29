package com.nuun.track.core.configs.networking

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor @Inject constructor(
    private val tokenProvider: TokenProvider,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenProvider.getToken()

        val requestBuilder = chain.request().newBuilder()
        token?.let {
            requestBuilder.addHeader("Authorization", it)
        }

        return chain.proceed(requestBuilder.build())
    }
}
